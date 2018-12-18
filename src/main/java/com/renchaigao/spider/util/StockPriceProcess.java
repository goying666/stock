package com.renchaigao.spider.util;

import com.renchaigao.spider.dao.StockDayLine;

public class StockPriceProcess {

    public static StockDayLine ProcessDayLineData(StockDayLine stockDayLine) {
//        先将所有Float的小数位数修改为2位；
        stockDayLine.setOpen(FloatValueProcess(stockDayLine.getOpen()));
        stockDayLine.setHigh(FloatValueProcess(stockDayLine.getHigh()));
        stockDayLine.setLow(FloatValueProcess(stockDayLine.getLow()));
        stockDayLine.setClose(FloatValueProcess(stockDayLine.getClose()));
        stockDayLine.setPreClose(FloatValueProcess(stockDayLine.getPreClose()));
        stockDayLine.setNetChangeRatio(FloatValueProcess(stockDayLine.getNetChangeRatio()));

//        新数据需要计算，不能直接得来
        stockDayLine.setMa5_avgPrice(FloatValueProcess(stockDayLine.getMa5_avgPrice()));
        stockDayLine.setMa10_avgPrice(FloatValueProcess(stockDayLine.getMa10_avgPrice()));
        stockDayLine.setMa20_avgPrice(FloatValueProcess(stockDayLine.getMa20_avgPrice()));

//        策略数据
        stockDayLine.setMax_open(Float4Wei((stockDayLine.getHigh() - stockDayLine.getClose()) / stockDayLine.getPreClose()));
        stockDayLine.setMax_close(Float4Wei((stockDayLine.getHigh() - stockDayLine.getClose()) / stockDayLine.getPreClose()));
        stockDayLine.setMax_close(Float4Wei((stockDayLine.getHigh() - stockDayLine.getClose()) / stockDayLine.getPreClose()));
        stockDayLine.setMax_close(Float4Wei((stockDayLine.getHigh() - stockDayLine.getClose()) / stockDayLine.getPreClose()));
        stockDayLine.setMax_close(Float4Wei((stockDayLine.getHigh() - stockDayLine.getClose()) / stockDayLine.getPreClose()));
        stockDayLine.setMax_close(Float4Wei((stockDayLine.getHigh() - stockDayLine.getClose()) / stockDayLine.getPreClose()));
//        stockDayLine.setMacd_dea(FloatValueProcess(stockDayLine.ge;
//        stockDayLine.setMacd_diff(FloatValueProcess(stockDayLine.ge;
//        stockDayLine.setMacd_macd(FloatValueProcess(stockDayLine.ge;
//        stockDayLine.setKdj_d(FloatValueProcess(stockDayLine.ge;
//        stockDayLine.setKdj_k(FloatValueProcess(stockDayLine.ge;
//        stockDayLine.setKdj_j(FloatValueProcess(stockDayLine.ge;
//        stockDayLine.setRsi1(FloatValueProcess(stockDayLine.ge;
//        stockDayLine.setRsi2(FloatValueProcess(stockDayLine.ge;
//        stockDayLine.setRsi3(FloatValueProcess(stockDayLine.ge;

//        给日线分类
        if (stockDayLine.getClose() > stockDayLine.getOpen()) {
            if (stockDayLine.getHigh() > stockDayLine.getClose()) {
                if (stockDayLine.getLow() < stockDayLine.getOpen()) {
                    stockDayLine.setStockClass("4");
                } else {
                    stockDayLine.setStockClass("2");
                }
            } else {
                if (stockDayLine.getLow() < stockDayLine.getOpen()) {
                    stockDayLine.setStockClass("3");
                } else {
                    stockDayLine.setStockClass("1");
                }
            }
        } else if (stockDayLine.getClose() < stockDayLine.getOpen()) {
            if (stockDayLine.getHigh() > stockDayLine.getOpen()) {
                if (stockDayLine.getLow() < stockDayLine.getClose()) {
                    stockDayLine.setStockClass("8");
                } else {
                    stockDayLine.setStockClass("6");
                }
            } else {
                if (stockDayLine.getLow() < stockDayLine.getClose()) {
                    stockDayLine.setStockClass("7");
                } else {
                    stockDayLine.setStockClass("5");
                }
            }
        } else {
            if (stockDayLine.getHigh() > stockDayLine.getOpen()) {
                if (stockDayLine.getLow() < stockDayLine.getOpen()) {
                    stockDayLine.setStockClass("12");
                } else {
                    stockDayLine.setStockClass("10");
                }
            } else {
                if (stockDayLine.getLow() < stockDayLine.getOpen()) {
                    stockDayLine.setStockClass("11");
                } else {
                    stockDayLine.setStockClass("9");
                }
            }
        }

        return stockDayLine;
    }

    private static StockDayLine DayClassProcess(StockDayLine stockDayLine) {
        Float preLine = (float) 0.0003;
        Float upLine = (float) 0.0002;
        if ((stockDayLine.getClose() - stockDayLine.getOpen())/stockDayLine.getPreClose() > preLine) {
            if (stockDayLine.getHigh() > stockDayLine.getClose()) {
                if (stockDayLine.getLow() < stockDayLine.getOpen()) {
                    stockDayLine.setStockClass("4");
                } else {
                    stockDayLine.setStockClass("2");
                }
            } else {
                if (stockDayLine.getLow() < stockDayLine.getOpen()) {
                    stockDayLine.setStockClass("3");
                } else {
                    stockDayLine.setStockClass("1");
                }
            }
        } else if ((stockDayLine.getOpen() - stockDayLine.getClose())/stockDayLine.getPreClose() > preLine) {
            if (stockDayLine.getHigh() > stockDayLine.getOpen()) {
                if (stockDayLine.getLow() < stockDayLine.getClose()) {
                    stockDayLine.setStockClass("8");
                } else {
                    stockDayLine.setStockClass("6");
                }
            } else {
                if (stockDayLine.getLow() < stockDayLine.getClose()) {
                    stockDayLine.setStockClass("7");
                } else {
                    stockDayLine.setStockClass("5");
                }
            }
        } else {
            if (stockDayLine.getHigh() > stockDayLine.getOpen()) {
                if (stockDayLine.getLow() < stockDayLine.getOpen()) {
                    stockDayLine.setStockClass("12");
                } else {
                    stockDayLine.setStockClass("10");
                }
            } else {
                if (stockDayLine.getLow() < stockDayLine.getOpen()) {
                    stockDayLine.setStockClass("11");
                } else {
                    stockDayLine.setStockClass("9");
                }
            }
        }
        return stockDayLine;
    }

    public static Float FloatValueProcess(Float f) {
        float num = (float) (Math.round(f * 100)) / 100;//兩位有效小數
        return num;
    }

    public static Float Float4Wei(Float f) {
        float num = (float) (Math.round(f * 10000)) / 10000;
        return num;
    }
}
