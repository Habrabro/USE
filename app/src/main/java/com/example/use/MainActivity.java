package com.example.use;

import androidx.appcompat.app.AppCompatActivity;
import androidx.fragment.app.FragmentManager;

import android.os.Bundle;

public class MainActivity extends AppCompatActivity implements SubjectsListFragment.Listener,
        TopicsListFragment.Listener
{
    private FragmentManager fragmentManager;

    @Override
    protected void onCreate(Bundle savedInstanceState)
    {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        fragmentManager = getSupportFragmentManager();
        if (fragmentManager.findFragmentByTag("subjectsListFragment") == null)
        {
            SubjectsListFragment subjectsListFragment = SubjectsListFragment.newInstance();
            fragmentManager.beginTransaction()
                    .add(R.id.fragmentContainer, subjectsListFragment, "subjectsListFragment")
                    .commit();
        }
    }

    @Override
    public void onSubjectsListFragmentInteraction(long subjectId)
    {
        if (fragmentManager.findFragmentByTag("subjectsListFragment") != null &&
        fragmentManager.findFragmentByTag("topicsListFragment") == null)
        {
            TopicsListFragment topicsListFragment = TopicsListFragment.newInstance(subjectId);
            fragmentManager.beginTransaction()
                    .replace(R.id.fragmentContainer, topicsListFragment, "topicsListFragment")
                    .addToBackStack(null)
                    .commit();
        }
    }

    @Override
    public void onTopicsListFragmentInteraction(long topicId, long number)
    {
        if (fragmentManager.findFragmentByTag("topicsListFragment") != null &&
                fragmentManager.findFragmentByTag("exercisesListFragment") == null)
        {
            ExercisesListFragment exercisesListFragment = ExercisesListFragment.newInstance(topicId, number);
            fragmentManager.beginTransaction()
                    .replace(R.id.fragmentContainer, exercisesListFragment, "exercisesListFragment")
                    .addToBackStack(null)
                    .commit();
        }
    }
}
