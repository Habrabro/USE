package com.yasdalteam.yasdalege;

import android.widget.LinearLayout;

import com.google.android.gms.ads.AdRequest;
import com.google.android.gms.ads.AdView;
import com.yasdalteam.yasdalege.App;
import com.yasdalteam.yasdalege.R;

public class AdsService
{
    private AdView adView;

    public void setAdView(AdView adView)
    {
        this.adView = adView;
    }

    public void enableAds()
    {
        AdRequest adRequest = new AdRequest.Builder().build();
        adView.loadAd(adRequest);
        LinearLayout.LayoutParams params = new LinearLayout.LayoutParams(
                LinearLayout.LayoutParams.MATCH_PARENT,
                LinearLayout.LayoutParams.WRAP_CONTENT
        );
        adView.setLayoutParams(params);
    }

    public void disableAds()
    {
        adView.destroy();
        LinearLayout.LayoutParams params = new LinearLayout.LayoutParams(
                LinearLayout.LayoutParams.MATCH_PARENT,
                0
        );
        adView.setLayoutParams(params);
    }
}
