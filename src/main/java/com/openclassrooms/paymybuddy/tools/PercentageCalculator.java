package com.openclassrooms.paymybuddy.tools;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;

import java.math.BigDecimal;
import java.math.RoundingMode;

@Component
public class PercentageCalculator {
    @Value("${commission.pourcentage}")
    private String discountPercentage;

    public BigDecimal calculPercent(BigDecimal amount) {
        if (amount == null || amount.compareTo(BigDecimal.ZERO) <= 0) {
            throw new IllegalArgumentException("Le montant doit être positif.");
        }

        BigDecimal percentageValue = amount.multiply(BigDecimal.valueOf(Double.parseDouble(discountPercentage)));
        return percentageValue.setScale(2, RoundingMode.HALF_UP);
    }
}