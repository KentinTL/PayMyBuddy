package com.openclassrooms.paymybuddy.tools;

import org.springframework.stereotype.Component;

import java.math.BigDecimal;
import java.math.RoundingMode;

@Component
public class PercentageCalculator {

    public BigDecimal calculPercent(BigDecimal amount) {
        if (amount == null || amount.compareTo(BigDecimal.ZERO) <= 0) {
            throw new IllegalArgumentException("Le montant doit être positif.");
        }

        BigDecimal percentageValue = amount.multiply(BigDecimal.valueOf(0.005)); // 0.5%
        return percentageValue.setScale(2, RoundingMode.HALF_UP); // Arrondi à 2 décimales
    }
}