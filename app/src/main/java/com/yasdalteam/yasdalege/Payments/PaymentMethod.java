package com.yasdalteam.yasdalege.Payments;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

public class PaymentMethod
{
    @SerializedName("account_number")
    @Expose
    private String accountNumber;
    @SerializedName("type")
    @Expose
    private String type;
    @SerializedName("id")
    @Expose
    private String id;
    @SerializedName("saved")
    @Expose
    private String saved;
    @SerializedName("title")
    @Expose
    private String title;

    public String getAccountNumber()
    {
        return accountNumber;
    }

    public String getType()
    {
        return type;
    }

    public String getId()
    {
        return id;
    }

    public String isSaved()
    {
        return saved;
    }

    public String getTitle()
    {
        return title;
    }
}
