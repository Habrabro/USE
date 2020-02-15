package com.yasdalteam.yasdalege;

import android.content.Context;
import android.content.SharedPreferences;

import androidx.fragment.app.FragmentActivity;

import java.util.HashSet;
import java.util.Set;

public class PreferencesHelper
{
    public static final String APP_PREFERENCES = "mysettings";

    private static PreferencesHelper instance;

    private SharedPreferences mSettings;
    private FragmentActivity activity;

    public PreferencesHelper()
    {
        if (App.getInstance().getCurrentFragment() != null) {
            activity = App.getInstance().getCurrentFragment().getActivity();
            mSettings = activity.getSharedPreferences(APP_PREFERENCES, Context.MODE_PRIVATE);
        }
    }

    public static PreferencesHelper getInstance()
    {
        if (instance == null)
        {
            instance = new PreferencesHelper();
        }
        return instance;
    }

    public void putStringSet(String key, HashSet<String> value)
    {
        if (mSettings != null)
        {
            SharedPreferences.Editor editor = mSettings.edit();
            editor.putStringSet(key, value);
            editor.apply();
        }
    }

    public Set<String> getStringSet(String key)
    {
        if (mSettings != null) {
            return mSettings.getStringSet(key, new HashSet<>());
        } else {
            return new HashSet<>();
        }
    }
}
