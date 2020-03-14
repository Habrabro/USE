package com.yasdalteam.yasdalege.Payments;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;
import com.yasdalteam.yasdalege.Networking.BaseResponse;
import com.yasdalteam.yasdalege.Networking.ShopItem;

import java.util.List;

public class PaymentResponse extends BaseResponse
{
    @SerializedName("data")
    @Expose
    private Payment data;

    public Payment getData()
    {
        return data;
    }
}
