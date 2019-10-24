package com.example.use;

import android.os.Bundle;
import android.util.Log;
import android.view.View;

import androidx.annotation.NonNull;
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
    public void setSnackbar(Snackbar snackbar)
    {
        this.snackbar = snackbar;
    }

    public Snackbar getSnackbar()
    {
        return snackbar;
    }

    private Snackbar snackbar;

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState)
    {
        super.onCreate(savedInstanceState);
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState)
    {
        super.onViewCreated(view, savedInstanceState);
        App.getInstance().setCurrentFragment(this);
    }

    @Override
    public void onStart()
    {
        super.onStart();
    }

    @Override
    public void onResponse(BaseResponse response)
    {
        ((MainActivity)getActivity()).onLoaded();
    }

    @Override
    public void onFailure(Throwable t)
    {
        Log.i("networking", t.getMessage());
    }

    @Override
    public void onDisconnected()
    {
        ((MainActivity)getActivity()).onDisconnected();
    }

    @Override
    public void onError(String error)
    {
        ((MainActivity)getActivity()).onLoaded();
        Log.i("networking", "Error " + error);
    }
}
