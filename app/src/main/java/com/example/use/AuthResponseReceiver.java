package com.example.use;

import com.example.use.Networking.BaseResponse;
import com.example.use.Networking.IResponseReceivable;
import com.example.use.Networking.UserResponse;
import com.google.android.material.snackbar.Snackbar;

public abstract class AuthResponseReceiver implements IResponseReceivable
{
    private BottomSheetFragment bottomSheetFragment;

    public AuthResponseReceiver(BottomSheetFragment bottomSheetFragment)
    {
        this.bottomSheetFragment = bottomSheetFragment;
    }

    @Override
    public void onResponse(BaseResponse response)
    {
        User user = ((UserResponse)response).getData();
        App.getInstance().getUser().authorize(user);
        bottomSheetFragment.reset();
    }

    @Override
    public void onDisconnected()
    {
        bottomSheetFragment.onDisconnected();
    }

    @Override
    public void onFailure(Throwable t)
    {

    }

    @Override
    public void onError(String error)
    {
        switch (error)
        {
            case "401":
                Snackbar snackbar = Snackbar.make(
                        bottomSheetFragment.getContentView(),
                        "Неверный логин/пароль",
                        Snackbar.LENGTH_SHORT);
                snackbar.getView().setTranslationZ(30);
                snackbar.show();
        }
    }
}
