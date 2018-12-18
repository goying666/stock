package com.renchaigao.spider.service.impl;

import com.alibaba.fastjson.JSONObject;
import com.renchaigao.spider.dao.StockDayLine;
import com.renchaigao.spider.dao.codes;
import com.renchaigao.spider.dao.mapper.codesMapper;
import com.renchaigao.spider.data.SpiderUrl;
import com.renchaigao.spider.data.StockDBName;
import com.renchaigao.spider.service.ProcessDataService;
import com.renchaigao.spider.util.DateUtil;
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

import java.util.ArrayList;
import java.util.Calendar;
import java.util.List;

@Service
public class ProcessDataServiceImpl implements ProcessDataService {

    private static Logger logger = Logger.getLogger(ProcessDataServiceImpl.class);

    @Autowired
    codesMapper codesMapper;

    @Autowired
    MongoTemplate mongoTemplate;

    @Override
    public ResponseEntity InitDayLineStockData() {
        Long beginTime = Calendar.getInstance().getTimeInMillis();

        SpiderUrl spiderUrl = new SpiderUrl();
        List<codes> codesList = codesMapper.selectAllCodes();
//        给每只股票的日线数据的每日增加型号；1~12类型号；
//        for (int i = 0; i < 30; i++) {
//            codes codes = codesList.get(i);
        for (codes codes : codesList) {
            Long forbegin = Calendar.getInstance().getTimeInMillis();
            ArrayList<StockDayLine> stockDayLines = new ArrayList<>(
                    mongoTemplate.findAll(StockDayLine.class, "DayLine" + codes.getCode()));
            for (int j = 0; j < stockDayLines.size(); j++) {
                StockDayLine stockDayLine = stockDayLines.get(j);
//            for (StockDayLine stockDayLine:stockDayLines){
                Criteria criteria = Criteria.where("id").is(stockDayLine.getId());
                try {
                    Update update = Update.update("stockClass", getStockClass(stockDayLine).getStockClass());
                    mongoTemplate.upsert(Query.query(criteria), update, StockDayLine.class, "DayLine" + codes.getCode());
                } catch (Exception e) {
                    JSONObject jsonUse = new JSONObject();
                    jsonUse.put("id", codes.getCode());
                    jsonUse.put("stockDataID", j);
                    jsonUse.put("fail", "InitDayLineStockData Exception");
                    jsonUse.put("detail", e.toString());
                    jsonUse.put("date", dateUse.getTodayDate());
                    jsonUse.put("time", dateUse.GetStringDateNow());
                    mongoTemplate.save(jsonUse, "InitDayLineStockDataFailData");
                    logger.error("code : " + codes.getCode() + " id is : " + j);
                    logger.error("Exception e : " + e);
                }
            }
            Long forend = Calendar.getInstance().getTimeInMillis();
            logger.info("for code is : " + codes.getCode() + "\n for spend : " + (forend - forbegin) / 1000 + "s"
                    + (forend - forbegin) % 1000 + "ms");
        }
        Long endTime = Calendar.getInstance().getTimeInMillis();
        logger.info("InitDayLineStockData spend : " + (endTime - beginTime) / 1000 + "s"
                + (endTime - beginTime) % 1000 + "ms");
        return null;
    }

    private StockDayLine getStockClass(StockDayLine stockDayLine) {
        if (stockDayLine.getClose() > stockDayLine.getOpen()) {
            if (stockDayLine.getHigh() > stockDayLine.getClose()) {
                if (stockDayLine.getLow() < stockDayLine.getOpen()) {
                    stockDayLine.setStockClass("4");
                } else {
                    stockDayLine.setStockClass("2");
                }
            } else {
                if (stockDayLine.getLow() < stockDayLine.getOpen()) {
                    stockDayLine.setStockClass("3");
                } else {
                    stockDayLine.setStockClass("1");
                }
            }
        } else if (stockDayLine.getClose() < stockDayLine.getOpen()) {
            if (stockDayLine.getHigh() > stockDayLine.getOpen()) {
                if (stockDayLine.getLow() < stockDayLine.getClose()) {
                    stockDayLine.setStockClass("8");
                } else {
                    stockDayLine.setStockClass("6");
                }
            } else {
                if (stockDayLine.getLow() < stockDayLine.getClose()) {
                    stockDayLine.setStockClass("7");
                } else {
                    stockDayLine.setStockClass("5");
                }
            }
        } else {
            if (stockDayLine.getHigh() > stockDayLine.getOpen()) {
                if (stockDayLine.getLow() < stockDayLine.getOpen()) {
                    stockDayLine.setStockClass("12");
                } else {
                    stockDayLine.setStockClass("10");
                }
            } else {
                if (stockDayLine.getLow() < stockDayLine.getOpen()) {
                    stockDayLine.setStockClass("11");
                } else {
                    stockDayLine.setStockClass("9");
                }
            }
        }
        return stockDayLine;
    }

