package com.renchaigao.spider.controller;

import com.renchaigao.spider.service.impl.ProcessDataServiceImpl;
import com.renchaigao.zujuba.domain.response.ResponseEntity;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;

@Controller
@RequestMapping(value = "/process")
public class ProcessDataController {
    @Autowired
    ProcessDataServiceImpl processDataServiceImpl;

    @GetMapping(value = "/init/class", consumes = "application/json")
    @ResponseBody
    public ResponseEntity getAllCodes(){
        return processDataServiceImpl.InitDayLineStockData();
    }

    @GetMapping(value = "/highopen/alltimes", consumes = "application/json")
    @ResponseBody
    public ResponseEntity GetHighOpenData(){
        return processDataServiceImpl.GetHighOpenData(null);
    }


}
