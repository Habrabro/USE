package com.example.use;

import android.app.Application;

import androidx.fragment.app.Fragment;
import androidx.room.Room;

import com.example.use.database.Db;

public class App extends Application
{
    private static App instance;
    private Fragment currentFragment;
    private User user;
    private Db database;

    @Override
    public void onCreate()
    {
        super.onCreate();
        instance = this;
        user = new User();
        database = Room
                .databaseBuilder(this, Db.class, "database")
                .build();
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

    public Db getDatabase() { return database; }

    public static App getInstance() { return instance; }
}
