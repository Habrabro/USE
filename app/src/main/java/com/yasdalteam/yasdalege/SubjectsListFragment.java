package com.yasdalteam.yasdalege;

import android.content.Context;
import android.os.Build;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.annotation.RequiresApi;
import androidx.recyclerview.widget.GridLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.yasdalteam.yasdalege.Networking.BaseResponse;
import com.yasdalteam.yasdalege.Networking.NetworkService;
import com.yasdalteam.yasdalege.Networking.ResponseHandler;
import com.yasdalteam.yasdalege.Networking.SubjectsResponse;
import com.yasdalteam.yasdalege.database.DbService;
import com.yasdalteam.yasdalege.database.DbRequestListener;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

public class SubjectsListFragment extends BaseFragment implements SubjectsListAdapter.Listener
{
    @BindView(R.id.btnSubjectListGuide)
    Button guideButton;

    private SubjectsListAdapter subjectsListAdapter;
    private List<Subject> subjects;

    public SubjectsListFragment()
    {

    }

    public static SubjectsListFragment newInstance()
    {
        SubjectsListFragment fragment = new SubjectsListFragment();
        return fragment;
    }

    @Override
    public void onStart()
    {
        super.onStart();
        App.shared().getTopics().clear();
    }

    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState)
    {
        View view = inflater.inflate(R.layout.fragment_subjects_list, container, false);
        return view;
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState)
    {
        super.onViewCreated(view, savedInstanceState);

        ButterKnife.bind(this, view);
        applyStyle();

        subjects = new ArrayList<>();
        RecyclerView recyclerView = view.findViewById(R.id.rvSubjectsList);
        GridLayoutManager gridLayoutManager = new GridLayoutManager(view.getContext(), 2);
        recyclerView.setLayoutManager(gridLayoutManager);
        subjectsListAdapter = new SubjectsListAdapter(this, subjects);
        recyclerView.setAdapter(subjectsListAdapter);

        if (App.shared().getSubjects().isEmpty())
        {
            Loader.show();
            NetworkService.getInstance(new ResponseHandler() {
                @Override
                public void onResponse(BaseResponse response)
                {
                    super.onResponse(response);
                    List<Subject> subjects = ((SubjectsResponse)response).getData();
                    for (Subject subject : subjects)
                    {
                        if (subject.isActive())
                        {
                            SubjectsListFragment.this.subjects.add(subject);
                        }
                    }
                    App.shared().getSubjects().addAll(subjects);
                    subjectsListAdapter.notifyDataSetChanged();
                    Loader.hide();
                }
            }).getSubjects(null);
        }
        else
        {
            for (Subject subject : App.shared().getSubjects())
            {
                if (subject.isActive())
                {
                    SubjectsListFragment.this.subjects.add(subject);
                }
            }
            subjectsListAdapter.notifyDataSetChanged();
        }
    }

    @Override
    public void OnViewHolderClick(int position, Subject subject)
    {
        SubjectMenuFragment subjectMenuFragment = SubjectMenuFragment.newInstance(subject);
        ((MainActivity)getActivity()).replaceFragment(subjectMenuFragment, "subjectMenuFragment");
    }

    @OnClick(R.id.btnSubjectListGuide)
    public void onGuideButtonClick()
    {
        
    }

    private void applyStyle()
    {
        guideButton.setOutlineProvider(new SubjectMenuButtonOutlineProvider());
    }
}
