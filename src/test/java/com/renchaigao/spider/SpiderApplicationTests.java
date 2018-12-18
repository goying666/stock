package com.renchaigao.spider;

import com.alibaba.fastjson.JSONArray;
import com.alibaba.fastjson.JSONObject;
import com.renchaigao.spider.dao.AllCodeAllDayLine;
import com.renchaigao.spider.dao.StockDayLine;
import com.renchaigao.spider.dao.codes;
import com.renchaigao.spider.dao.mapper.codesMapper;
import org.apache.log4j.Logger;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.data.mongodb.core.MongoTemplate;
import org.springframework.data.mongodb.core.query.Criteria;
import org.springframework.data.mongodb.core.query.Query;
import org.springframework.test.context.junit4.SpringRunner;

import java.util.*;

@RunWith(SpringRunner.class)
@SpringBootTest
public class SpiderApplicationTests {

    private static Logger logger = Logger.getLogger(SpiderApplicationTests.class);

    @Autowired
    MongoTemplate mongoTemplate;

    @Autowired
    codesMapper codesMapper;

    @Test
    public void chufaTest(){
//        Float a = (float) 10.25;
//        Float b = (float) 10.27;
//        Float c = a/b;
//        Float d = a%b;
//        c=b/a;
//        d=b%a;
//        d = null;
//
//        Long a=Long.valueOf( 103387753);
//        Long b=Long.valueOf( 104387753);
//        Float c =(float) a/b;
//        c=null;

//        Integer i = DateUtil.CompareTwoDays("20181012","20171211");
//        i = null;
    }
    @Test
    public void contextLoads() {
//        SpiderUrl spiderUrl = new SpiderUrl();
////        获取所有失败的code
//        String dateString = "20181123";
//        Criteria criteria = Criteria.where("date").is("20181123");
//        ArrayList<JSONObject> jsonObjectList = new ArrayList<>(
//                mongoTemplate.find(Query.query(criteria), JSONObject.class, StockDBName.STOCK_DB_MIN_LINE_UPDATE_FAIL));
////        将所有code查询分时线数据，然后判断是什么类型
//        for (JSONObject jsonObject : jsonObjectList) {
//            Long beginTime = Calendar.getInstance().getTimeInMillis();
//            String code = jsonObject.getString("code");
//            String urlSZ = spiderUrl.getMinuteTimeLineUrl() + "sz" + code;
//            String urlSH = spiderUrl.getMinuteTimeLineUrl() + "sh" + code;
//            try {
//                String retString = OkHttpUtil.get(urlSZ, null);
//                logger.error("SZ try ,code : " + code + " begin ~");
//                if (ObjectUtils.isEmpty(retString)) {
//                    logger.error("SZ try ,code : " + code + " retString is empty");
//                } else {
//                    JSONObject json = JSONObject.parseObject(retString);
//                    if (json.getString("errorNo").equals("0")) {
//                        logger.error("SZ try ,code : " + code + " retString is ok");
//                    } else {
//                        logger.error("SZ try ,code : " + code + " errorNo isnot 0");
//                    }
//                }
//            } catch (Exception e) {
//                logger.error("SZ try ,code : " + code + " happen exception : e is " + e.toString());
//            }
//            try {
//                String retString = OkHttpUtil.get(urlSH, null);
//                logger.error("SH try ,code : " + code + " begin ~");
//                if (ObjectUtils.isEmpty(retString)) {
//                    logger.error("SH try ,code : " + code + " retString is empty");
//                } else {
//                    JSONObject json = JSONObject.parseObject(retString);
//                    if (json.getString("errorNo").equals("0")) {
//                        if(json.getString("timeLine").length()!=0){
//                            logger.error("SH try ,code : " + code + " timeLine is ok");
//                        }else {
//                            logger.error("SH try ,code : " + code + " timeLine is wrong");
//                        }
//                        logger.error("SH try ,code : " + code + " retString is ok");
//                    } else {
//                        logger.error("SH try ,code : " + code + " errorNo isnot 0");
//                    }
//                }
//            } catch (Exception e) {
//                logger.error("SH try ,code : " + code + " happen exception : e is " + e.toString());
//            }
//            Long iTimeEnd = Calendar.getInstance().getTimeInMillis();
//            logger.info("code " + code + " spend : " + (iTimeEnd - beginTime) / 1000 + "s"
//                    + (iTimeEnd - beginTime) % 1000 + "ms");
//        }
    }
//
//
//    @Test
//    public void getHighOpenTimes() {
//
//        Long beginTime = Calendar.getInstance().getTimeInMillis();
//        List<JSONObject> jsonObjectList =
//                mongoTemplate.findAll(JSONObject.class, "HighOpenDataDetail");
//        Long forend = Calendar.getInstance().getTimeInMillis();
//        logger.info(" spend times is : " + (forend - beginTime) / 1000 + "s"
//                + (forend - beginTime) % 1000 + "ms");
//        Integer i = jsonObjectList.size();
//        i = null;
//    }
//
//    @Test
//    public void getMissDayLine() {
//
//        Long beginTime = Calendar.getInstance().getTimeInMillis();
//        List<codes> codesList = codesMapper.selectAllCodes();
//        for (int i = 0; i < codesList.size(); i++) {
//            Long forbegin = Calendar.getInstance().getTimeInMillis();
//            codes codes = codesList.get(i);
//            List<StockDayLine> stockDayLineList =
//                    mongoTemplate.findAll(StockDayLine.class, "DayLine" + codes.getCode());
//            JSONObject jsonObject = new JSONObject();
//            jsonObject.put("code", codes.getCode());
//            jsonObject.put("name", codes.getName());
//            try {
//                if (stockDayLineList.size() < 100) {
//                    jsonObject.put("number", stockDayLineList.size());
//                    mongoTemplate.save(jsonObject, "0missDayLine");
//                    logger.info("DayLine Check ,find a miss code : " + codes.getCode() + " and its size is " + stockDayLineList.size());
//                }
//            } catch (Exception e) {
//                jsonObject.put("Exception", e.toString());
//                mongoTemplate.save(jsonObject, "0missDayLineFail");
//                logger.info("DayLine Check ,find a Exception code : " + codes.getCode() + " and its Exception is " + e.toString());
//            }
//            Long forend = Calendar.getInstance().getTimeInMillis();
//            logger.info("check dayline, code is : " + codes.getCode() + " spend times is : " + (forend - forbegin) / 1000 + "s"
//                    + (forend - forbegin) % 1000 + "ms");
//        }
//        logger.info("finish dayLine check");
//
//        for (int i = 0; i < codesList.size(); i++) {
//            Long forbegin = Calendar.getInstance().getTimeInMillis();
//            codes codes = codesList.get(i);
//            List<StockMinLine> stockDayLineList =
//                    mongoTemplate.findAll(StockMinLine.class, "MinLine" + codes.getCode());
//            JSONObject jsonObject = new JSONObject();
//            jsonObject.put("code", codes.getCode());
//            jsonObject.put("name", codes.getName());
//            try {
//                if (stockDayLineList.size() < 100) {
//                    jsonObject.put("number", stockDayLineList.size());
//                    mongoTemplate.save(jsonObject, "0missMinLine");
//                    logger.info("MinLine Check ,find a miss code : " + codes.getCode() + " and its size is " + stockDayLineList.size());
//                }
//            } catch (Exception e) {
//                jsonObject.put("Exception", e.toString());
//                mongoTemplate.save(jsonObject, "0missMinLineFail");
//                logger.info("MinLine Check ,find a Exception code : " + codes.getCode() + " and its Exception is " + e.toString());
//            }
//            Long forend = Calendar.getInstance().getTimeInMillis();
//            logger.info("check minLine, code is : " + codes.getCode() + " spend times is : " + (forend - forbegin) / 1000 + "s"
//                    + (forend - forbegin) % 1000 + "ms");
//        }
//        logger.info("finish MinLine check");
//        Long forend = Calendar.getInstance().getTimeInMillis();
//        logger.info(" spend times is : " + (forend - beginTime) / 1000 + "s"
//                + (forend - beginTime) % 1000 + "ms");
//    }
//
//    @Autowired
//    SpiderServiceImpl spiderServiceImpl;
//
//    @Test
//    public void UpdateMissDayLine() {
//        Long beginTime = Calendar.getInstance().getTimeInMillis();
//        List<JSONObject> jsonObjectList = mongoTemplate.findAll(JSONObject.class, "0missDayLine");
//        for (int i = 0; i < jsonObjectList.size(); i++) {
//            Long forbegin = Calendar.getInstance().getTimeInMillis();
//            JSONObject jsonObject = jsonObjectList.get(i);
//            String codeString = jsonObject.getString("code");
//            if (jsonObject.getInteger("number").equals(0)) {
//                logger.info("number is 0 : code : " + jsonObject.getString("code"));
//                spiderServiceImpl.getOneCodes(jsonObject.getString("code"));
////                检查是否ok
//                List<StockDayLine> stockDayLineList =
//                        mongoTemplate.findAll(StockDayLine.class, "DayLine" + jsonObject.getString("code"));
//                JSONObject json = new JSONObject();
//                json.put("code", jsonObject.getString("code"));
//                try {
//                    if (stockDayLineList.size() > 0) {
//                        json.put("number", stockDayLineList.size());
//                        mongoTemplate.save(json, "0UpdateMissDayLine");
//                        Criteria criteria = Criteria.where("code").is(codeString);
//                        mongoTemplate.remove(Query.query(criteria), "0missDayLine");
//                        logger.info("DayLine Check ,find a miss code : " + jsonObject.getString("code") + " and its size is " + stockDayLineList.size());
//                    } else {
//                        json.put("number ", stockDayLineList.size());
//                        mongoTemplate.save(json, "0UpdateMissDayLineFail");
//                        logger.info("DayLine Check ,find a Exception code : " + jsonObject.getString("code") );
//                    }
//                } catch (Exception e) {
//                    json.put("Exception", e.toString());
//                    mongoTemplate.save(json, "0UpdateMissDayLineFail");
//                    logger.info("DayLine Check ,find a Exception code : " + jsonObject.getString("code") + " and its Exception is " + e.toString());
//                }
//            }
//            Long forend = Calendar.getInstance().getTimeInMillis();
//            logger.info("check , code is : " + jsonObject.getString("code") + " spend times is : " + (forend - forbegin) / 1000 + "s"
//                    + (forend - forbegin) % 1000 + "ms");
//        }
//        Long forend = Calendar.getInstance().getTimeInMillis();
//        logger.info("UpdateMissDayLine spend times is : " + (forend - beginTime) / 1000 + "s"
//                + (forend - beginTime) % 1000 + "ms");
//    }
//    @Test
//    public void readAllDayLineData(){
//        Long beginTime = Calendar.getInstance().getTimeInMillis();
//        List<codes> codesList = codesMapper.selectAllCodes();
//        for (codes codes : codesList) {
//
//            Long forBeginTimes = Calendar.getInstance().getTimeInMillis();
//            ArrayList<StockDayLine> stockDayLines = new ArrayList<>(
//                    mongoTemplate.find(Query.query(Criteria.where("dataClass").is("DayLine")),
//                            StockDayLine.class, codes.getCode().toString()));
//            AllCodeAllDayLine allCodeAllDayLine =new AllCodeAllDayLine();
//            allCodeAllDayLine.setId(codes.getCode());
//            allCodeAllDayLine.setCode(codes.getCode());
//            allCodeAllDayLine.setName(codes.getName());
//            allCodeAllDayLine.setDataSum(stockDayLines.size());
//            Collections.sort(stockDayLines, new Comparator<StockDayLine>() {
//                @Override
//                public int compare(StockDayLine o1, StockDayLine o2) {
//                    return (Integer.valueOf(o2.getId()) - Integer.valueOf(o1.getId()));
//                }
//            });
//            allCodeAllDayLine.setStockDayLines(stockDayLines);
//            allCodeAllDayLine.setLastDataString(stockDayLines.get(0).getId());
//            mongoTemplate.save(allCodeAllDayLine,"*AllCodeAllDayLineDB");
//            Long forEndTimes = Calendar.getInstance().getTimeInMillis();
//            logger.info("finish code : " + codes.getId().toString() + " ,and spend time is : " +
//                    (forEndTimes - forBeginTimes) / 1000 + "s" + (forEndTimes - forBeginTimes) % 1000 + "ms"            );
//        }
//        Long endTime = Calendar.getInstance().getTimeInMillis();
//        logger.info("readAllDayLineData all spend time : " + (endTime - beginTime) / 1000 + "s" + (endTime - beginTime) % 1000 + "ms");
//    }
}
