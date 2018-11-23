package com.renchaigao.spider;

import com.alibaba.fastjson.JSONObject;
import com.renchaigao.spider.dao.StockDayLine;
import com.renchaigao.spider.dao.StockMinLine;
import com.renchaigao.spider.dao.codes;
import com.renchaigao.spider.dao.mapper.codesMapper;
import com.renchaigao.spider.service.impl.ProcessDataServiceImpl;
import com.renchaigao.spider.service.impl.SpiderServiceImpl;
import com.renchaigao.spider.util.StockPriceProcess;
import javafx.scene.shape.Circle;
import org.apache.log4j.Logger;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.data.mongodb.core.MongoTemplate;
import org.springframework.data.mongodb.core.query.Criteria;
import org.springframework.data.mongodb.core.query.Query;
import org.springframework.test.context.junit4.SpringRunner;

import java.util.Calendar;
import java.util.List;

import static com.renchaigao.spider.util.StockPriceProcess.FloatValueProcess;

@RunWith(SpringRunner.class)
@SpringBootTest
public class SpiderApplicationTests {

    private static Logger logger = Logger.getLogger(SpiderApplicationTests.class);

    @Autowired

    codesMapper codesMapper;
    @Test
    public void contextLoads() {
//        Float a = StockPriceProcess.FloatValueProcess(Float.valueOf((float) 3.119));
//        System.out.println(a);
//        a = StockPriceProcess.FloatValueProcess(Float.valueOf((float) 3.111));
//        System.out.println(a);
//        a = StockPriceProcess.FloatValueProcess(Float.valueOf((float) -3.119));
//        System.out.println(a);
//        a = StockPriceProcess.FloatValueProcess(Float.valueOf((float) -3.112));
//        System.out.println(a);
//        List<codes> codesList = codesMapper.selectAllCodes();
//        codesList = null;
    }
//
//    @Autowired
//    MongoTemplate mongoTemplate;
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

}
