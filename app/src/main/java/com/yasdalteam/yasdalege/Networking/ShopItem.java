package com.yasdalteam.yasdalege.Networking;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

public class ShopItem
{
    @SerializedName("id")
    @Expose
    private int id;
    @SerializedName("name")
    @Expose
    private String name;
    @SerializedName("description")
    @Expose
    private String description;
    @SerializedName("countOfChecks")
    @Expose
    private String countOfChecks;
    @SerializedName("price")
    @Expose
    private String price;
    @SerializedName("disablesAds")
    @Expose
    private Boolean disablesAds;

    public ShopItem(int id, String name, String description, String countOfChecks, String price, Boolean disablesAds)
    {
        this.id = id;
        this.name = name;
        this.description = description;
        this.countOfChecks = countOfChecks;
        this.price = price;
        this.disablesAds = disablesAds;
    }

    public int getId()
    {
        return id;
    }

    public String getName()
    {
        return name;
    }

    public String getDescription()
    {
        return description;
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