    @Override
    public ResponseEntity GetHighOpenData(String beginDate) {
        Long beginTime = Calendar.getInstance().getTimeInMillis();
        List<codes> codesList = codesMapper.selectAllCodes();
        for (codes codes : codesList) {
            Long forbegin = Calendar.getInstance().getTimeInMillis();
            ArrayList<StockDayLine> stockDayLines = new ArrayList<>(
                    mongoTemplate.findAll(StockDayLine.class, "DayLine" + codes.getCode()));
            for (int j = 0; j < stockDayLines.size(); j++) {
                StockDayLine stockDayLine = stockDayLines.get(j);
                try {
                    if (checkStockIsHighOpen(stockDayLine)) {
                        JSONObject jsonObject = new JSONObject();
                        jsonObject.put("id", codes.getCode() + "_" + stockDayLine.getId());
                        jsonObject.put("code", codes.getCode());
                        jsonObject.put("date", stockDayLine.getId());
                        mongoTemplate.save(jsonObject, "HighOpenDataDetail");
                    }
                } catch (Exception e) {
                    JSONObject jsonObject = new JSONObject();
                    jsonObject.put("id", codes.getCode());
                    jsonObject.put("code", codes.getCode());
                    jsonObject.put("date", j);
                    jsonObject.put("fail detail : ", e.toString());
                    mongoTemplate.save(jsonObject, "FailHighOpenDataDetail");
                }
            }
            Long forend = Calendar.getInstance().getTimeInMillis();
            logger.info("for code is : " + codes.getCode() + " for spend : " + (forend - forbegin) / 1000 + "s"
                    + (forend - forbegin) % 1000 + "ms");
        }
        return null;
    }

