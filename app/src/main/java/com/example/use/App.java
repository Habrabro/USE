package com.example.use;

import android.app.Application;

import com.example.use.Networking.BaseResponse;
import com.example.use.Networking.IResponseReceivable;
import com.example.use.Networking.NetworkService;
import com.example.use.Networking.UserResponse;
import com.example.use.database.DbService;

import java.util.List;

import com.google.android.gms.ads.MobileAds;
import com.google.android.gms.ads.initialization.InitializationStatus;
import com.google.android.gms.ads.initialization.OnInitializationCompleteListener;
import com.vk.api.sdk.*;
import com.vk.api.sdk.utils.VKUtils;

public class App extends Application
{
    public final String SERVER_BASE_URL = "http://host1803169.hostland.pro/";

    private static App instance;
    private BaseFragment currentFragment;
    private User user;

    public List<Exercise> getFavoriteExercises()
    {
        return favoriteExercises;
    }

    public List<Exercise> getCompletedExercises()
    {
        return completedExercises;
    }

    private List<Exercise> favoriteExercises, completedExercises;

    @Override
    public void onCreate()
    {
        super.onCreate();
        instance = this;
        DbService.getInstance().getUser(result ->
        {
            user = result;
//            NetworkService.getInstance(new IResponseReceivable()
//            {
//                @Override
//                public void onResponse(BaseResponse response)
//                {
//                    User user = ((UserResponse)response).getData();
//                    user.setLastUpdate(App.getInstance().user.getLastUpdate());
//                    App.getInstance().user.authorize(user);
//                }
//
//                @Override
//                public void onFailure(Throwable t)
//                {
//
//                }
//
//                @Override
//                public void onError(String error)
//                {
//
//                }
//
//                @Override
//                public void onDisconnected()
//                {
//
//                }
//            }).getProfile();
        });

        VK.initialize(this);

        MobileAds.initialize(this, initializationStatus ->
        {

        });
    }

    public void setCurrentFragment(BaseFragment fragment)
    {
        currentFragment = fragment;
    }

    public BaseFragment getCurrentFragment()
    {
        return currentFragment;
    }

    public User getUser() { return user; }

    public static App getInstance() { return instance; }
}
