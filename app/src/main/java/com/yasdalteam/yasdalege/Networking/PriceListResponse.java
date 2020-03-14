package com.yasdalteam.yasdalege.Networking;

import java.util.List;

public class PriceListResponse extends BaseResponse
{
    private List<ShopItem> data;

    public List<ShopItem> getData()
    {
        return data;
    }
}
