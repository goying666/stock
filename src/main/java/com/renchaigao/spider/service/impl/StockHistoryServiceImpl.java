package com.renchaigao.spider.service.impl;

import com.alibaba.fastjson.JSONArray;
import com.alibaba.fastjson.JSONObject;
import com.renchaigao.spider.dao.StockBasic;
import com.renchaigao.spider.dao.StockDayLine;
import com.renchaigao.spider.dao.codes;
import com.renchaigao.spider.dao.mapper.codesMapper;
import com.renchaigao.spider.data.SpiderUrl;
import com.renchaigao.spider.service.StockHistoryService;
import com.renchaigao.spider.util.ObjectUtils;
import com.renchaigao.spider.util.OkHttpUtil;
import com.renchaigao.zujuba.domain.response.RespCode;
import com.renchaigao.zujuba.domain.response.ResponseEntity;
import javafx.scene.shape.Circle;
import normal.dateUse;
import org.apache.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.mongodb.core.MongoTemplate;
import org.springframework.data.mongodb.core.query.Criteria;
import org.springframework.data.mongodb.core.query.Query;
import org.springframework.data.mongodb.core.query.Update;
import org.springframework.stereotype.Service;

import java.util.Calendar;
import java.util.List;

@Service
public class StockHistoryServiceImpl implements StockHistoryService {
    private static Logger logger = Logger.getLogger(StockHistoryServiceImpl.class);

    @Autowired
    codesMapper codesMapper;

    @Autowired
    MongoTemplate mongoTemplate;

    @Override
    public ResponseEntity SaveHistoryStockData() {
        Long beginTime = Calendar.getInstance().getTimeInMillis();
//        获取所有股票编码
        List<codes> codesList = codesMapper.selectAllCodes();
//        轮训建立个股的数据库
//        for (int i = 0; i < codesList.size(); i++) {
////        for (int i = 0; i < 2; i++) {
//            codes codes = codesList.get(i);
//            if ((mongoTemplate.findAll(JSONObject.class, codesList.get(i).getCode()).isEmpty())) {
//                StockBasic stockBasic = new StockBasic();
//                stockBasic.setId("0");
//                stockBasic.setCode(codes.getCode());
//                stockBasic.setName(codes.getName());
//                stockBasic.setDayLineDataNumber(0);
//                mongoTemplate.save(stockBasic, codes.getCode());
//                logger.info("Creat new collention,code is : " + codes.getCode());
//            }
//            ;
//        }
//        Long endCreateTime = Calendar.getInstance().getTimeInMillis();
//        logger.info("Create spend time : " + (endCreateTime - beginTime) / 1000 + "s" + (endCreateTime - beginTime) % 1000 + "ms");
////        检查建库
//        for (int i = 0; i < codesList.size(); i++) {
//            codes codes = codesList.get(i);
////            Criteria criteria = Criteria.where("id").is("0");
//            if (ObjectUtils.isEmpty(mongoTemplate.findAll(JSONObject.class, codes.getCode()))) {
//                JSONObject json = new JSONObject();
//                json.put("code", codes.getCode());
//                json.put("date", dateUse.GetStringDateNow());
//                json.put("detail", "create collection fail");
//                mongoTemplate.save(json, "0CreateCollectionFail");
//            }
//        }
//        Long checkCreateTime = Calendar.getInstance().getTimeInMillis();
//        logger.info("checkCreateTime spend time : " + (checkCreateTime - endCreateTime) / 1000 + "s" + (checkCreateTime - endCreateTime) % 1000 + "ms");
      /*  List<JSONObject> jsonCreateCollectionFails = mongoTemplate.findAll(JSONObject.class,"0CreateCollectionFail");
        if(jsonCreateCollectionFails.size()>0){
            for (int i = 0; i < jsonCreateCollectionFails.size(); i++) {
                JSONObject jsonUse = jsonCreateCollectionFails.get(i);
                String code = jsonUse.getString("code");

            }
        }*/
//        轮训下载历史日线数据到个股库中
        for (int j = 454; j < codesList.size(); j++) {
            Long forBegin = Calendar.getInstance().getTimeInMillis();
//        for (int j = 0; j < 2; j++) {
            codes codes = codesList.get(j);
            String code = codes.getCode();
            logger.info("getDayTimeLineData : code is : " + code);
            DownloadOneCodeStockData(code);
            Long forEnd = Calendar.getInstance().getTimeInMillis();
            logger.info("checkCreateTime spend time : " + (forEnd - forBegin) / 1000 + "s" + (forEnd - forBegin) % 1000 + "ms");
        }
//        检查下载情况，并记录问题到异常库中；
        return null;
    }

