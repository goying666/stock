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
@RequestMapping(value = "/everyday")
public class EverydayController {
    @Autowired
    EverydayServiceImpl everydayService;

    @GetMapping(value = "/update/today/all", consumes = "application/json")
    @ResponseBody
    public ResponseEntity updateTodayDayLine_All(){
        return everydayService.updateTodayDayLine_All();
    }



    @GetMapping(value = "/update/min/all", consumes = "application/json")
    @ResponseBody
    public ResponseEntity updateTodayMinLine_All(){
        return everydayService.updateTodayMinLine_All();
    }

    @GetMapping(value = "/update/day/{code}/{date}", consumes = "application/json")
    @ResponseBody
    public ResponseEntity updateOneDayLine_One(@PathVariable("code") String code,@PathVariable("date") String oneDay){
        return everydayService.updateOneDayLine_One(code,oneDay);
    }
    @GetMapping(value = "/update/min/{code}", consumes = "application/json")
    @ResponseBody
    public ResponseEntity updateTodayMinLine_One(@PathVariable("code") String code){
        return everydayService.updateTodayMinLine_One(code);
    }

}
