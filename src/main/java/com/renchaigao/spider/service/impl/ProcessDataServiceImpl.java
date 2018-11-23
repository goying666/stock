package com.renchaigao.spider.service.impl;

import com.alibaba.fastjson.JSONObject;
import com.renchaigao.spider.dao.StockDayLine;
import com.renchaigao.spider.dao.codes;
import com.renchaigao.spider.dao.mapper.codesMapper;
import com.renchaigao.spider.data.SpiderUrl;
import com.renchaigao.spider.service.ProcessDataService;
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
                try{
                    if(checkStockIsHighOpen(stockDayLine)){
                        JSONObject jsonObject = new JSONObject();
                        jsonObject.put("id",codes.getCode()+"_"+stockDayLine.getId());
                        jsonObject.put("code",codes.getCode());
                        jsonObject.put("date",stockDayLine.getId());
                        mongoTemplate.save(jsonObject,"HighOpenDataDetail");
                    }
                }catch (Exception e){
                    JSONObject jsonObject = new JSONObject();
                    jsonObject.put("id",codes.getCode());
                    jsonObject.put("code",codes.getCode());
                    jsonObject.put("date",j);
                    jsonObject.put("fail detail : " , e.toString());
                    mongoTemplate.save(jsonObject,"FailHighOpenDataDetail");
                }
            }
            Long forend = Calendar.getInstance().getTimeInMillis();
            logger.info("for code is : " + codes.getCode() + " for spend : " + (forend - forbegin) / 1000 + "s"
                    + (forend - forbegin) % 1000 + "ms");
        }
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
