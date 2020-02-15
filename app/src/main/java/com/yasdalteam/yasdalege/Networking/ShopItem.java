package com.yasdalteam.yasdalege.Networking;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

public class ShopItem
{
    @SerializedName("id")
    @Expose
    private int id;
    @SerializedName("countOfChecks")
    @Expose
    private String countOfChecks;
    @SerializedName("price")
    @Expose
    private String price;
    @SerializedName("disablesAds")
    @Expose
    private Boolean disablesAds;

    public ShopItem(int id, String countOfChecks, String price, Boolean disablesAds)
    {
        this.id = id;
        this.countOfChecks = countOfChecks;
        this.price = price;
        this.disablesAds = disablesAds;
    }

    public int getId()
    {
        return id;
    }

    public String getCountOfChecks()
    {
        return countOfChecks;
    }

    public String getPrice()
    {
        return price;
    }

    public Boolean getDisablesAds()
    {
        return disablesAds;
    }
}
