package com.yasdalteam.yasdalege.Payments;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

public class Payment
{
    @SerializedName("id")
    @Expose
    private String id;
    @SerializedName("status")
    @Expose
    private String status;
    @SerializedName("recipient")
    @Expose
    private Recipient recipient;
    @SerializedName("amount")
    @Expose
    private Amount amount;
    @SerializedName("description")
    @Expose
    private String description;
    @SerializedName("payment_method")
    @Expose
    private PaymentMethod paymentMethod;
    @SerializedName("created_at")
    @Expose
    private String createdAt;
    @SerializedName("paid")
    @Expose
    private String paid;
    @SerializedName("refundable")
    @Expose
    private String refundable;
    @SerializedName("expires_at")
    @Expose
    private String expiresAt;
    @SerializedName("test")
    @Expose
    private String test;

    public String getId()
    {
        return id;
    }

    public String getStatus()
    {
        return status;
    }

    public Recipient getRecipient()
    {
        return recipient;
    }

    public Amount getAmount()
    {
        return amount;
    }

    public PaymentMethod getPaymentMethod()
    {
        return paymentMethod;
    }

    public String getCreatedAt()
    {
        return createdAt;
    }

    public String isPaid()
    {
        return paid;
    }

    public String isRefundable()
    {
        return refundable;
    }

    public String getExpiresAt()
    {
        return expiresAt;
    }

    public String isTest()
    {
        return test;
    }
}
