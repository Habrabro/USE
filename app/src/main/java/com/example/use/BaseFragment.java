package com.example.use;

import android.util.Log;

import androidx.fragment.app.Fragment;

import com.example.use.Networking.BaseResponse;
import com.example.use.Networking.IResponseReceivable;

public class BaseFragment extends Fragment implements IResponseReceivable
{
    @Override
    public void onResponse(BaseResponse response)
    {

    }

    @Override
    public void onFailure(Throwable t)
    {
        Log.i("networking", t.getMessage());
    }

    @Override
    public void onDisconnected()
    {

    }

    @Override
    public void onError()
    {

    }
}