    @Override
    public ResponseEntity GuessOne() {
//        获取所有股票的编码
        Long beginTime = Calendar.getInstance().getTimeInMillis();
        List<codes> codesList = codesMapper.selectAllCodes();
//        轮训处理
        JSONObject json = new JSONObject();
        json.put("_id", "0");
        json.put("id", "0");
        json.put("allYesEvn", 0);
        json.put("allHighTimes", 0);
        mongoTemplate.save(json, StockDBName.GUESS_ONE_DB_NAME);
        Integer allYesEvn = 0, allHighTimes = 0;
        for (codes codes : codesList) {
            String code = codes.getCode();
            Long forbegin = Calendar.getInstance().getTimeInMillis();
            ArrayList<StockDayLine> stockDayLines = new ArrayList<>(
                    mongoTemplate.find(Query.query(Criteria.where("dataClass").is("DayLine")), StockDayLine.class, code));
//        获取第一日数据，判断是否有前一日数据和前两日数据，
            if (stockDayLines.size() >= 3) {
                try {
//        具备前两日数据的数据进行判断
                    for (int j = 2; j < stockDayLines.size(); j++) {
                        StockDayLine currentDayLine = stockDayLines.get(j - 2);
                        StockDayLine yesterDayLine = stockDayLines.get(j - 1);
                        StockDayLine threeDayLine = stockDayLines.get(j);
//        1、判断前一日是否为：收盘 大于 开盘 2%
                        Float a = (yesterDayLine.getClose() / yesterDayLine.getOpen()),
                                b = threeDayLine.getOpen() / yesterDayLine.getClose(),
                                c = (float) (yesterDayLine.getVolume() / yesterDayLine.getVolume());

                        if (a > 1.02
                                && (yesterDayLine.getClose() > yesterDayLine.getOpen())
                                && (!yesterDayLine.getHigh().equals(yesterDayLine.getClose()))
                                && (!yesterDayLine.getLow().equals(yesterDayLine.getOpen()))
                                && (c < 1.5)
                                && (c > 0.5)) {
//                            判断时间是否有过长的间隔3天以上剔除
//        2、前两日 开盘价格 大于 前一日收盘价格 1.5%
                            if (b > 1.015
                                    && (threeDayLine.getOpen() > yesterDayLine.getClose())
                                    && (threeDayLine.getClose() > yesterDayLine.getClose())
                                    && (DateUtil.CompareTwoDays(yesterDayLine.getId(), threeDayLine.getId()) < 3)
                                    && (DateUtil.CompareTwoDays(currentDayLine.getId(), yesterDayLine.getId()) < 3)) {
                                JSONObject jsonObject = new JSONObject();
                                jsonObject.put("code", code);
                                jsonObject.put("date", currentDayLine.getId());
                                jsonObject.put("env", "yes");
                                allYesEvn++;
                                if (currentDayLine.getOpen() > currentDayLine.getPreClose()) {
                                    jsonObject.put("highOpen", "yes");
                                    jsonObject.put("highPre", currentDayLine.getOpen() % currentDayLine.getPreClose());
                                    allHighTimes++;
                                } else {
                                    jsonObject.put("highOpen", "no");
                                }
                                mongoTemplate.save(jsonObject, StockDBName.GUESS_ONE_DB_NAME);
                            }
                        }
                    }
                } catch (Exception e) {
                    JSONObject jsonObject = new JSONObject();
                    jsonObject.put("code", code);
                    jsonObject.put("Exception", e.toString());
                }
            }
            Long forend = Calendar.getInstance().getTimeInMillis();
            logger.info("code is : " + code + " and spend time is : " +
                    (forend - forbegin) / 1000 + "s" +
                    (forend - forbegin) % 1000 + "ms");
            mongoTemplate.updateFirst(Query.query(Criteria.where("id").is(0)),
                    Update.update("allYesEvn", allYesEvn).addToSet("allHighTimes", allHighTimes), "*GuessOne");
        }
//        满足上诉几点的股票，输出股票代码和日期（当日），判断是否为高开，以及高开比率，高开幅度；
        Long allend = Calendar.getInstance().getTimeInMillis();
        logger.info(" all spend time is : " + (allend - beginTime) / 1000 + "s" + (allend - beginTime) % 1000 + "ms");
        return null;
    }

