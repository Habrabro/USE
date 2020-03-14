package com.yasdalteam.yasdalege.Networking;

public class BaseResponse
{
    private String status;
    private String error;

    private String message;

    public BaseResponse(){}

    public BaseResponse(String status, String error)
    {
        this.status = status;
        this.error = error;
    }

    public String getStatus() { return status; }
    public String getError() { return error; }
    public String getMessage() { return message; }

    public void setMessage(String message)
    {
        this.message = message;
    }
}
