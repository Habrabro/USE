package com.yasdalteam.yasdalege;

import android.view.View;

import com.google.android.material.snackbar.Snackbar;

public class Loader
{
    private static Snackbar snackbar;

    static void show()
    {
        if (App.shared().getCurrentFragment().getView() != null)
        {
            String tag = App.shared().getCurrentFragment().getTag();
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

    static void hide()
    {
        if (snackbar != null)
        {
            snackbar.dismiss();
        }
    }
}
