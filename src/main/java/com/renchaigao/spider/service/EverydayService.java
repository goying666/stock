package com.renchaigao.spider.service;

import com.renchaigao.zujuba.domain.response.ResponseEntity;

public interface EverydayService {
    ResponseEntity updateTodayDayLine_All();

    ResponseEntity updateOneDayLine_One(String code, String oneDay);

    ResponseEntity updateTodayMinLine_All();

    ResponseEntity updateTodayMinLine_One(String code);
}
