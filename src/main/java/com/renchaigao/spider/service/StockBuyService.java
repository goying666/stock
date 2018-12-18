package com.renchaigao.spider.service;

import com.renchaigao.zujuba.domain.response.ResponseEntity;

public interface StockBuyService {
    ResponseEntity GuessTwoBuyInit();
    ResponseEntity InitBuy();//前一天获取完当天数据后，将靠谱的策略进行判断；
}
