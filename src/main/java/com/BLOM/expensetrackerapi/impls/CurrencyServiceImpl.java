package com.BLOM.expensetrackerapi.impls;

import com.BLOM.expensetrackerapi.service.CurrencyService;
import jakarta.annotation.PostConstruct;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestClient;

import java.math.BigDecimal;
import java.math.RoundingMode;
import java.util.Map;

@Service
public class CurrencyServiceImpl implements CurrencyService {

    private final RestClient restClient;
    private BigDecimal cachedRate = new BigDecimal("89500");

    public CurrencyServiceImpl() {
        this.restClient = RestClient.create();
    }

    @PostConstruct
    public void loadExchangeRateAtStartup() {
        try {
            Map response = restClient.get()
                    .uri("https://open.er-api.com/v6/latest/USD")
                    .retrieve()
                    .body(Map.class);

            Map<String, Object> rates =
                    (Map<String, Object>) response.get("rates");

            cachedRate = new BigDecimal(
                    rates.get("LBP").toString()
            );

            System.out.println(
                    "USD/LBP exchange rate loaded: " + cachedRate
            );

        } catch (Exception e) {

            System.out.println(
                    "Could not load live USD/LBP rate. Using fallback: "
                            + cachedRate
            );
        }
    }

    @Override
    public BigDecimal getUsdToLbpRate() {
        return cachedRate;
    }

    @Override
    public BigDecimal convertUsdToLbp(BigDecimal amount) {
        return amount.multiply(cachedRate);
    }

    @Override
    public BigDecimal convertLbpToUsd(BigDecimal amount) {
        return amount.divide(cachedRate, 2, RoundingMode.HALF_UP);
    }
}