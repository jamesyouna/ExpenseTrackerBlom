package com.BLOM.expensetrackerapi.service;

import java.math.BigDecimal;

public interface CurrencyService {

    BigDecimal getUsdToLbpRate();

    BigDecimal convertUsdToLbp(BigDecimal amount);

    BigDecimal convertLbpToUsd(BigDecimal amount);
}