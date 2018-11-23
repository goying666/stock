package com.renchaigao.spider.dao;

import lombok.Getter;
import lombok.Setter;

@Setter
@Getter
public class StockBasic {
    private String id;
    private String code;
    private String name;
    private Integer dayLineDataNumber;
}
