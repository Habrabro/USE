package com.yasdalteam.yasdalege.Payments;

import java.math.BigDecimal;

public class PaymentCache
{
    private BigDecimal amount = BigDecimal.ZERO;
    private long shopItemId = -1;
    private String description;
    private Payment payment;

    public PaymentCache(BigDecimal amount, long shopItemId, String description)
    {
        this.amount = amount;
        this.shopItemId = shopItemId;
        this.description = description;
    }

    public BigDecimal getAmount()
    {
        return amount;
    }

    public long getShopItemId()
    {
        return shopItemId;
    }

    public String getDescription()
    {
        return description;
    }

    public Payment getPayment()
    {
        return payment;
    }

    public void setPayment(Payment payment)
    {
        this.payment = payment;
    }
}
