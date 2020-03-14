package com.yasdalteam.yasdalege;

import android.util.Log;
import android.widget.TextView;

import com.google.android.material.snackbar.Snackbar;

public class Messager
{
    static int MAX_LINES = 5;

    static void message(String message)
    {
        show(message, "OK", Snackbar.LENGTH_INDEFINITE, MAX_LINES);
    }

    public static void notify(String message, int length)
    {
        show(message, null, length, MAX_LINES);
    }

    private static void show(String message, String buttonText, int length, int maxLines)
    {
        if (App.shared().getCurrentFragment() != null)
        {
            Snackbar snackbar = Snackbar.make(
                    App.shared().getCurrentFragment().getView(),
                    message,
                    length);
            if (buttonText != null)
            {
                snackbar.setAction(buttonText, view -> snackbar.dismiss());
            }
            TextView textView = snackbar.getView().findViewById(R.id.snackbar_text);
            textView.setMaxLines(maxLines);
            snackbar.show();
        }
        else
        {
            Log.i("Messager", "CurrentFragment is null");
        }
    }
}
