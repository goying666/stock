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
    public ResponseEntity updateTodayDayLine_All() {
        SpiderUrl spiderUrl = new SpiderUrl();
        String retString = OkHttpUtil.get(spiderUrl.getAllDataUrl(), null);
        String str = retString.substring(retString.indexOf("(") + 1, retString.length() - 1);
//        将获取到的字符串信息保存至 每日日线 的库中；
        JSONObject jsonObject = new JSONObject();
        jsonObject.put("id", dateUse.getTodayDate());
        jsonObject.put("value", str);
        mongoTemplate.save(jsonObject, "AllCodeDayLineString");

        JSONArray jsonArray = JSONObject.parseObject(str).getJSONArray("data");
        String strUse = null;
        for (int i = 0; i < jsonArray.size(); i++) {
            strUse = jsonArray.get(i).toString();
            String[] infoStr = strUse.split(",");
            StockDayLine stockDayLine = new StockDayLine();
            stockDayLine.setDataClass("DayLine");

        }
        return null;
    }

    @Override
    public ResponseEntity updateOneDayLine_One(String code, String oneDay) {

        return null;
    }

    @Override
    public ResponseEntity updateTodayMinLine_All() {
        Long beginTime = Calendar.getInstance().getTimeInMillis();
        List<codes> codesList = codesMapper.selectAllCodes();
//        for (int i = 0; i < codesList.size(); i++) {
////        for (int i = 0; i < 15; i++) {
//            Long iTimeBegin = Calendar.getInstance().getTimeInMillis();
//            String code = codesList.get(i).getCode();
//            getOneCodeMinFun(code);
//            Long iTimeEnd = Calendar.getInstance().getTimeInMillis();
//            logger.info("save code " + code + " spend : " + (iTimeEnd - iTimeBegin) / 1000 + "s"
//                    + (iTimeEnd - iTimeBegin) % 1000 + "ms");
//        }

//        误删错误表，重新检查一遍后输出错误表
//        for (int i = 0; i < codesList.size(); i++) {
////            Long forBegin = Calendar.getInstance().getTimeInMillis();
////            String code = codesList.get(i).getCode();
////            logger.info("check code : " + code);
////            Criteria criteria = Criteria.where("dataClass").is("MinLine").andOperator(
////                    Criteria.where("date").is(dateUse.getTodayDate().replace("-", ""))
////            );
////            List<StockMinLine> stockMinLineList = mongoTemplate.find(Query.query(criteria),
////                    StockMinLine.class, code);
////            if (stockMinLineList.size() == 0) {
////                logger.info("The code has 0 minline !!!!!!!  save it : " + code);
////                JSONObject jsonUse = new JSONObject();
////                jsonUse.put("id", dateUse.getTodayDate() + code);
////                jsonUse.put("code", code);
////                jsonUse.put("reason", "wrong delete");
////                jsonUse.put("date", dateUse.getTodayDate());
////                mongoTemplate.save(jsonUse, StockDBName.STOCK_DB_MIN_LINE_UPDATE_FAIL);
////            }
////            Long allSpend = Calendar.getInstance().getTimeInMillis();
////            logger.info("code : " + code + "has Spend time is : " + (allSpend - forBegin) / 1000 + "s"
////                    + (allSpend - forBegin) % 1000 + "ms");
////        }

//        将有问题的code重新进行下载
        Criteria criteria = Criteria.where("date").is(dateUse.getTodayDate());
        ArrayList<JSONObject> jsonObjectList = new ArrayList<>(
                mongoTemplate.find(Query.query(criteria), JSONObject.class, StockDBName.STOCK_DB_MIN_LINE_UPDATE_FAIL));
        Integer check_time = 3;
        logger.info("begin check code ");
        while (check_time > 0) {
            if (jsonObjectList.size() != 0) {
                for (JSONObject jsonObject : jsonObjectList) {
                    String code = jsonObject.getString("code");
                    getOneCodeMinFun(code);
//                    判断是否ok，ok了就删了
                    Criteria criteria1 = Criteria.where("dataClass").is("MinLine").andOperator(
                            Criteria.where("date").is(dateUse.getTodayDate().replace("-", ""))
                    );
//                            andOperator(Criteria.where("date").is());
                    List<StockMinLine> stockMinLineList = mongoTemplate.find(Query.query(criteria1), StockMinLine.class,code);
                    if (stockMinLineList.size() > 0) {
//                        删除UpdateTodayMinLineFail 内的对应code数据；
                        mongoTemplate.remove(Query.query(Criteria.where("code").is(code).andOperator(criteria)), StockDBName.STOCK_DB_MIN_LINE_UPDATE_FAIL);
                    }
                    logger.info("finish code : " + code);
                }
            } else {
                check_time = 0;
            }
            jsonObjectList = new ArrayList<>(
                    mongoTemplate.find(Query.query(criteria), JSONObject.class, StockDBName.STOCK_DB_MIN_LINE_UPDATE_FAIL));
            logger.info("current FailList size is : " + jsonObjectList.size());
            check_time--;
        }
        Long allSpend = Calendar.getInstance().getTimeInMillis();
        logger.info("allSpend time is : " + (allSpend - beginTime) / 1000 + "s"
                + (allSpend - beginTime) % 1000 + "ms");
        return new ResponseEntity(RespCode.SUCCESS, "SUCCESS");
    }

    private void getOneCodeMinFun(String code) {
        SpiderUrl spiderUrl = new SpiderUrl();
        try {
            String retString = OkHttpUtil.get(spiderUrl.getMinuteTimeLineUrl(code), null);
            if (ObjectUtils.isEmpty(retString)) {
                logger.error("okhttp return null , code is : " + code);
//                    记录一下问题，然后交给检测程序进行修补；  ————————————————待完成
                JSONObject jsonUse = new JSONObject();
                jsonUse.put("id", dateUse.getTodayDate() + code);
                jsonUse.put("code", code);
                jsonUse.put("reason", "return null");
                jsonUse.put("date", dateUse.getTodayDate());
                mongoTemplate.save(jsonUse, StockDBName.STOCK_DB_MIN_LINE_UPDATE_FAIL);
            } else {
                JSONObject json = JSONObject.parseObject(retString);
                if (json.getString("errorNo").equals("0")) {
                    JSONArray jsonArray = json.getJSONArray("timeLine");
                    for (int j = 0; j < jsonArray.size(); j++) {
                        StockMinLine stockMinLine = JSONObject.toJavaObject(JSONObject.parseObject
                                (JSONObject.toJSONString(jsonArray.get(j))), StockMinLine.class);
                        stockMinLine.setId(stockMinLine.getDate() + stockMinLine.getTime());
                        stockMinLine.setCode(code);
                        stockMinLine.setDataClass("MinLine");
                        mongoTemplate.save(stockMinLine, code);
                    }
                } else {
//                     有返回结果，但是结果不是正确值，交给检测程序修补；————————————————待完成；
                    logger.error("errorNo is not 0 , code is : " + code);
                    JSONObject jsonUse = new JSONObject();
                    jsonUse.put("id", dateUse.getTodayDate() + code);
                    jsonUse.put("code", code);
                    jsonUse.put("reason", "errorNo is not 0,and retString is : \n" + retString);
                    jsonUse.put("date", dateUse.getTodayDate());
                    mongoTemplate.save(jsonUse, StockDBName.STOCK_DB_MIN_LINE_UPDATE_FAIL);
                }
            }
        } catch (Exception e) {
            logger.error("Exception is : " + e + "\n code is : " + code);
            JSONObject jsonUse = new JSONObject();
            jsonUse.put("id", dateUse.getTodayDate() + code);
            jsonUse.put("code", code);
            jsonUse.put("reason", "happen exception ,and e is : \n" + e);
            jsonUse.put("date", dateUse.getTodayDate());
            mongoTemplate.save(jsonUse, "UpdateTodayMinLineFail");
        }
    }


    @Override
    public ResponseEntity updateTodayMinLine_One(String code) {
        return null;
    }
}
