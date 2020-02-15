package com.yasdalteam.yasdalege;

import android.app.Application;

import com.yasdalteam.yasdalege.Networking.BaseResponse;
import com.yasdalteam.yasdalege.Networking.IResponseReceivable;
import com.yasdalteam.yasdalege.Networking.NetworkService;
import com.yasdalteam.yasdalege.Networking.UserResponse;
import com.yasdalteam.yasdalege.database.DbService;

import java.util.List;

import com.google.android.gms.ads.MobileAds;
import com.vk.api.sdk.*;

public class App extends Application
{
    public final String SERVER_BASE_URL = "http://host1803169.hostland.pro/";
    public final String OLD_SERVER_BASE_URL = "https://usetrainingadmin.000webhostapp.com/";

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

    public void setUser(User user)
    {
        this.user = user;
    }

    public static App getInstance() { return instance; }
}
