package com.example.use;

import retrofit2.Response;

public class BaseResponse
{
    private String status;
    private String error;

    public String getStatus() { return status; }
    public String getError() { return error; }
}
