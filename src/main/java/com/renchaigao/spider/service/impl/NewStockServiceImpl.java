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
import com.renchaigao.spider.service.NewStockService;
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
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.Calendar;
import java.util.List;

@Service
public class NewStockServiceImpl implements NewStockService {

    private static Logger logger = Logger.getLogger(NewStockServiceImpl.class);

    @Autowired
    codesMapper codesMapper;

    @Autowired
    MongoTemplate mongoTemplate;

    @Override
    public ResponseEntity updateEveryDayData(){
        Long updateEveryDayDataBeginTime = Calendar.getInstance().getTimeInMillis();
//        获取已有数据：1、dayLine_day dayLine_code 2、minLine_day minLine_code;

//        获取每日数据：1、dayLine  2、minLine；
//        1、获取dayLine数据，
        SpiderUrl spiderUrl = new SpiderUrl();
        String retString = OkHttpUtil.get(spiderUrl.getAllDataUrl(), null);
        String str = retString.substring(retString.indexOf("(") + 1, retString.length() - 1);
        String todayDate = dateUse.getTodayDate().replace("-", "");
        JSONArray jsonArray = JSONObject.parseObject(str).getJSONArray("data");
        String strUse = null;
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
//        处理每日数据：1、dayLine  2、minLine；

//        存储；

//        检查异常数据
        return null;
    }

    @Override
    public ResponseEntity setUpStockData() {
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

}
