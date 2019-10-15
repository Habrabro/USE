package com.example.use;

import androidx.appcompat.app.ActionBar;
import androidx.appcompat.app.AppCompatActivity;
import androidx.fragment.app.FragmentManager;

import android.os.Bundle;
import android.widget.Button;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;

public class MainActivity extends AppCompatActivity implements SubjectMenuFragment.Listener,
        SubjectsListFragment.Listener, TopicsListFragment.Listener
{
    private FragmentManager fragmentManager;

    @BindView(R.id.btnProfile) Button profileButton;

    @Override
    protected void onCreate(Bundle savedInstanceState)
    {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        ActionBar actionBar = getSupportActionBar();
        actionBar.setDisplayOptions(ActionBar.DISPLAY_SHOW_CUSTOM);
        actionBar.setDisplayShowCustomEnabled(true);
        actionBar.setCustomView(R.layout.action_bar);

        ButterKnife.bind(this, actionBar.getCustomView());

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
        fragmentManager.findFragmentByTag("subjectMenuFragment") == null)
        {
            SubjectMenuFragment subjectMenuFragment = SubjectMenuFragment.newInstance(subjectId);
            fragmentManager.beginTransaction()
                    .setCustomAnimations(R.animator.fragment_transition_slide_in,
                            android.R.animator.fade_out)
                    .replace(R.id.fragmentContainer, subjectMenuFragment, "subjectMenuFragment")
                    .addToBackStack(null)
                    .commit();
        }
    }

    @Override
    public void onDirectoryFragmentDisplay(long subjectId)
    {
        if (fragmentManager.findFragmentByTag("subjectMenuFragment") != null &&
                fragmentManager.findFragmentByTag("directoryFragment") == null)
        {
            DirectoryFragment directoryFragment = DirectoryFragment.newInstance(subjectId);
            fragmentManager.beginTransaction()
                    .setCustomAnimations(R.animator.fragment_transition_slide_in,
                            android.R.animator.fade_out)
                    .replace(R.id.fragmentContainer, directoryFragment, "directoryFragment")
                    .addToBackStack(null)
                    .commit();
        }
    }

    @Override
    public void onTopicsListFragmentDisplay(long subjectId)
    {
        if (fragmentManager.findFragmentByTag("subjectMenuFragment") != null &&
                fragmentManager.findFragmentByTag("topicsListFragment") == null)
        {
            TopicsListFragment topicsListFragment = TopicsListFragment.newInstance(subjectId);
            fragmentManager.beginTransaction()
                    .setCustomAnimations(R.animator.fragment_transition_slide_in,
                            android.R.animator.fade_out)
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
                    .setCustomAnimations(R.animator.fragment_transition_slide_in,
                            android.R.animator.fade_out)
                    .replace(R.id.fragmentContainer, exercisesListFragment, "exercisesListFragment")
                    .addToBackStack(null)
                    .commit();
        }
    }

    @OnClick(R.id.btnProfile)
    public void onProfileButtonClick()
    {
//        View bottomSheet = App.getInstance().getCurrentFragment().getView().findViewById(R.id.bottomSheet);
//        BottomSheetBehavior<View> behavior = BottomSheetBehavior.from(bottomSheet);
//        behavior.setState(BottomSheetBehavior.STATE_EXPANDED);
        if (fragmentManager.findFragmentByTag("bottomSheet") == null)
        {
            BottomSheetFragment bottomSheet = BottomSheetFragment.newInstance();
            bottomSheet.show(fragmentManager, "bottomSheet");
        }
    }
}
