package com.example.use.Networking;

import java.util.HashMap;
import java.util.Map;

public class UserResponse extends BaseResponse
{
    private long sessionId;
    private Map<String, String> data = new HashMap<>();

    public long getSessionId() { return sessionId; }

    public Map<String, String> getData()
    {
        return data;
    }
}
