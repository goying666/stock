package com.renchaigao.spider.service;

import com.renchaigao.zujuba.domain.response.ResponseEntity;

public interface StockHistoryService {
    ResponseEntity SaveHistoryStockData();
    ResponseEntity updateHistoryStockData();
}
