package com.renchaigao.spider.service;

import com.renchaigao.zujuba.domain.response.ResponseEntity;

public interface StockGuessService {

    ResponseEntity GuessStock(String code);
    ResponseEntity GuessStock_One();
}
