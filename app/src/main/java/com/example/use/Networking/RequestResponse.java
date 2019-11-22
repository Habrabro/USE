package com.example.use.Networking;

import com.example.use.Request;

import java.util.List;

public class RequestResponse extends BaseResponse
{
    public List<Request> getData()
    {
        return data;
    }

    private List<Request> data;
}
