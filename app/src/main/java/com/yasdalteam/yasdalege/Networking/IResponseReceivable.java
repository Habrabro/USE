package com.yasdalteam.yasdalege.Networking;

public interface IResponseReceivable
{
    void onResponse(BaseResponse response);
    void onFailure(Throwable t);
    void onError(String error);
    void onDisconnected();
}
