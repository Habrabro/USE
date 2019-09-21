package com.example.use.Networking;

import java.util.List;

public class Topic extends BaseResponse
{
    private List<TopicDatum> data = null;

    public List<TopicDatum> getData() {
        return data;
    }
}
