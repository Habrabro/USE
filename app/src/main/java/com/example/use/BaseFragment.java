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
        if (snackbar != null)
        {
            snackbar.dismiss();
        }
    }

    @Override
    public void onFailure(Throwable t)
    {
        Log.i("networking", t.getMessage());
    }

    @Override
    public void onDisconnected()
    {
        snackbar = Snackbar.make(
                this.getActivity().findViewById(R.id.fragmentContainer),
                "Disconnected",
                Snackbar.LENGTH_INDEFINITE);
        snackbar.getView().setTranslationZ(130);
        snackbar.show();
    }

    @Override
    public void onError(String error)
    {
        if (snackbar != null)
        {
            snackbar.dismiss();
        }
        Log.i("networking", "Error " + error);
    }
}
