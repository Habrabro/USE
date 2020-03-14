package com.yasdalteam.yasdalege;

import android.app.Application;

import com.yasdalteam.yasdalege.Networking.BaseResponse;
import com.yasdalteam.yasdalege.Networking.NetworkService;
import com.yasdalteam.yasdalege.Networking.ResponseHandler;
import com.yasdalteam.yasdalege.Networking.UserResponse;
import com.yasdalteam.yasdalege.Payments.PaymentCache;
import com.yasdalteam.yasdalege.database.DbService;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.concurrent.TimeUnit;

import com.google.android.gms.ads.MobileAds;
import com.vk.api.sdk.*;

public class App extends Application
{
    public final String SERVER_BASE_URL = "http://host1803169.hostland.pro/";
    public final String OLD_SERVER_BASE_URL = "https://usetrainingadmin.000webhostapp.com/";
    public final int SESSION_LIFETIME_IN_DAYS = 14;
    public static final int FILE_REQUEST_CODE = 61599;

    private static App instance;
    private BaseFragment currentFragment;
    private AdsService adsService;
    private PaymentCache paymentCache;

    private User user;

    private List<Subject> subjects = new ArrayList<>();
    private List<Topic> topics = new ArrayList<>();

    @Override
    public void onCreate()
    {
        super.onCreate();

        instance = this;
        adsService = new AdsService();
        VK.initialize(this);
        MobileAds.initialize(this, initializationStatus -> {});
    }

    public List<Subject> getSubjects()
    {
        return subjects;
    }

    public void setSubjects(List<Subject> subjects)
    {
        this.subjects = subjects;
    }

    public List<Topic> getTopics()
    {
        return topics;
    }

    public void setTopics(List<Topic> topics)
    {
        this.topics = topics;
    }

    public AdsService getAdsService()
    {
        return adsService;
    }

    public PaymentCache getPaymentCache()
    {
        return paymentCache;
    }

    public void setPaymentCache(PaymentCache paymentCache)
    {
        this.paymentCache = paymentCache;
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

    public static App shared() { return instance; }

    interface ConfigureUserCompletion { void onUserReceived(User user); }
    public void configureUser(ConfigureUserCompletion completion)
    {
        DbService.getInstance().getUser(result ->
        {
            App.shared().setUser(result);

            Date currentDateTime = new Date();
            Date authorizeDateTime = result.getAuthorizeDateTime();
            long diffInMillies = Math.abs(currentDateTime.getTime() - authorizeDateTime.getTime());
            long diff = TimeUnit.DAYS.convert(diffInMillies, TimeUnit.MILLISECONDS);

            if (diff < SESSION_LIFETIME_IN_DAYS)
            {
                NetworkService.getInstance(new ResponseHandler() {
                    @Override
                    public void onResponse(BaseResponse response)
                    {
                        super.onResponse(response);

                        User user = ((UserResponse)response).getData();
                        user.setLastUpdate(App.shared().getUser().getLastUpdate());
                        if (completion != null) { completion.onUserReceived(user); }
                    }

                    @Override
                    public void onError(String error)
                    {
                        super.onError(error);
                        if (completion != null) { completion.onUserReceived(null); }
                    }
                }).getProfile();
            }
            else
            {
                App.shared().getUser().logout();
            }
        });
    }
}
