package com.example.use;

import android.graphics.Outline;
import android.view.View;
import android.view.ViewOutlineProvider;

public class SubjectMenuButtonOutlineProvider extends ViewOutlineProvider
{
    private int padding = (int)App.getInstance().getResources().getDimension(R.dimen.subjectIconOuterPadding);
    private int borderRadius = (int)App.getInstance().getResources().getDimension(R.dimen.subjectMenuButtonBorderRadius);

    @Override
    public void getOutline(View view, Outline outline)
    {
        outline.setRoundRect(padding, 0, view.getWidth() - padding, view.getHeight(), borderRadius);
        outline.setAlpha(0.7f);
    }
}
