package com.example.use.Networking;

import androidx.appcompat.app.AppCompatActivity;
import androidx.fragment.app.FragmentActivity;

import com.example.use.App;
import com.example.use.MainActivity;
import com.example.use.PreferencesHelper;
import com.google.android.material.snackbar.Snackbar;

import java.io.IOException;
import java.util.HashSet;
import java.util.prefs.Preferences;

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
                HashSet<String> cookies = new HashSet<>();
                for (String header : originalResponse.headers("Set-Cookie"))
                {
                    cookies.add(header);
                }
                PreferencesHelper.getInstance().putStringSet(PREF_COOKIES, cookies);

                if (App.getInstance().getUser().isAuthorized())
                {
                    App.getInstance().getUser().logout();
                    Snackbar.make(
                            App.getInstance().getCurrentFragment().getView(),
                            "Session expired. Please re-login",
                            Snackbar.LENGTH_SHORT).show();
                }
            }
        }

        return originalResponse;
    }
}