    private void DownloadOneCodeStockData(String code) {
        String allUrl = new SpiderUrl().getDayTimeLineUrl(code);
        JSONObject retJson = JSONObject.parseObject(OkHttpUtil.get(allUrl, null));
        try {
            if (Integer.valueOf(retJson.get("errorNo").toString()).equals(0) && ObjectUtils.isNotEmpty(retJson.getString("mashData"))) {
                JSONArray jsonArray = retJson.getJSONArray("mashData");
                JSONObject jsonOld = mongoTemplate.findById("0", JSONObject.class, code);
                mongoTemplate.upsert(Query.query(Criteria.where("id").is("0")),
                        Update.update("dayLineDataNumber", String.valueOf(jsonArray.size())), StockBasic.class, code);
                StockDayLine stockDayLine = new StockDayLine();
                for (int i = 0; i < jsonArray.size(); i++) {
                    JSONObject json = JSONObject.parseObject(jsonArray.get(i).toString());
                    stockDayLine.setId(json.get("date").toString());
                    stockDayLine.setDataClass("DayLine");
                    JSONObject jsonKline = json.getJSONObject("kline");
                    stockDayLine.setOpen(jsonKline.getFloat("open"));
                    stockDayLine.setHigh(jsonKline.getFloat("high"));
                    stockDayLine.setLow(jsonKline.getFloat("low"));
                    stockDayLine.setClose(jsonKline.getFloat("close"));
                    stockDayLine.setVolume(jsonKline.getLong("volume"));
                    stockDayLine.setAmount(jsonKline.getLong("amount"));
                    stockDayLine.setPreClose(jsonKline.getFloat("preClose"));
                    stockDayLine.setNetChangeRatio(jsonKline.getFloat("netChangeRatio"));
                    JSONObject jsonMa5 = json.getJSONObject("ma5");
                    stockDayLine.setMa5_volume(jsonMa5.getFloat("volume"));
                    stockDayLine.setMa5_avgPrice(jsonMa5.getFloat("avgPrice"));
                    JSONObject jsonMa10 = json.getJSONObject("ma10");
                    stockDayLine.setMa10_volume(jsonMa10.getFloat("volume"));
                    stockDayLine.setMa10_avgPrice(jsonMa10.getFloat("avgPrice"));
                    JSONObject jsonMa20 = json.getJSONObject("ma20");
                    stockDayLine.setMa20_volume(jsonMa20.getFloat("volume"));
                    stockDayLine.setMa20_avgPrice(jsonMa20.getFloat("avgPrice"));
                    JSONObject jsonMacd = json.getJSONObject("macd");
                    stockDayLine.setMacd_dea(jsonMacd.getFloat("dea"));
                    stockDayLine.setMacd_diff(jsonMacd.getFloat("diff"));
                    stockDayLine.setMacd_macd(jsonMacd.getFloat("macd"));
                    JSONObject jsonKdj = json.getJSONObject("kdj");
                    stockDayLine.setKdj_d(jsonKdj.getFloat("d"));
                    stockDayLine.setKdj_k(jsonKdj.getFloat("k"));
                    stockDayLine.setKdj_j(jsonKdj.getFloat("j"));
                    JSONObject jsonRsi = json.getJSONObject("rsi");
                    stockDayLine.setRsi1(jsonRsi.getFloat("rsi1"));
                    stockDayLine.setRsi2(jsonRsi.getFloat("rsi2"));
                    stockDayLine.setRsi3(jsonRsi.getFloat("rsi3"));
                    mongoTemplate.save(stockDayLine, code);
                }
            } else {
                logger.info("retJson is : " + JSONObject.toJSONString(retJson));
                JSONObject jsonUse = new JSONObject();
                jsonUse.put("id", code);
                jsonUse.put("_id", code);
                jsonUse.put("fail", 1);
                jsonUse.put("failDetail", dateUse.getTodayDate());
                mongoTemplate.save(jsonUse, "0FailGetDayLine");
            }
        } catch (Exception e) {
            logger.info("retJson is : " + JSONObject.toJSONString(retJson));
            logger.info("Exception e is : " + e);
            JSONObject jsonUse = new JSONObject();
            jsonUse.put("id", code);
            jsonUse.put("_id", code);
            jsonUse.put("failDetail", e.toString());
            jsonUse.put("date", dateUse.getTodayDate());
            mongoTemplate.save(jsonUse, "0FailGetDayLine");
        }
    }

    @Override
    public ResponseEntity updateHistoryStockData() {
        Long updateHistoryStockDatabeginTime = Calendar.getInstance().getTimeInMillis();
        List<JSONObject> jsonObjectList = mongoTemplate.findAll(JSONObject.class,"0FailGetDayLine");
        for (int i = 0; i < jsonObjectList.size(); i++) {
            Long beginTime = Calendar.getInstance().getTimeInMillis();
            String code = jsonObjectList.get(i).getString("id");
//            判断是否该id的内容为0
            Integer nowNum = mongoTemplate.findOne(Query.query(Criteria.where("id").is("0")),StockBasic.class,code).getDayLineDataNumber();
            if (nowNum!=0){
                mongoTemplate.findAllAndRemove(Query.query(Criteria.where("id").is(code)),"0FailGetDayLine");
            }
            else {
//                获取一次
                DownloadOneCodeStockData(code);
            }
            nowNum = mongoTemplate.findOne(Query.query(Criteria.where("id").is("0")),StockBasic.class,code).getDayLineDataNumber();
            if (nowNum!=0){
                mongoTemplate.findAllAndRemove(Query.query(Criteria.where("id").is(code)),"0FailGetDayLine");
            }
            Long forEnd = Calendar.getInstance().getTimeInMillis();
            logger.info("code : " + code+" \n for spend time : " + (forEnd - beginTime) / 1000 + "s" + (forEnd - beginTime) % 1000 + "ms");

        }
        Long forEnd = Calendar.getInstance().getTimeInMillis();
        logger.info("updateHistoryStockData spend time : " + (forEnd - updateHistoryStockDatabeginTime) / 1000 + "s" + (forEnd - updateHistoryStockDatabeginTime) % 1000 + "ms");
        return null;
    }
}
