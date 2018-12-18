package com.renchaigao.spider.service;

import com.renchaigao.zujuba.domain.response.ResponseEntity;

public interface EverydayService {
    ResponseEntity updateTodayDayLine_All(String check);

    ResponseEntity updateOneDayLine_One(String code, String oneDay);

    ResponseEntity updateTodayMinLine_All(String check);

    ResponseEntity updateTodayMinLine_One(String code);
}
