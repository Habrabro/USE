package com.example.use;

import androidx.appcompat.app.AppCompatActivity;
import androidx.fragment.app.FragmentManager;

import android.os.Bundle;

public class MainActivity extends AppCompatActivity
{

    @Override
    protected void onCreate(Bundle savedInstanceState)
    {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        FragmentManager fragmentManager = getSupportFragmentManager();
        if (fragmentManager.findFragmentByTag("subjectsListFragment") == null)
        {
            SubjectsListFragment subjectsListFragment = SubjectsListFragment.newInstance();
            fragmentManager.beginTransaction()
                    .replace(R.id.fragmentContainer, subjectsListFragment, "subjectsListFragment")
                    .commit();
        }
    }
}
