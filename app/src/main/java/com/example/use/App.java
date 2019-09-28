package com.example.use;

import android.app.Application;

import androidx.fragment.app.Fragment;

public class App extends Application
{
    private static App instance;
    private Fragment currentFragment;
    private User user;

    @Override
    public void onCreate()
    {
        super.onCreate();
        instance = this;
        user = new User();
    }

    public void setCurrentFragment(Fragment fragment)
    {
        currentFragment = fragment;
    }

    public Fragment getCurrentFragment()
    {
        return currentFragment;
    }

    public User getUser() { return user; }

    public static App getInstance() { return instance; }
}
