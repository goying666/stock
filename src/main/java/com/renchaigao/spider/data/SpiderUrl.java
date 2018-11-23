package com.renchaigao.spider.data;

import lombok.Getter;
import lombok.Setter;

@Getter
public class SpiderUrl {
    private String allDataUrl = "http://nufm.dfcfw.com/EM_Finance2014NumericApplication/JS.aspx?cb=jQuery112402877553911409074_1542688436847&type=CT&token=4f1862fc3b5e77c150a2b985b12db0fd&sty=FCOIATC&js=(%7Bdata%3A%5B(x)%5D%2CrecordsFiltered%3A(tot)%7D)&cmd=C._A&st=(Code)&sr=1&p=1&ps=3577&_=1542688436860";
    private String dayTimeLineUrl = "https://gupiao.baidu.com/api/stocks/stockdaybar?from=pc&os_ver=1&cuid=xxx&vv=100&format=json&step=3&start=&count=160&fq_type=no&timestamp=1542713620813&stock_code=";
    private String minuteTimeLineUrl = "https://gupiao.baidu.com/api/stocks/stocktimeline?from=pc&os_ver=1&cuid=xxx&vv=100&format=json&timestamp=1542730632705&stock_code=";

    public String getDayTimeLineUrl(String code){
        if (Integer.valueOf(code) > 599999)
            return this.dayTimeLineUrl + "sh" + code;
        else
            return this.dayTimeLineUrl + "sz" + code;
    }
    public String getMinuteTimeLineUrl(String code){
        if (Integer.valueOf(code) > 599999)
            return this.minuteTimeLineUrl + "sh" + code;
        else
            return this.minuteTimeLineUrl + "sz" + code;
    }
}
