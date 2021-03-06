package com.yasdalteam.yasdalege;

import android.content.Context;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;

public class SubjectMenuFragment extends BaseFragment
{
    private static final String PARAM_1 = "param_1";
    private long subjectId;
    private Subject subject;
    public void setSubject(Subject subject)
    {
        this.subject = subject;
    }

    @BindView(R.id.btnDirectory) Button directoryView;
    @BindView(R.id.btnStartTraining) Button startTrainingView;
    @BindView(R.id.btnTopicsList) Button topicsListView;

    public SubjectMenuFragment()
    {

    }

    public static SubjectMenuFragment newInstance(Subject subject)
    {
        SubjectMenuFragment fragment = new SubjectMenuFragment();
        fragment.setSubject(subject);
        return fragment;
    }

    @Override
    public void onCreate(Bundle savedInstanceState)
    {
        super.onCreate(savedInstanceState);
        subjectId = subject.getId();
    }

    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState)
    {
        View view = inflater.inflate(R.layout.fragment_subject_menu, container, false);
        return view;
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState)
    {
        super.onViewCreated(view, savedInstanceState);
        ButterKnife.bind(this, view);
        directoryView.setOutlineProvider(new SubjectMenuButtonOutlineProvider());
        startTrainingView.setOutlineProvider(new SubjectMenuButtonOutlineProvider());
        topicsListView.setOutlineProvider(new SubjectMenuButtonOutlineProvider());

        setupMenuForEnglish();
    }

    @OnClick(R.id.btnDirectory)
    public void onDirectoryViewClick()
    {
        DirectoryFragment directoryFragment = DirectoryFragment.newInstance(subject);
        ((MainActivity)getActivity()).replaceFragment(directoryFragment, "directoryFragment");
    }

    @OnClick(R.id.btnStartTraining)
    public void onStartTrainingViewClick()
    {
        ((MainActivity)getActivity()).replaceFragment(
                VariantFragment.newInstance(subjectId), "variantFragment");
    }

    @OnClick(R.id.btnTopicsList)
    public void onTopicsListViewClick()
    {
        TopicsListFragment topicsListFragment = TopicsListFragment.newInstance(subject);
        ((MainActivity)getActivity()).replaceFragment(topicsListFragment, "topicsListFragment");
    }

    private void setupMenuForEnglish()
    {
        if (subject.getId() == 64 || subject.getName().equals("Английский язык"))
        {
            startTrainingView.setHint("Решить");
            topicsListView.setHint("Практика");
        }
    }
}
