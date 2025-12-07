package org.example.gym_application.domain;

import java.math.BigDecimal;

public enum MembershipType {
    MONTHLY(new BigDecimal("100.00")),
    QUARTERLY(new BigDecimal("270.00")),
    YEARLY(new BigDecimal("1000.00"));

    private final BigDecimal price;

    MembershipType(BigDecimal price) {
        this.price = price;
    }

    public BigDecimal getPrice() {
        return price;
    }
}




