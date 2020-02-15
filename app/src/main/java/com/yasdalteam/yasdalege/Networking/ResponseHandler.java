package com.yasdalteam.yasdalege.Networking;

import com.yasdalteam.yasdalege.App;
import com.yasdalteam.yasdalege.MainActivity;

public class ResponseHandler implements IResponseReceivable
{
    @Override
    public void onResponse(BaseResponse response)
    {
        if (App.shared().getCurrentFragment().getActivity() != null)
        {
            ((MainActivity)App.shared().getCurrentFragment().getActivity()).onLoaded();
        }
    }

    @Override
    public void onFailure(Throwable t)
    {

    }

    @Override
    public void onError(String error)
    {

    }

    @Override
    public void onDisconnected()
    {

    }
}
