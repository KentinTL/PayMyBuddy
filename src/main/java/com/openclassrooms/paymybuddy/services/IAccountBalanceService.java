package com.openclassrooms.paymybuddy.services;

import java.math.BigDecimal;

public interface IAccountBalanceService {
    public BigDecimal getBalance(Long userId);
}
