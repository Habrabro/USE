package com.yasdalteam.yasdalege.Networking;

import com.yasdalteam.yasdalege.Topic;

import java.util.List;

public class TopicResponse extends BaseResponse
{
    private List<Topic> data = null;

    public List<Topic> getData() {
        return data;
    }
}
