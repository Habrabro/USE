package com.yasdalteam.yasdalege;

import android.widget.LinearLayout;

import androidx.fragment.app.FragmentManager;
import androidx.room.Entity;
import androidx.room.Ignore;
import androidx.room.PrimaryKey;
import androidx.room.TypeConverters;

import com.google.android.gms.ads.AdView;
import com.yasdalteam.yasdalege.database.DateConverter;
import com.yasdalteam.yasdalege.database.DbService;

import java.util.Date;

@Entity
@TypeConverters(DateConverter.class)
public class User
{
    @Ignore
    public static final String PREF_COOKIES = "PREF_COOKIES";

    @PrimaryKey(autoGenerate = true)
    public long id;
    private String login;
    private String name;
    private Date lastUpdate;
    private Date authorizeDateTime = new Date();
    private Long sessionId = null;

    public boolean isAdsEnabled()
    {
        return isAdsEnabled;
    }

    public void setAdsEnabled(boolean adsEnabled)
    {
        isAdsEnabled = adsEnabled;
    }

    private boolean isAdsEnabled = true;

    public int getAvailableChecks()
    {
        return availableChecks;
    }

    public void setAvailableChecks(int availableChecks)
    {
        this.availableChecks = availableChecks;
    }

    public void decAvailableChecks()
    {
        this.availableChecks--;
        DbService.getInstance().insertOrUpdateUser(this);
    }

    private int availableChecks;

    private boolean isAuthorized = false;

    public void setLogin(String login)
    {
        this.login = login;
    }
    public String getLogin()
    {
        return login;
    }
    public long getId() { return id; }
    public Date getLastUpdate() { return  lastUpdate; }
    public Long getSessionId()
    {
        return sessionId;
    }
    public void setSessionId(Long sessionId)
    {
        this.sessionId = sessionId;
    }
    public boolean isAuthorized() { return isAuthorized; }
    public void setAuthorized(boolean authorized)
    {
        isAuthorized = authorized;
    }

    public String getName()
    {
        return name;
    }

    public void setName(String name)
    {
        this.name = name;
    }

    public void setLastUpdate(Date lastUpdate) { this.lastUpdate = lastUpdate; }

    public Date getAuthorizeDateTime()
    {
        return authorizeDateTime;
    }

    public void setAuthorizeDateTime(Date authorizeDateTime)
    {
        this.authorizeDateTime = authorizeDateTime;
    }

    public User(Date lastUpdate)
    {
        this.lastUpdate = lastUpdate;
    }

    public void authorize(User user)
    {
        this.sessionId = user.getSessionId();
        this.login = user.getLogin();
        this.name = user.getName();
        this.availableChecks = user.getAvailableChecks();
        this.isAdsEnabled = user.isAdsEnabled;
        this.isAuthorized = true;
        this.authorizeDateTime = new Date();
        DbService.getInstance().insertOrUpdateUser(this);

        if (!isAdsEnabled)
        {
            AdView view = App.getInstance().getCurrentFragment().getActivity().findViewById(R.id.adView);
            view.destroy();
            LinearLayout.LayoutParams params = new LinearLayout.LayoutParams(0, 0);
            view.setLayoutParams(params);
        }
    }

    public void logout()
    {
        sessionId = null;
        isAuthorized = false;
        DbService.getInstance().insertOrUpdateUser(this);
        PreferencesHelper.getInstance().putStringSet(PREF_COOKIES, null);

        FragmentManager fm = App.getInstance().getCurrentFragment().getActivity().getSupportFragmentManager();
        for(int i = 0; i < fm.getBackStackEntryCount(); ++i)
        {
            fm.popBackStack();
        }
    }
}
