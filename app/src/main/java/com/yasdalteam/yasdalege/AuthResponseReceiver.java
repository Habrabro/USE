package com.yasdalteam.yasdalege;

import com.yasdalteam.yasdalege.Networking.BaseResponse;
import com.yasdalteam.yasdalege.Networking.IResponseReceivable;
import com.yasdalteam.yasdalege.Networking.UserResponse;
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
        App.shared().getUser().authorize(user);
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
