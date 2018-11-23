package com.renchaigao.spider.controller;

import com.renchaigao.spider.service.impl.SpiderServiceImpl;
import com.renchaigao.zujuba.domain.response.ResponseEntity;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.*;

@Controller
@RequestMapping(value = "/spider")
public class MessageController {
    @Autowired
    SpiderServiceImpl spiderServiceImpl;

    @GetMapping(value = "/getall", consumes = "application/json")
    @ResponseBody
    public ResponseEntity getAllCodes(){
        return spiderServiceImpl.getAllCodes();
    }

    @GetMapping(value = "/get/{code}", consumes = "application/json")
    @ResponseBody
    public ResponseEntity getOneCode(@PathVariable("code") String code){
        return spiderServiceImpl.getOneCodes(code);
    }
    @GetMapping(value = "/gettodayall", consumes = "application/json")
    @ResponseBody
    public ResponseEntity saveTodayAllCodes(){
        return spiderServiceImpl.saveTodayAllCodes();
    }


    @GetMapping(value = "/getacdl", consumes = "application/json")
    @ResponseBody
    public ResponseEntity getAllCodesDayLine(){
        return spiderServiceImpl.getAllCodesDayLine();
    }
    @GetMapping(value = "/checkallcdl", consumes = "application/json")
    @ResponseBody
    public ResponseEntity checkAllCodeDayLine(){
        return spiderServiceImpl.checkAllCodeDayLine();
    }

}
