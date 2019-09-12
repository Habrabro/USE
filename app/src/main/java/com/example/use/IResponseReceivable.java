package com.example.use;

import javax.security.auth.callback.Callback;

public interface IResponseReceivable
{
    void onResponse(BaseResponse response);
    void onFailure();
    void onError();
    void onDisconnected();
}
