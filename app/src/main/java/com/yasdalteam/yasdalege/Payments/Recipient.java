package com.yasdalteam.yasdalege.Payments;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

public class Recipient
{
    @SerializedName("account_id")
    @Expose
    private String accountId;
    @SerializedName("gateway_id")
    @Expose
    private String gatewayId;

    public String getAccountId() {
        return accountId;
    }

    public String getGatewayId() {
        return gatewayId;
    }
}
