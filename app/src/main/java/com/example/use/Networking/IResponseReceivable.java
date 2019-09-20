package com.example.use.Networking;

public interface IResponseReceivable
{
    void onResponse(BaseResponse response);
    void onFailure(Throwable t);
    void onError();
    void onDisconnected();
}
