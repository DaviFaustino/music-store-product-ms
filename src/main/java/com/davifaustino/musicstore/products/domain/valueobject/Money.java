package com.davifaustino.musicstore.products.domain.valueobject;

import java.math.BigDecimal;
import java.math.RoundingMode;

public record Money(BigDecimal amount) {

    public Money {
        if (amount == null || amount.signum() <= 0) {
            throw new IllegalArgumentException("amount must be greater than zero");
        }

        amount = amount.setScale(2, RoundingMode.HALF_UP);
    }
}
