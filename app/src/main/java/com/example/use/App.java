package com.example.use;

import android.app.Application;
import android.icu.util.Calendar;

import androidx.fragment.app.Fragment;
import androidx.room.Room;

import com.example.use.database.Db;
import com.example.use.database.DbRequestListener;
import com.example.use.database.DbService;

import java.util.GregorianCalendar;

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
        DbService.getInstance().getUser(result -> user = result);
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
