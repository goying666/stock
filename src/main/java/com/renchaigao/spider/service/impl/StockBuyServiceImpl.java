package com.renchaigao.spider.service.impl;

import com.alibaba.fastjson.JSONObject;
import com.renchaigao.spider.dao.StockDayLine;
import com.renchaigao.spider.dao.codes;
import com.renchaigao.spider.dao.mapper.codesMapper;
import com.renchaigao.spider.data.StockDBName;
import com.renchaigao.spider.service.StockBuyService;
import com.renchaigao.zujuba.domain.response.ResponseEntity;
import normal.dateUse;
import org.apache.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.mongodb.core.MongoTemplate;
import org.springframework.data.mongodb.core.query.Criteria;
import org.springframework.data.mongodb.core.query.Query;
import org.springframework.data.mongodb.core.query.Update;
import org.springframework.stereotype.Service;

import java.util.*;

@Service
public class StockBuyServiceImpl implements StockBuyService {

    private static Logger logger = Logger.getLogger(StockBuyServiceImpl.class);

    @Autowired
    MongoTemplate mongoTemplate;

    @Autowired
    codesMapper codesMapper;

    @Override
    public ResponseEntity GuessTwoBuyInit() {
        return null;
    }

    @Override
    public ResponseEntity InitBuy() {
//        获取所有股票的编码
        Long beginTime = Calendar.getInstance().getTimeInMillis();
        List<codes> codesList = codesMapper.selectAllCodes();
//        轮训处理
        String todayString = dateUse.getTodayDate();
        JSONObject json = new JSONObject();
        json.put("_id", "0");
        json.put("id", "0");
        json.put("date", todayString);
        mongoTemplate.save(json, StockDBName.STOCK_BUY_PRE_DB_NAME);
        for (codes codes : codesList) {
            String code = codes.getCode();
            Long forbegin = Calendar.getInstance().getTimeInMillis();
            List<StockDayLine> stockDayLines = new ArrayList<>(
                    mongoTemplate.find(Query.query(Criteria.where("dataClass").is("DayLine")),
                            StockDayLine.class, code));
            Collections.sort(stockDayLines, new Comparator<StockDayLine>() {
                @Override
                public int compare(StockDayLine o1, StockDayLine o2) {
                    return (Integer.valueOf(o2.getId()) - Integer.valueOf(o1.getId()));
                }
            });
//            guessTwo part
            GuessTwoPart(todayString, code, stockDayLines);
            Long forend = Calendar.getInstance().getTimeInMillis();
            logger.info("code is : " + code + " and spend time is : " +
                    (forend - forbegin) / 1000 + "s" + (forend - forbegin) % 1000 + "ms");
        }
//        满足上诉几点的股票，输出股票代码和日期（当日），判断是否为高开，以及高开比率，高开幅度；
        Long allend = Calendar.getInstance().getTimeInMillis();
        logger.info(" all spend time is : " + (allend - beginTime) / 1000 + "s" + (allend - beginTime) % 1000 + "ms");
        return null;
    }

    private void GuessTwoPart(String todayString, String code, List<StockDayLine> stockDayLines) {
        Integer GuessTwoEVN = 0;
        if (stockDayLines.size() >= 2) {
            try {
                int j = 1;
                StockDayLine twoDayLine = stockDayLines.get(j - 1);
                StockDayLine threeDayLine = stockDayLines.get(j);
//                    float a = yesterDayLine.getClose() / yesterDayLine.getOpen();
                float b = twoDayLine.getOpen() / twoDayLine.getClose();
//                    float c = (float) yesterDayLine.getVolume() / twoDayLine.getVolume();
                float d = (float) twoDayLine.getVolume() / threeDayLine.getVolume();
                if (
//                            a > 1.025
//                            && twoDayLine.getOpen() > yesterDayLine.getHigh()
//                            && twoDayLine.getClose() < yesterDayLine.getLow()&&
                        b > 1.05
                                && threeDayLine.getOpen() > threeDayLine.getClose()
                                && threeDayLine.getClose() < twoDayLine.getOpen()
//                            && c > 1.15
                                && d > 1.3) {
                    JSONObject jsonObject = new JSONObject();
                    jsonObject.put("code", code);
                    jsonObject.put("date", todayString);
                    jsonObject.put("env", "yes");
                    jsonObject.put("guess", "two");
                    GuessTwoEVN++;
                    mongoTemplate.save(jsonObject, StockDBName.GUESS_TWO_DB_NAME);
                    mongoTemplate.updateFirst(Query.query(Criteria.where("id").is("0")),
                            Update.update("GuessTwoEVN", GuessTwoEVN.toString()), JSONObject.class,
                            StockDBName.STOCK_BUY_PRE_DB_NAME);
                }
            } catch (Exception e) {
                JSONObject jsonObject = new JSONObject();
                jsonObject.put("code", code);
                jsonObject.put("Exception", e.toString());
            }
        }
    }

}
