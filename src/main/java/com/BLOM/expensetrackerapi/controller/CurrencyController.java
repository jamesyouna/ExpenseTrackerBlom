package com.BLOM.expensetrackerapi.controller;

import com.BLOM.expensetrackerapi.service.CurrencyService;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import java.math.BigDecimal;

@RestController
@RequestMapping("/currency")
public class CurrencyController {

    private final CurrencyService currencyService;

    public CurrencyController(CurrencyService currencyService) {
        this.currencyService = currencyService;
    }

    @GetMapping("/rate")
    public BigDecimal getRate() {
        return currencyService.getUsdToLbpRate();
    }

    @GetMapping("/usd-to-lbp")
    public BigDecimal convertUsdToLbp(@RequestParam BigDecimal amount) {
        return currencyService.convertUsdToLbp(amount);
    }

    @GetMapping("/lbp-to-usd")
    public BigDecimal convertLbpToUsd(@RequestParam BigDecimal amount) {
        return currencyService.convertLbpToUsd(amount);
    }
}