package com.yasdalteam.yasdalege.Networking;

import com.yasdalteam.yasdalege.Request;

import java.util.List;

public class RequestResponse extends BaseResponse
{
    public List<Request> getData()
    {
        return data;
    }

    private List<Request> data;
}
