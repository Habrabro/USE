package com.example.use.Networking;

import java.util.HashMap;
import java.util.Map;

public class RegisterResponse extends BaseResponse
{
    private Map<String, String> data = new HashMap<>();

    public Map<String, String> getData()
    {
        return data;
    }
}