    @Override
    public ResponseEntity GuessTwo(String codeInit) {

//        获取所有股票的编码
        Long beginTime = Calendar.getInstance().getTimeInMillis();
        List<codes> codesList = codesMapper.selectAllCodes();
//        轮训处理
        JSONObject json = new JSONObject();
        json.put("_id", "0");
        json.put("id", "0");
        json.put("allYesEvn", 0);
        json.put("allHighTimes", 0);
        mongoTemplate.save(json, StockDBName.GUESS_TWO_DB_NAME);
        Integer allYesEvn = 0, allHighTimes = 0,noHighTimes = 0;
        for (codes codes : codesList) {
            String code = codes.getCode();
            if(!codeInit.equals("0"))
                code = codeInit;
            Long forbegin = Calendar.getInstance().getTimeInMillis();
            ArrayList<StockDayLine> stockDayLines = new ArrayList<>(
                    mongoTemplate.find(Query.query(Criteria.where("dataClass").is("DayLine")), StockDayLine.class, code));
//        获取第一日数据，判断是否有前一日数据和前两日数据，
            if (stockDayLines.size() >= 4) {
                try {
//        具备前两日数据的数据进行判断
                    for (int j =3; j < stockDayLines.size(); j++) {
                        StockDayLine currentDayLine = stockDayLines.get(j - 3);
                        StockDayLine yesterDayLine = stockDayLines.get(j - 2);
                        StockDayLine twoDayLine = stockDayLines.get(j - 1);
                        StockDayLine threeDayLine = stockDayLines.get(j);
                        float a= yesterDayLine.getClose()/yesterDayLine.getOpen();
                        float b= twoDayLine.getOpen()/twoDayLine.getClose();
                        float c= (float)yesterDayLine.getVolume()/twoDayLine.getVolume();
                        float d= (float)twoDayLine.getVolume()/threeDayLine.getVolume();
                        if (    a>1.025
                                && twoDayLine.getOpen()>yesterDayLine.getHigh()
                                && twoDayLine.getClose()<yesterDayLine.getLow()
                                && b>1.05
                                && threeDayLine.getOpen()>threeDayLine.getClose()
                                && threeDayLine.getClose()<twoDayLine.getOpen()
                                && c>1.15
                                && d>1.3) {
                            JSONObject jsonObject = new JSONObject();
                            jsonObject.put("code", code);
                            jsonObject.put("date", currentDayLine.getId());
                            jsonObject.put("env", "yes");
                            allYesEvn++;
                            if (currentDayLine.getOpen() > currentDayLine.getPreClose()) {
                                jsonObject.put("highOpen", "yes");
                                jsonObject.put("highPre", (currentDayLine.getOpen() / currentDayLine.getPreClose() - 1) * 100 + "%");
                                allHighTimes++;
                            } else {
                                jsonObject.put("highOpen", "no");
                                noHighTimes++;
                            }
                            mongoTemplate.save(jsonObject, StockDBName.GUESS_TWO_DB_NAME);
                            mongoTemplate.updateFirst(Query.query(Criteria.where("id").is("0")),
                                    Update.update("allYesEvn", allYesEvn.toString()),JSONObject.class,StockDBName.GUESS_TWO_DB_NAME);
                            mongoTemplate.updateFirst(Query.query(Criteria.where("id").is("0")),
                                    Update.update("noHighTimes", noHighTimes.toString()),JSONObject.class,StockDBName.GUESS_TWO_DB_NAME);
                            mongoTemplate.updateFirst(Query.query(Criteria.where("id").is("0")),
                                    Update.update("allHighTimes", allHighTimes.toString()),JSONObject.class,StockDBName.GUESS_TWO_DB_NAME);
                            String rightPre = (allHighTimes/allYesEvn)*100 + "%";
                            mongoTemplate.updateFirst(Query.query(Criteria.where("id").is("0")),
                                    Update.update("rightPre", rightPre),JSONObject.class,StockDBName.GUESS_TWO_DB_NAME);

//                            mongoTemplate.updateFirst(Query.query(Criteria.where("id").is("0")),
//                                    Update.update("allYesEvn", allYesEvn.toString()).addToSet("allHighTimes", allHighTimes.toString())
//                                    .addToSet("noHighTimes",noHighTimes.toString()),JSONObject.class,StockDBName.GUESS_TWO_DB_NAME);
                        }
                    }
                } catch (Exception e) {
                    JSONObject jsonObject = new JSONObject();
                    jsonObject.put("code", code);
                    jsonObject.put("Exception", e.toString());
                }
            }
            Long forend = Calendar.getInstance().getTimeInMillis();
            logger.info("code is : " + code + " and spend time is : " +
                    (forend - forbegin) / 1000 + "s" +
                    (forend - forbegin) % 1000 + "ms");
        }
//        满足上诉几点的股票，输出股票代码和日期（当日），判断是否为高开，以及高开比率，高开幅度；
        Long allend = Calendar.getInstance().getTimeInMillis();
        logger.info(" all spend time is : " + (allend - beginTime) / 1000 + "s" + (allend - beginTime) % 1000 + "ms");
        return null;
    }

