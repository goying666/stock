package com.renchaigao.spider.dao;

import lombok.Getter;
import lombok.Setter;

@Setter
@Getter
public class StockDayLine {
    private String id;
    private String dataClass;
    private String code;
    private String dateString;
    private Float open;
    private Float high;
    private Float low;
    private Float close;
    private Long volume;
    private Long amount;
    private Float preClose;
    private Float netChangeRatio; //涨跌幅

    private Float ma5_volume;
    private Float ma10_volume;
    private Float ma20_volume;
    private Float ma5_avgPrice;
    private Float ma10_avgPrice;
    private Float ma20_avgPrice;
    private Float macd_diff;
    private Float macd_dea;
    private Float macd_macd;
    private Float kdj_k;
    private Float kdj_d;
    private Float kdj_j;
    private Float rsi1;
    private Float rsi2;
    private Float rsi3;


    private Float ChangeSorting;//涨跌额
    private Float AmplitudeSorting;//振幅
    private Float VolumeRate; //量比
    private Float TurnoverRate;//换手率
    private Float PERationSorting;//市盈率（动态）
    private Float PBSorting;//市净率

    private String stockClass;

    //    保留四位有效数字
    private Float max_open;
    private Float max_close;
    private Float max_min;
    private Float open_min;
    private Float close_min;
    private Float open_close;

    //    四个参数对比昨日四个参数的数据
    private Float open_compare_yesterday;
    private Float close_compare_yesterday;
    private Float high_compare_yesterday;
    private Float low_compare_yesterday;

}
