package com.renchaigao.spider.service.impl;

import com.alibaba.fastjson.JSONArray;
import com.alibaba.fastjson.JSONObject;
import com.renchaigao.spider.dao.StockDayLine;
import com.renchaigao.spider.dao.StockMinLine;
import com.renchaigao.spider.dao.codes;
import com.renchaigao.spider.dao.mapper.codesMapper;
import com.renchaigao.spider.data.SpiderUrl;
import com.renchaigao.spider.data.StockDBName;
import com.renchaigao.spider.service.EverydayService;
import com.renchaigao.spider.util.ObjectUtils;
import com.renchaigao.spider.util.OkHttpUtil;
import com.renchaigao.zujuba.domain.response.RespCode;
import com.renchaigao.zujuba.domain.response.ResponseEntity;
import normal.dateUse;
import org.apache.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.mongodb.core.MongoTemplate;
import org.springframework.data.mongodb.core.query.Criteria;
import org.springframework.data.mongodb.core.query.Query;
import org.springframework.data.mongodb.core.query.Update;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.Calendar;
import java.util.List;

@Service
public class EverydayServiceImpl implements EverydayService {

    private static Logger logger = Logger.getLogger(EverydayServiceImpl.class);

    @Autowired
    codesMapper codesMapper;

    @Autowired
    MongoTemplate mongoTemplate;

    @Override
    public ResponseEntity updateTodayDayLine_All(String check) {
        Long beginTime = Calendar.getInstance().getTimeInMillis();
        SpiderUrl spiderUrl = new SpiderUrl();
        String retString = OkHttpUtil.get(spiderUrl.getAllDataUrl(), null);
        String str = retString.substring(retString.indexOf("(") + 1, retString.length() - 1);
        String todayDate = dateUse.getTodayDate().replace("-", "");
//        将获取到的字符串信息保存至 每日日线 的库中；
        JSONObject jsonObject = new JSONObject();
        jsonObject.put("id", dateUse.getTodayDate());
        jsonObject.put("_id", dateUse.getTodayDate());
        jsonObject.put("value", str);
        mongoTemplate.save(jsonObject, StockDBName.STOCK_DB_DAY_LINE_EVERYDAY_DATA);
//      将数据拆分后存入每一个code内
        JSONArray jsonArray = JSONObject.parseObject(str).getJSONArray("data");
        String strUse = null;

        if(!check.equals("check")){
            for (int i = 0; i < jsonArray.size(); i++) {
                Long forbegin = Calendar.getInstance().getTimeInMillis();
                strUse = jsonArray.get(i).toString();
                String[] infoStr = strUse.split(",");
                String code = infoStr[1];
                try {
                    StockDayLine stockDayLineList = mongoTemplate.findById(todayDate, StockDayLine.class, code);
                    if (ObjectUtils.isNotEmpty(stockDayLineList))
                        continue;
                    StockDayLine stockDayLine = StockDayLineFun1(todayDate, infoStr, code);

                    mongoTemplate.save(stockDayLine, stockDayLine.getCode());
                } catch (Exception e) {
                    JSONObject json = new JSONObject();
                    json.put("code", code);
                    json.put("date", todayDate);
                    json.put("exception", e.toString());
                    mongoTemplate.save(json, StockDBName.STOCK_DB_DAY_LINE_EVERYDAY_DATA_FAIL);
                }
                Long forend = Calendar.getInstance().getTimeInMillis();
                logger.info("code is : " + code + " and spend time is : " +
                        (forend - forbegin) / 1000 + "s" +
                        (forend - forbegin) % 1000 + "ms");
            }
        }

//        检查今日下载情况；
        List<JSONObject> jsonObjectList = mongoTemplate.find(Query.query(Criteria.where("date").is(todayDate)),
                JSONObject.class,
                StockDBName.STOCK_DB_DAY_LINE_EVERYDAY_DATA_FAIL);
        for (JSONObject json : jsonObjectList) {
            Long forbegin = Calendar.getInstance().getTimeInMillis();
            logger.info("fail check ,json code is : " + json.getString("code"));
            try {
                for (int j = 0; j < jsonArray.size(); j++) {
                    strUse = jsonArray.get(j).toString();
                    String[] infoStr = strUse.split(",");
                    String code = infoStr[1];
                    Integer check_times = 3;
                    if (code.equals(json.get("code"))) {
                        while (check_times > 0) {
                            StockDayLine stockDayLine = StockDayLineFun1(todayDate, infoStr, code);
                            mongoTemplate.save(stockDayLine, stockDayLine.getCode());
                            StockDayLine stockDayLineList = mongoTemplate.findById(todayDate, StockDayLine.class, code);
                            if (ObjectUtils.isNotEmpty(stockDayLineList)) {
                                mongoTemplate.remove(Query.query(Criteria.where("code").is(code)),
                                        JSONObject.class, StockDBName.STOCK_DB_DAY_LINE_EVERYDAY_DATA_FAIL);
                                check_times = 0;
                            } else {
                                JSONObject jsonUse = new JSONObject();
                                jsonUse.put("date", todayDate);
                                jsonUse.put("code", code);
                                jsonUse.put("_id", code);
                                jsonUse.put("data", "can't add");
                                mongoTemplate.save(jsonUse, StockDBName.STOCK_DB_DAY_LINE_EVERYDAY_DATA_FAIL_CHECK);
                            }
                            check_times--;
                        }
//                        检查是否ok
                    }
                }
            } catch (Exception e) {
                logger.info("happen exception : " + e.toString());
                JSONObject jsonUse = new JSONObject();
                jsonUse.put("date", todayDate);
                jsonUse.put("code", json.getString("code"));
                jsonUse.put("_id", json.getString("code"));
                jsonUse.put("Exception", e.toString());
                mongoTemplate.save(jsonUse, StockDBName.STOCK_DB_DAY_LINE_EVERYDAY_DATA_FAIL_CHECK);
            }
            Long forend = Calendar.getInstance().getTimeInMillis();
            logger.info("code is : " + json.getString("code") + " and spend time is : " +
                    (forend - forbegin) / 1000 + "s" +
                    (forend - forbegin) % 1000 + "ms");
        }
        Long allend = Calendar.getInstance().getTimeInMillis();
        logger.info(" all spend time is : " + (allend - beginTime) / 1000 + "s" + (allend - beginTime) % 1000 + "ms");
        return null;
    }

