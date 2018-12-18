package com.renchaigao.spider.service.impl;

import com.renchaigao.spider.dao.StockDayLine;
import com.renchaigao.spider.dao.codes;
import com.renchaigao.spider.dao.mapper.codesMapper;
import com.renchaigao.spider.service.StockGuessService;
import com.renchaigao.zujuba.domain.response.ResponseEntity;
import org.apache.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.mongodb.core.MongoTemplate;
import org.springframework.data.mongodb.core.query.Criteria;
import org.springframework.data.mongodb.core.query.Query;
import org.springframework.stereotype.Service;

import java.util.*;

@Service
public class StockGuessServiceImpl implements StockGuessService {
    private static Logger logger = Logger.getLogger(StockGuessServiceImpl.class);

    @Autowired
    MongoTemplate mongoTemplate;

    @Autowired
    codesMapper codesMapper;

    @Override
    public ResponseEntity GuessStock(String code) {
        Long beginTime = Calendar.getInstance().getTimeInMillis();
        List<codes> codesList = codesMapper.selectAllCodes();
//        获取对应股票近4天的交易数据
        List<StockDayLine> stockDayLines = new ArrayList<>(
                mongoTemplate.find(Query.query(Criteria.where("dataClass").is("DayLine")),
                        StockDayLine.class, code));
        Collections.sort(stockDayLines, new Comparator<StockDayLine>() {
            @Override
            public int compare(StockDayLine o1, StockDayLine o2) {
                return (Integer.valueOf(o2.getId()) - Integer.valueOf(o1.getId()));
            }
        });
        if (stockDayLines.size() >= 3) {
            try {
                int j = 1;
                StockDayLine twoDayLine = stockDayLines.get(j - 1);
                StockDayLine threeDayLine = stockDayLines.get(j);
            } catch (Exception e) {

            }
        }
//        梳理对应的约束条件
//        轮训历史其他股票进行约束匹配

//        获取所有股票的编码
//        匹配的股票记录code 和 date
//        统计数据
        return null;
    }

    @Override
    public ResponseEntity GuessStock_One() {
        Long beginTime = Calendar.getInstance().getTimeInMillis();
        List<codes> codesList = codesMapper.selectAllCodes();
        for (codes codes : codesList) {
            //        获取对应股票近5天的交易数据
            ArrayList<StockDayLine> stockDayLines = new ArrayList<>(
                    mongoTemplate.find(Query.query(Criteria.where("dataClass").is("DayLine")),
                            StockDayLine.class, codes.getId().toString()));

            Collections.sort(stockDayLines, new Comparator<StockDayLine>() {
                @Override
                public int compare(StockDayLine o1, StockDayLine o2) {
                    return (Integer.valueOf(o2.getId()) - Integer.valueOf(o1.getId()));
                }
            });
            if (stockDayLines.size() >= 4) {
                try {
                    for (int j = 4; j < stockDayLines.size(); j++) {
                        StockDayLine oneDayLine = stockDayLines.get(j - 4);
                        StockDayLine twoDayLine = stockDayLines.get(j - 3);
                        StockDayLine threeDayLine = stockDayLines.get(j - 2);
                        StockDayLine fourDayLine = stockDayLines.get(j - 1);
                        StockDayLine fiveLine = stockDayLines.get(j);
                        if (oneDayLine.getOpen() > oneDayLine.getPreClose() &&
                                twoDayLine.getOpen() > twoDayLine.getClose()&&
                                twoDayLine.getClose()/twoDayLine.getOpen()>1.005&&
                                twoDayLine.getClose()/twoDayLine.getOpen()<1.006


                                ) {

                        }
                    }
                } catch (Exception e) {

                }
            }
        }

        return null;
    }


}
