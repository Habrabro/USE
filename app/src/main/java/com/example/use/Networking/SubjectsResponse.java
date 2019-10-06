package com.example.use.Networking;

import java.util.List;

public class SubjectsResponse extends BaseResponse
{
    private List<Subject> data;

    public SubjectsResponse(List<Subject> data)
    {
        this.data = data;
    }

    public List<Subject> getData()
    {
        return data;
    }
}
