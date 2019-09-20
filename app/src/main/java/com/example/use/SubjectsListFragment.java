package com.example.use;

import android.content.Context;
import android.net.Uri;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.recyclerview.widget.GridLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.example.use.Networking.BaseResponse;
import com.example.use.Networking.Exercise;
import com.example.use.Networking.Subject;
import com.example.use.Networking.SubjectDatum;
import com.example.use.Networking.NetworkService;

import java.util.ArrayList;
import java.util.List;

public class SubjectsListFragment extends BaseFragment implements SubjectsListAdapter.Listener
{
    private OnFragmentInteractionListener mListener;
    private SubjectsListAdapter subjectsListAdapter;

    private List<SubjectDatum> subjects = new ArrayList<>();

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

        RecyclerView recyclerView = view.findViewById(R.id.rvSubjectsList);
        GridLayoutManager gridLayoutManager = new GridLayoutManager(view.getContext(), 2);
        recyclerView.setLayoutManager(gridLayoutManager);
        subjectsListAdapter = new SubjectsListAdapter(this, subjects);
        recyclerView.setAdapter(subjectsListAdapter);

        NetworkService networkService = NetworkService.getInstance(this);
        networkService.getSubjects(false);
    }

    @Override
    public void onAttach(@NonNull Context context)
    {
        super.onAttach(context);
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
        Subject subject = (Subject)response;
        subjects.addAll(subject.getData());
        subjectsListAdapter.notifyDataSetChanged();
    }

    @Override
    public void OnViewHolderClick(int position)
    {

    }

    public interface OnFragmentInteractionListener
    {
        void onFragmentInteraction(Uri uri);
    }
}
