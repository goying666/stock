package com.renchaigao.spider.controller;

import com.renchaigao.spider.service.impl.EverydayServiceImpl;
import com.renchaigao.zujuba.domain.response.ResponseEntity;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;

@Controller
@RequestMapping(value = "/new")
public class NewController {
    @Autowired
    EverydayServiceImpl everydayService;

    @GetMapping(value = "/update/today/all/{check}", consumes = "application/json")
    @ResponseBody
    public ResponseEntity updateTodayDayLine_All(@PathVariable("check") String check){
        return everydayService.updateTodayDayLine_All(check);
    }

    @GetMapping(value = "/update/min/all/{check}", consumes = "application/json")
    @ResponseBody
    public ResponseEntity updateTodayMinLine_All(@PathVariable("check") String check){
        return everydayService.updateTodayMinLine_All(check);
    }


}
