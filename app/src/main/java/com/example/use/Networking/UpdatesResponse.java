package com.example.use.Networking;

import com.example.use.Update;

import java.util.List;

public class UpdatesResponse extends BaseResponse
{
    private List<Update> data;

    public UpdatesResponse(String status, String error, List<Update> data)
    {
        super(status, error);
        this.data = data;
    }

    public List<Update> getData()
    {
        return data;
    }
}
