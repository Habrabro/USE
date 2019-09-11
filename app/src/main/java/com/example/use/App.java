package com.example.use;

import android.app.Application;

public class App extends Application
{
    private static Application instance;

    @Override
    public void onCreate()
    {
        super.onCreate();
        instance = this;
    }

    public static Application getInstance() { return instance; }
}
