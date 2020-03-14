package com.yasdalteam.yasdalege.Payments;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

public class Amount {

    @SerializedName("value")
    @Expose
    private String value;
    @SerializedName("currency")
    @Expose
    private String currency;

    public String getValue()
    {
        return value;
    }

    public String getCurrency()
    {
        return currency;
    }
}
