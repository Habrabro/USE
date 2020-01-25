package com.example.use;

import android.content.Context;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.recyclerview.widget.GridLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.example.use.Networking.BaseResponse;
import com.example.use.database.DbService;
import com.example.use.database.DbRequestListener;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

public class SubjectsListFragment extends BaseFragment implements SubjectsListAdapter.Listener
{
    private Listener mListener;
    private SubjectsListAdapter subjectsListAdapter;
    private HashMap<String, IDbOperationable> tableOperationsMap;
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
    public void onCreate(Bundle savedInstanceState)
    {
        super.onCreate(savedInstanceState);
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

        subjects = new ArrayList<>();
        RecyclerView recyclerView = view.findViewById(R.id.rvSubjectsList);
        GridLayoutManager gridLayoutManager = new GridLayoutManager(view.getContext(), 2);
        recyclerView.setLayoutManager(gridLayoutManager);
        subjectsListAdapter = new SubjectsListAdapter(this, subjects);
        recyclerView.setAdapter(subjectsListAdapter);

        DbService.getInstance().getSubjects((DbRequestListener<List<Subject>>) subjects ->
        {
            SubjectsListFragment.this.subjects.addAll(subjects);
            subjectsListAdapter.notifyDataSetChanged();
            DbService.getInstance().updateDb(result ->
                    DbService.getInstance().getSubjects((DbRequestListener<List<Subject>>) subjects1 ->
            {
                SubjectsListFragment.this.subjects.clear();
                SubjectsListFragment.this.subjects.addAll(subjects1);
                subjectsListAdapter.notifyDataSetChanged();
            }));
        });
    }

    @Override
    public void onAttach(@NonNull Context context)
    {
        super.onAttach(context);
        mListener = (Listener)getActivity();
    }

    @Override
    public void onDetach()
    {
        super.onDetach();
        mListener = null;
    }

    @Override
    public void onResponse(BaseResponse response)
    {
        super.onResponse(response);
    }

    @Override
    public void OnViewHolderClick(int position, Subject subject)
    {
        mListener.onSubjectsListFragmentInteraction(subject);
    }

    interface Listener
    {
        void onSubjectsListFragmentInteraction(Subject subject);
    }
}
