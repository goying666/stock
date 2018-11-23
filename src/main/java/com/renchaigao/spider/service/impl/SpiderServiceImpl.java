package com.renchaigao.spider.service.impl;

import com.alibaba.fastjson.JSONArray;
import com.alibaba.fastjson.JSONObject;
import com.renchaigao.spider.dao.StockDayLine;
import com.renchaigao.spider.dao.codes;
import com.renchaigao.spider.dao.mapper.codesMapper;
import com.renchaigao.spider.dao.oneDayCode;
import com.renchaigao.spider.data.SpiderUrl;
import com.renchaigao.spider.service.SpiderService;
import com.renchaigao.spider.util.ObjectUtils;
import com.renchaigao.spider.util.OkHttpUtil;
import com.renchaigao.zujuba.domain.response.RespCode;
import com.renchaigao.zujuba.domain.response.ResponseEntity;
import normal.dateUse;
import org.apache.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.mongodb.core.MongoTemplate;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.Calendar;

@Service
public class SpiderServiceImpl implements SpiderService {

    private static Logger logger = Logger.getLogger(SpiderServiceImpl.class);

    @Autowired
    MongoTemplate mongoTemplate;

    @Autowired
    codesMapper codesMapper;

    @Override
    public ResponseEntity getAllCodes() {
        Long beginTime = Calendar.getInstance().getTimeInMillis();
        System.out.println("begin");
        SpiderUrl spiderUrl = new SpiderUrl();
        String retString = OkHttpUtil.get(spiderUrl.getAllDataUrl(), null);
        Long testTime1 = Calendar.getInstance().getTimeInMillis();
        System.out.println((testTime1 - beginTime) / 1000);
        String str = retString.substring(retString.indexOf("(") + 1, retString.length() - 1);
        JSONObject json = JSONObject.parseObject(str);
        JSONArray jsonArray = json.getJSONArray("data");
        codes codes = new codes();
        String strUse = null;
        for (int i = 0; i < jsonArray.size(); i++) {
            strUse = jsonArray.get(i).toString();
            String[] infoStr = strUse.split(",");
            codes.setCode(infoStr[1]);
            codes.setName(infoStr[2]);
            codesMapper.insert(codes);
        }
        Long testTime2 = Calendar.getInstance().getTimeInMillis();
        System.out.println((testTime2 - testTime1) / 1000);
        System.out.println("end");

//        String[] strAray = json.get("data");
//
//        for (i:jsonArray){
//
//        }
        return new ResponseEntity(RespCode.SUCCESS, str);
    }

    @Override
    public ResponseEntity saveTodayAllCodes() {
        Long beginTime = Calendar.getInstance().getTimeInMillis();
        System.out.println("begin");
        SpiderUrl spiderUrl = new SpiderUrl();
        String retString = OkHttpUtil.get(spiderUrl.getAllDataUrl(), null);
        Long testTime1 = Calendar.getInstance().getTimeInMillis();
        System.out.println((testTime1 - beginTime) / 1000);
        String str = retString.substring(retString.indexOf("(") + 1, retString.length() - 1);
        JSONObject json = JSONObject.parseObject(str);
        JSONArray jsonArray = json.getJSONArray("data");
        oneDayCode oneDayCode = new oneDayCode();
        String strUse = null;
        for (int i = 0; i < jsonArray.size(); i++) {
            strUse = jsonArray.get(i).toString();
            String[] infoStr = strUse.split(",");
//            oneDayCode.setId(dateUse.getTodayDate());
            oneDayCode.setId(String.valueOf(i));
            oneDayCode.setToday(dateUse.getTodayDate());
            oneDayCode.setCode(infoStr[1]);
            oneDayCode.setName(infoStr[2]);
            oneDayCode.setCloseSorting(infoStr[4]);
            oneDayCode.setChangePercent(infoStr[5]);
            oneDayCode.setChangeSorting(infoStr[6]);
            oneDayCode.setVolumeSorting(infoStr[7]);
            oneDayCode.setAmountSorting(infoStr[8]);
            oneDayCode.setAmplitudeSorting(infoStr[9]);
            oneDayCode.setHighSorting(infoStr[10]);
            oneDayCode.setLowSorting(infoStr[11]);
            oneDayCode.setOpenSorting(infoStr[12]);
            oneDayCode.setPreviousCloseSorting(infoStr[13]);
            oneDayCode.setVolumeRateSorting(infoStr[14]);
            oneDayCode.setTurnoverRateSorting(infoStr[15]);
            oneDayCode.setPERationSorting(infoStr[16]);
            oneDayCode.setPBSorting(infoStr[17]);
            mongoTemplate.save(oneDayCode, dateUse.getTodayDate());
            System.out.println(i);
        }
        Long testTime2 = Calendar.getInstance().getTimeInMillis();
        System.out.println((testTime2 - testTime1) / 1000);
        System.out.println("end");

//        String[] strAray = json.get("data");
//
//        for (i:jsonArray){
//
//        }
        return new ResponseEntity(RespCode.SUCCESS, str);
    }

