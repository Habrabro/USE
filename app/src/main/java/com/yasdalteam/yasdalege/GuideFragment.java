package com.yasdalteam.yasdalege;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.annotation.NonNull;

public class GuideFragment extends BaseFragment
{
    public GuideFragment() {}

    public static GuideFragment newInstance()
    {
        GuideFragment fragment = new GuideFragment();
        return fragment;
    }

    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState)
    {
        View view = inflater.inflate(R.layout.guide_fragment_layout, container, false);
        return view;
    }
}
