package com.renchaigao.spider.dao;

import lombok.Getter;
import lombok.Setter;

@Setter
@Getter
public class StockMinLine {
    private String id;
    private String dataClass;
    private String code;
    private String date;
    private String time;
    private Float price;
    private Float volume;
    private Float avgPrice;
    private Float ccl;
    private Float netChageRatio;
    private Float preClose;
    private Float amount;
}
