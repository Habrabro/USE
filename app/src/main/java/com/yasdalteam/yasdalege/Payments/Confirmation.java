package com.yasdalteam.yasdalege.Payments;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

public class Confirmation {

    @SerializedName("type")
    @Expose
    private String type;
    @SerializedName("confirmation_url")
    @Expose
    private String confirmationUrl;

    public Confirmation(String type, String confirmationUrl)
    {
        this.type = type;
        this.confirmationUrl = confirmationUrl;
    }

    public String getType()
    {
        return type;
    }

    public String getConfirmationUrl()
    {
        return confirmationUrl;
    }
}