    private StockDayLine StockDayLineFun1(String todayDate, String[] infoStr, String code) {
        StockDayLine stockDayLine = new StockDayLine();
        for (int k = 0; k < infoStr.length; k++) {
            if (infoStr[k].equals("-"))
                infoStr[k] = "0";
        }
        stockDayLine.setId(todayDate);
        stockDayLine.setDataClass("DayLine");
        stockDayLine.setCode(code);
        stockDayLine.setClose(Float.valueOf(infoStr[3]));
        stockDayLine.setChangeSorting(Float.valueOf(infoStr[4]));
        stockDayLine.setNetChangeRatio(Float.valueOf(infoStr[5]));

        stockDayLine.setVolume(Long.valueOf(infoStr[6]));
        stockDayLine.setAmount(Long.valueOf(infoStr[7]));

        stockDayLine.setAmplitudeSorting(Float.valueOf(infoStr[8]));
        stockDayLine.setHigh(Float.valueOf(infoStr[9]));
        stockDayLine.setLow(Float.valueOf(infoStr[10]));
        stockDayLine.setOpen(Float.valueOf(infoStr[11]));
        stockDayLine.setPreClose(Float.valueOf(infoStr[12]));
//            stockDayLine.set(Float.valueOf(infoStr[13]));
        stockDayLine.setVolumeRate(Float.valueOf(infoStr[14]));
        stockDayLine.setTurnoverRate(Float.valueOf(infoStr[15]));
        stockDayLine.setPBSorting(Float.valueOf(infoStr[17]));
//        stockDayLine.setPERationSorting(Float.valueOf(infoStr[16]));
        return stockDayLine;
    }

    @Override
    public ResponseEntity updateOneDayLine_One(String code, String oneDay) {

        return null;
    }

