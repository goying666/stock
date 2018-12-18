package com.renchaigao.spider.service;

import com.renchaigao.zujuba.domain.response.ResponseEntity;

public interface ProcessDataService {
    ResponseEntity InitDayLineStockData();
    ResponseEntity GetHighOpenData(String date);

    ResponseEntity GuessOne();
    ResponseEntity GuessTwo(String code);
    ResponseEntity GuessThree(String code);

}
