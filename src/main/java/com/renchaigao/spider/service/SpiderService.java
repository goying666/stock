package com.renchaigao.spider.service;

import com.renchaigao.zujuba.domain.response.ResponseEntity;
import com.renchaigao.zujuba.mongoDB.info.GroupMessages;
import com.renchaigao.zujuba.mongoDB.info.team.TeamInfo;

public interface SpiderService {

    ResponseEntity getAllCodes();
    ResponseEntity saveTodayAllCodes();
    ResponseEntity getOneCodes(String code);
    ResponseEntity getOneCodeTodayTimeLine(String code);
    ResponseEntity getAllCodesDayLine();

    ResponseEntity checkAllCodeDayLine();

}
