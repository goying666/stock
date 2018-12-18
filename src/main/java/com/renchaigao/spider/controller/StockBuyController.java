package com.renchaigao.spider.controller;

import com.renchaigao.spider.service.impl.StockBuyServiceImpl;
import com.renchaigao.zujuba.domain.response.ResponseEntity;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;

@Controller
@RequestMapping(value = "/buy")
public class StockBuyController {
    @Autowired
    StockBuyServiceImpl stockBuyService;

    @GetMapping(value = "/guess/init", consumes = "application/json")
    @ResponseBody
    public ResponseEntity InitBuy() {
        return stockBuyService.InitBuy();
    }

}
