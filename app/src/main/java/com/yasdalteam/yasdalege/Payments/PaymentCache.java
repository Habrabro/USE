package com.yasdalteam.yasdalege.Payments;

import java.math.BigDecimal;

public class PaymentCache
{
    private BigDecimal amount = BigDecimal.ZERO;
    private long shopItemId = -1;
    private String description;
    private Payment payment;
    private int countOfChecks;
    private boolean disablesAds;

    public PaymentCache(BigDecimal amount, long shopItemId, String description, int countOfChecks, boolean disablesAds)
    {
        this.amount = amount;
        this.shopItemId = shopItemId;
        this.description = description;
        this.countOfChecks = countOfChecks;
        this.disablesAds = disablesAds;
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

    public int getCountOfChecks()
    {
        return countOfChecks;
    }

    public boolean doesDisableAds()
    {
        return disablesAds;
    }
}