    @Override
    public ResponseEntity getOneCodes(String code) {
        logger.info("GetOneCodes : " + "Curren code is : " + code + " , start !");
        Long beginTime = Calendar.getInstance().getTimeInMillis();
        String allUrl = new SpiderUrl().getDayTimeLineUrl(code);
        Long testTime1 = Calendar.getInstance().getTimeInMillis();
        JSONObject retJson = JSONObject.parseObject(OkHttpUtil.get(allUrl, null));
        try {
            if (Integer.valueOf(retJson.get("errorNo").toString()).equals(0)&&ObjectUtils.isNotEmpty(retJson.getString("mashData"))) {
                logger.info("okhttp spend time : " + (testTime1 - beginTime) / 1000 + "s" + (testTime1 - beginTime) % 1000 + "ms");
                System.out.println((testTime1 - beginTime) / 1000);
                JSONArray jsonArray = retJson.getJSONArray("mashData");
                StockDayLine stockDayLine = new StockDayLine();
                for (int i = 0; i < jsonArray.size(); i++) {
                    JSONObject json = JSONObject.parseObject(jsonArray.get(i).toString());
                    stockDayLine.setId(json.get("date").toString());
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
                    mongoTemplate.save(stockDayLine, "DayLine" + code);
                }
            } else {
                logger.info("retJson is : " + JSONObject.toJSONString(retJson));
                JSONObject jsonUse = new JSONObject();
                jsonUse.put("id", code);
                jsonUse.put("fail", 1);
                jsonUse.put("date", dateUse.getTodayDate());
                mongoTemplate.save(jsonUse, "failGetOnCodesInformation");
                return new ResponseEntity(RespCode.WRONGIP, retJson);
            }
        } catch (Exception e) {
            logger.info("retJson is : " + JSONObject.toJSONString(retJson));
            logger.info("Exception e is : " + e);
        }

        Long testTime2 = Calendar.getInstance().getTimeInMillis();
        logger.info("mongodb save spend time : " + (testTime2 - testTime1) / 1000 + "s" + (testTime2 - testTime1) % 1000 + "ms");
        logger.info("GetOneCodes : " + "Curren code is : " + code + " , end !");
        return new ResponseEntity(RespCode.SUCCESS, retJson);
    }


    @Override
    public ResponseEntity getAllCodesDayLine() {
        Long beginTime = Calendar.getInstance().getTimeInMillis();
        System.out.println("getAllCodesDayLine begin");
        logger.info("getAllCodesDayLine begin");
//        获取所有股票编码；
        ArrayList<codes> codesArrayList = new ArrayList<>();
        for (int i = 1; i <= 3577; i++) {
            codesArrayList.add(codesMapper.selectByPrimaryKey(i));
        }
//        轮训将所有股票的day信息抓好；
        for (codes codes : codesArrayList) {
            try {
                getOneCodes(codes.getCode());
            } catch (Exception e) {

            }
            logger.info("save day  code ,it is " + codes.getCode() + " name is : " + codes.getName());
            System.out.println("codes");
        }
        Long testTime2 = Calendar.getInstance().getTimeInMillis();
        System.out.println((testTime2 - beginTime) / 1000);
        System.out.println("end");
        logger.info("getAllCodesDayLine end");
        return new ResponseEntity(RespCode.SUCCESS, null);
    }

    @Override
    public ResponseEntity getOneCodeTodayTimeLine(String code) {
        String allUrl = new SpiderUrl().getDayTimeLineUrl(code);
        JSONObject retJson = JSONObject.parseObject(OkHttpUtil.get(allUrl, null));

        return new ResponseEntity(RespCode.SUCCESS, retJson);
    }


    @Override
    public ResponseEntity checkAllCodeDayLine() {
        //        获取所有股票编码；
        ArrayList<codes> codesArrayList = new ArrayList<>();
        for (int i = 1; i <= 3577; i++) {
            codesArrayList.add(codesMapper.selectByPrimaryKey(i));
        }
        String collectionName = "DayLine";
        for (int j = 0; j < codesArrayList.size(); j++) {

            logger.info("check code begin : now is : " + codesArrayList.get(j).getCode());

            String todayStr = dateUse.getTodayDate().replace("-","");
            String collectionNameAll = collectionName + codesArrayList.get(j).getCode().toString();

            try {
                StockDayLine s = mongoTemplate.findById(todayStr, StockDayLine.class,collectionNameAll);
                if (ObjectUtils.isEmpty(s)) {
                    logger.info("miss code is : " + codesArrayList.get(j).getCode());
                    getOneCodes(codesArrayList.get(j).getCode());
                } else {

                }
            } catch (Exception e) {
                logger.info("Exception is " + e + "\n and miss code is : " + codesArrayList.get(j).getCode());
            }
            logger.info("check code end : now is : " + codesArrayList.get(j).getCode());
        }
        return new ResponseEntity(RespCode.SUCCESS, null);
    }
}
