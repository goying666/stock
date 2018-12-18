package com.renchaigao.spider.dao;

import lombok.Getter;
import lombok.Setter;

import java.util.ArrayList;

@Setter
@Getter
public class AllCodeAllDayLine {
    private String id;
    private String code;
    private String name;
    private ArrayList<StockDayLine> stockDayLines;
    private Integer dataSum;
    private String LastDataString;
}
