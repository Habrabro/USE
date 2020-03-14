package com.yasdalteam.yasdalege;

import androidx.fragment.app.FragmentManager;
import androidx.room.Entity;
import androidx.room.Ignore;
import androidx.room.PrimaryKey;
import androidx.room.TypeConverters;

import com.yasdalteam.yasdalege.database.DateConverter;
import com.yasdalteam.yasdalege.database.DbService;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;

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
    private int availableChecks;
    private boolean isAuthorized = false;

    @Ignore
    private List<IUserObservable> observers = new ArrayList<>();

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

    public List<IUserObservable> getObservers()
    {
        return observers;
    }

    public void addObserver(IUserObservable observer)
    {
        this.observers.add(observer);
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

        if (isAdsEnabled) {
            App.shared().getAdsService().enableAds();
        } else {
            App.shared().getAdsService().disableAds();
        }

        for (IUserObservable observer: observers)
        {
            if (observer != null)
            {
                observer.onAuthorize(this);
            }
        }
    }

    public void logout()
    {
        sessionId = null;
        isAuthorized = false;
        DbService.getInstance().insertOrUpdateUser(this);
        PreferencesHelper.getInstance().putStringSet(PREF_COOKIES, null);

        for (IUserObservable observer: observers)
        {
            if (observer != null)
            {
                observer.onLogout();
            }
        }
    }

    interface IUserObservable
    {
        void onAuthorize(User user);
        void onLogout();
    }
}