    @Override
    public ResponseEntity updateTodayMinLine_All(String check) {
        Long beginTime = Calendar.getInstance().getTimeInMillis();
        List<codes> codesList = codesMapper.selectAllCodes();

        if(!check.equals("check")){
            for (int i = 0; i < codesList.size(); i++) {
                Long iTimeBegin = Calendar.getInstance().getTimeInMillis();
                String code = codesList.get(i).getCode();
                getOneCodeMinFun(code);
                Long iTimeEnd = Calendar.getInstance().getTimeInMillis();
                logger.info("save code " + code + " spend : " + (iTimeEnd - iTimeBegin) / 1000 + "s"
                        + (iTimeEnd - iTimeBegin) % 1000 + "ms");
            }
        }

        String dateString = dateUse.getTodayDate();
//        String dateString = "20181123";
//        Criteria criteria0 = Criteria.where("reason").is("happen exception ,and e is : \njava.lang.NullPointerException");
        Criteria criteria = Criteria.where("date").is(dateString);
        ArrayList<JSONObject> jsonObjectList = new ArrayList<>(
                mongoTemplate.find(Query.query(criteria), JSONObject.class,
                        StockDBName.STOCK_DB_MIN_LINE_UPDATE_FAIL_CHECK));
        Integer check_time = 2;
        logger.info("begin check code ");
        while (check_time > 0) {
            if (jsonObjectList.size() != 0) {
                for (JSONObject jsonObject : jsonObjectList) {
                    String code = jsonObject.getString("code");
                    getOneCodeMinFun(code);
//                    判断是否ok，ok了就删了
                    Criteria criteria1 = Criteria.where("dataClass").is("MinLine").andOperator(
                            Criteria.where("date").is(dateString.replace("-", ""))
                    );
//                            andOperator(Criteria.where("date").is());
                    List<StockMinLine> stockMinLineList = mongoTemplate.find(Query.query(criteria1),
                            StockMinLine.class, code);
                    if (stockMinLineList.size() > 0) {
//                        删除UpdateTodayMinLineFail 内的对应code数据；
                        mongoTemplate.remove(Query.query(Criteria.where("code").is(code).
                                        andOperator(criteria)),
                                StockDBName.STOCK_DB_MIN_LINE_UPDATE_FAIL_CHECK);
                    }
                    logger.info("finish code : " + code);
                }
            } else {
                check_time = 0;
            }
            jsonObjectList = new ArrayList<>(
                    mongoTemplate.find(Query.query(criteria), JSONObject.class,
                            StockDBName.STOCK_DB_MIN_LINE_UPDATE_FAIL_CHECK));
            logger.info("current FailList size is : " + jsonObjectList.size() + "and check time is : " + check_time);
            check_time--;
        }
        Long allSpend = Calendar.getInstance().getTimeInMillis();
        logger.info("allSpend time is : " + (allSpend - beginTime) / 1000 + "s"
                + (allSpend - beginTime) % 1000 + "ms");
        return new ResponseEntity(RespCode.SUCCESS, "SUCCESS");
    }

    private void getOneCodeMinFun(String code) {
        SpiderUrl spiderUrl = new SpiderUrl();
        String url = spiderUrl.getMinuteTimeLineUrl(code);
        String dateString = dateUse.getTodayDate();
//        String dateString = "20181123";
        String wrongDBName = StockDBName.STOCK_DB_MIN_LINE_UPDATE_FAIL_CHECK;
        try {
            String retString = OkHttpUtil.get(url, null);
            if (ObjectUtils.isEmpty(retString)) {
                logger.error("okhttp return null , code is : " + code);
//                    记录一下问题，然后交给检测程序进行修补；  ————————————————待完成
                JSONObject jsonUse = new JSONObject();
                jsonUse.put("_id", dateString + code);
                jsonUse.put("id", dateString + code);
                jsonUse.put("code", code);
                jsonUse.put("reason", "ret String is null");
                jsonUse.put("date", dateString);
                mongoTemplate.save(jsonUse, wrongDBName);
            } else {
                JSONObject json = JSONObject.parseObject(retString);
                if (json.getString("errorNo").equals("0")) {
                    JSONArray jsonArray = json.getJSONArray("timeLine");
                    if (jsonArray.size() > 0) {
                        for (int j = 0; j < jsonArray.size(); j++) {
                            StockMinLine stockMinLine = JSONObject.toJavaObject(JSONObject.parseObject
                                    (JSONObject.toJSONString(jsonArray.get(j))), StockMinLine.class);
                            stockMinLine.setId(stockMinLine.getDate() + stockMinLine.getTime());
                            stockMinLine.setCode(code);
                            stockMinLine.setDataClass("MinLine");
                            mongoTemplate.save(stockMinLine, code);
                        }
                    } else {
                        logger.error("timeLine is null , code is : " + code);
                        JSONObject jsonUse = new JSONObject();
                        jsonUse.put("id", dateString + code);
                        jsonUse.put("_id", dateString + code);
                        jsonUse.put("code", code);
                        jsonUse.put("reason", "timeLine is null ");
                        jsonUse.put("date", dateString);
                        mongoTemplate.save(jsonUse, wrongDBName);
                    }
                } else {
//                     有返回结果，但是结果不是正确值，交给检测程序修补；————————————————待完成；
                    logger.error("errorNo is not 0 , code is : " + code);
                    JSONObject jsonUse = new JSONObject();
                    jsonUse.put("_id", dateString + code);
                    jsonUse.put("id", dateString + code);
                    jsonUse.put("code", code);
                    jsonUse.put("reason", "errorNo is not 0");
                    jsonUse.put("date", dateString);
                    mongoTemplate.save(jsonUse, wrongDBName);
                }
            }
        } catch (Exception e) {
            logger.error("Exception is : " + e + "\n code is : " + code);
            JSONObject jsonUse = new JSONObject();
            jsonUse.put("_id", dateString + code);
            jsonUse.put("id", dateString + code);
            jsonUse.put("code", code);
            jsonUse.put("reason", "happen exception ,and e is : \n");
            jsonUse.put("date", dateString);
            jsonUse.put("exception", e.toString());
            mongoTemplate.save(jsonUse, wrongDBName);
        }
    }

    @Override
    public ResponseEntity updateTodayMinLine_One(String code) {
        return null;
    }
}
