package com.yasdalteam.yasdalege.Networking;

import com.yasdalteam.yasdalege.App;
import com.yasdalteam.yasdalege.PreferencesHelper;

import java.io.IOException;
import java.util.Date;
import java.util.HashSet;

import okhttp3.Interceptor;
import okhttp3.Response;

/**
 * This Interceptor add all received Cookies to the app DefaultPreferences.
 * Your implementation on how to save the Cookies on the Preferences MAY VARY.
 * <p>
 * Created by tsuharesu on 4/1/15.
 */
public class ReceivedCookiesInterceptor implements Interceptor
{
    public static final String PREF_COOKIES = "PREF_COOKIES";

    @Override
    public Response intercept(Chain chain) throws IOException
    {
        Response originalResponse = chain.proceed(chain.request());

        if (!originalResponse.headers("Set-Cookie").isEmpty())
        {
            if (originalResponse.headers("Set-Cookie").toArray() != (PreferencesHelper.getInstance().getStringSet(PREF_COOKIES).toArray()))
            {
                // !WARNING! Yandex.kassa SDK has a bug: SDK uses this interceptor

                HashSet<String> cookies = new HashSet<>();
                for (String header : originalResponse.headers("Set-Cookie"))
                {
                    cookies.add(header);
                }
                PreferencesHelper.getInstance().putStringSet(PREF_COOKIES, cookies);

                if (App.shared().getUser() != null)
                {
                    App.shared().getUser().setAuthorizeDateTime(new Date());
                }
            }
        }

        return originalResponse;
    }
}