    @Override
    public ResponseEntity GuessThree(String codeInit) {

//        获取所有股票的编码
        Long beginTime = Calendar.getInstance().getTimeInMillis();
        List<codes> codesList = codesMapper.selectAllCodes();
//        轮训处理
        JSONObject json = new JSONObject();
        json.put("_id", "0");
        json.put("id", "0");
        json.put("allYesEvn", 0);
        json.put("allHighTimes", 0);
        mongoTemplate.save(json, StockDBName.GUESS_THREE_DB_NAME);
        Integer allYesEvn = 0, allHighTimes = 0,noHighTimes = 0;
        for (codes codes : codesList) {
            String code = codes.getCode();
            if(!codeInit.equals("0"))
                code = codeInit;
            Long forbegin = Calendar.getInstance().getTimeInMillis();
            ArrayList<StockDayLine> stockDayLines = new ArrayList<>(
                    mongoTemplate.find(Query.query(Criteria.where("dataClass").is("DayLine")),
                            StockDayLine.class, code));
//        获取第一日数据，判断是否有前一日数据和前两日数据，
            if (stockDayLines.size() >= 4) {
                try {
//        具备前两日数据的数据进行判断
                    for (int j =3; j < stockDayLines.size(); j++) {
                        StockDayLine currentDayLine = stockDayLines.get(j - 3);
                        StockDayLine yesterDayLine = stockDayLines.get(j - 2);
                        StockDayLine twoDayLine = stockDayLines.get(j - 1);
                        StockDayLine threeDayLine = stockDayLines.get(j);
                        float a= yesterDayLine.getClose()/yesterDayLine.getOpen();
                        float b= twoDayLine.getOpen()/twoDayLine.getClose();
                        float e= threeDayLine.getOpen()/threeDayLine.getClose();
                        float c= (float)yesterDayLine.getVolume()/twoDayLine.getVolume();
                        float d= (float)currentDayLine.getVolume()/threeDayLine.getVolume();
                        if (       a<0.986
                                && b>1.05
                                && e>1.009
                                && yesterDayLine.getClose()>currentDayLine.getOpen()
                                && threeDayLine.getClose()>twoDayLine.getOpen()
                                && c>1.8
                                && d>1.4) {
                            JSONObject jsonObject = new JSONObject();
                            jsonObject.put("code", code);
                            jsonObject.put("date", currentDayLine.getId());
                            jsonObject.put("env", "yes");
                            allYesEvn++;

                            mongoTemplate.save(jsonObject, StockDBName.GUESS_THREE_DB_NAME);
                            mongoTemplate.updateFirst(Query.query(Criteria.where("id").is("0")),
                                    Update.update("allYesEvn", allYesEvn.toString()),JSONObject.class,StockDBName.GUESS_THREE_DB_NAME);
                            mongoTemplate.updateFirst(Query.query(Criteria.where("id").is("0")),
                                    Update.update("noHighTimes", noHighTimes.toString()),JSONObject.class,StockDBName.GUESS_THREE_DB_NAME);
                            mongoTemplate.updateFirst(Query.query(Criteria.where("id").is("0")),
                                    Update.update("allHighTimes", allHighTimes.toString()),JSONObject.class,StockDBName.GUESS_THREE_DB_NAME);
                            String rightPre = (allHighTimes/allYesEvn)*100 + "%";
                            mongoTemplate.updateFirst(Query.query(Criteria.where("id").is("0")),
                                    Update.update("rightPre", rightPre),JSONObject.class,StockDBName.GUESS_THREE_DB_NAME);
                        }
                    }
                } catch (Exception e) {
                    JSONObject jsonObject = new JSONObject();
                    jsonObject.put("code", code);
                    jsonObject.put("Exception", e.toString());
                }
            }
            Long forend = Calendar.getInstance().getTimeInMillis();
            logger.info("code is : " + code + " and spend time is : " +
                    (forend - forbegin) / 1000 + "s" +
                    (forend - forbegin) % 1000 + "ms");
        }
//        满足上诉几点的股票，输出股票代码和日期（当日），判断是否为高开，以及高开比率，高开幅度；
        Long allend = Calendar.getInstance().getTimeInMillis();
        logger.info(" all spend time is : " + (allend - beginTime) / 1000 + "s" + (allend - beginTime) % 1000 + "ms");
        return null;
    }

    private Boolean checkStockIsHighOpen(StockDayLine stockDayLine) {
        try {
            if (stockDayLine.getOpen() > stockDayLine.getPreClose()) {
                return true;
            } else return false;
        } catch (Exception e) {
            return false;
        }
    }


}
