package com.yasdalteam.yasdalege.Networking;

import android.util.Log;

import com.yasdalteam.yasdalege.PreferencesHelper;

import java.io.IOException;
import java.util.HashSet;

import okhttp3.Interceptor;
import okhttp3.Request;
import okhttp3.Response;

/**
 * This interceptor put all the Cookies in Preferences in the Request.
 * Your implementation on how to get the Preferences MAY VARY.
 * <p>
 * Created by tsuharesu on 4/1/15.
 */
public class AddCookiesInterceptor implements Interceptor
{
    public static final String PREF_COOKIES = "PREF_COOKIES";

    @Override
    public Response intercept(Interceptor.Chain chain) throws IOException
    {
        Request.Builder builder = chain.request().newBuilder();
        HashSet<String> preferences = (HashSet<String>)PreferencesHelper.getInstance().getStringSet(PREF_COOKIES);
        for (String cookie : preferences) {
            builder.addHeader("Cookie", cookie);
            Log.v("OkHttp", "Adding Header: " + cookie); // This is done so I know which headers are being added; this interceptor is used after the normal logging of OkHttp
        }

        return chain.proceed(builder.build());
    }
}