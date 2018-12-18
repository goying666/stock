package com.renchaigao.spider.controller;

import com.renchaigao.spider.service.impl.StockHistoryServiceImpl;
import com.renchaigao.zujuba.domain.response.ResponseEntity;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;

@Controller
@RequestMapping(value = "/history")
public class StockHistoryController {
    @Autowired
    StockHistoryServiceImpl stockHistoryService;

    @GetMapping(value = "/day/get", consumes = "application/json")
    @ResponseBody
    public ResponseEntity SaveHistoryStockData(){
        return stockHistoryService.SaveHistoryStockData();
    }

    @GetMapping(value = "/day/update", consumes = "application/json")
    @ResponseBody
    public ResponseEntity updateHistoryStockData(){
        return stockHistoryService.updateHistoryStockData();
    }

}
