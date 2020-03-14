package com.yasdalteam.yasdalege;

import android.app.ProgressDialog;
import android.content.Context;
import android.view.View;

import com.google.android.material.snackbar.Snackbar;

public class Loader
{
    private static Snackbar snackbar;
    private static ProgressDialog dialog;

    static void show()
    {
        if (App.shared().getCurrentFragment().getView() != null)
        {
            View view = App.shared().getCurrentFragment().getView();
            snackbar = Snackbar.make(
                    view,
                    "Загрузка",
                    Snackbar.LENGTH_INDEFINITE);
            snackbar.getView().setTranslationZ(130);
            snackbar.show();
        }
    }

    static void show(String text, int length)
    {
        if (App.shared().getCurrentFragment().getView() != null)
        {
            View view = App.shared().getCurrentFragment().getView();
            snackbar = Snackbar.make(view, text, length);
            snackbar.getView().setTranslationZ(130);
            snackbar.show();
        }
    }

    static void showOverlay(String title, String description, Context context)
    {
        dialog = ProgressDialog.show(
                context,
                title,
                description,
                true
        );
    }

    static void hide()
    {
        if (snackbar != null)
        {
            snackbar.dismiss();
        }
    }

    public static void hideOverlay()
    {
        if (dialog != null)
        {
            dialog.dismiss();
        }
    }
}
