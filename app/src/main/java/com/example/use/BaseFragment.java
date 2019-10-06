package com.example.use;

import android.os.Bundle;
import android.util.Log;

import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;

import com.example.use.Networking.BaseResponse;
import com.example.use.Networking.IResponseReceivable;
import com.example.use.Networking.Subject;
import com.example.use.database.IDbResponseReceivable;
import com.google.android.material.snackbar.Snackbar;

import java.util.List;

public class BaseFragment extends Fragment implements IResponseReceivable
{
    @Override
    public void onCreate(@Nullable Bundle savedInstanceState)
    {
        super.onCreate(savedInstanceState);

    }

    @Override
    public void onStart()
    {
        super.onStart();
        App.getInstance().setCurrentFragment(this);
    }

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
        Snackbar.make(this.getView(), "Disconnected", Snackbar.LENGTH_SHORT).show();
    }

    @Override
    public void onError(String error)
    {
        Log.i("networking", "Error " + error);
    }
}
