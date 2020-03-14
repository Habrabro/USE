package com.yasdalteam.yasdalege.Networking;

import com.yasdalteam.yasdalege.App;
import com.yasdalteam.yasdalege.Loader;
import com.yasdalteam.yasdalege.MainActivity;

public class ResponseHandler implements IResponseReceivable
{
    @Override
    public void onResponse(BaseResponse response)
    {
        Loader.hideOverlay();
    }

    @Override
    public void onFailure(Throwable t)
    {
        Loader.hideOverlay();
    }

    @Override
    public void onError(String error)
    {
        Loader.hideOverlay();
    }

    @Override
    public void onDisconnected()
    {
        Loader.hideOverlay();
    }
}
