package com.example.use;

import androidx.appcompat.app.ActionBar;
import androidx.appcompat.app.AppCompatActivity;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;
import androidx.fragment.app.FragmentTransaction;

import android.os.Bundle;
import android.widget.Button;

import com.google.android.material.snackbar.Snackbar;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;

public class MainActivity extends AppCompatActivity implements SubjectMenuFragment.Listener,
        SubjectsListFragment.Listener, TopicsListFragment.Listener
{
    private FragmentManager fragmentManager;

    @BindView(R.id.btnProfile) Button profileButton;
    private Snackbar snackbar;

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

    public void replaceFragment (Fragment newFragment, String newFragmentTag)
    {
        String backStateName =  newFragmentTag;
        String fragmentTag = backStateName;

        FragmentManager manager = getSupportFragmentManager();
        boolean fragmentPopped = manager.popBackStackImmediate (backStateName, 0);

        if (!fragmentPopped && manager.findFragmentByTag(fragmentTag) == null){ //fragment not in back stack, create it.
            FragmentTransaction ft = manager.beginTransaction();
            ft.replace(R.id.fragmentContainer, newFragment, fragmentTag);
            ft.setTransition(FragmentTransaction.TRANSIT_FRAGMENT_FADE);
            ft.addToBackStack(backStateName);
            ft.commit();
        }
    }

    public void onLoad()
    {
        snackbar = Snackbar.make(
                findViewById(R.id.fragmentContainer),
                "Loading",
                Snackbar.LENGTH_INDEFINITE);
        snackbar.getView().setTranslationZ(130);
        snackbar.show();
    }

    public void onLoaded()
    {
        if (snackbar != null)
        {
            snackbar.dismiss();
        }
    }

    public void onDisconnected()
    {
        snackbar = Snackbar.make(
                findViewById(R.id.fragmentContainer),
                "Disconnected",
                Snackbar.LENGTH_INDEFINITE);
        snackbar.show();
    }
}
