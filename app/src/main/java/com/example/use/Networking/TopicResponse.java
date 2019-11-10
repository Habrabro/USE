package com.example.use.Networking;

import com.example.use.Topic;

import java.util.List;

public class TopicResponse extends BaseResponse
{
    private List<Topic> data = null;

    public List<Topic> getData() {
        return data;
    }
}
