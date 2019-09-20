package com.example.use;

import android.content.Context;
import android.net.Uri;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.GridLayoutManager;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.example.use.Networking.BaseResponse;
import com.example.use.Networking.Exercise;
import com.example.use.Networking.ExerciseDatum;
import com.example.use.Networking.IResponseReceivable;
import com.example.use.Networking.NetworkService;

import java.util.ArrayList;
import java.util.List;

public class ExercisesListFragment extends BaseFragment implements ExercisesListAdapter.Listener
{
    private OnFragmentInteractionListener mListener;
    private ExercisesListAdapter exercisesListAdapter;

    private List<ExerciseDatum> exercises = new ArrayList<>();

    public ExercisesListFragment()
    {

    }

    public static ExercisesListFragment newInstance()
    {
        ExercisesListFragment fragment = new ExercisesListFragment();
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
        View view = inflater.inflate(R.layout.fragment_exercises_list, container, false);
        return view;
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState)
    {
        super.onViewCreated(view, savedInstanceState);

        RecyclerView recyclerView = view.findViewById(R.id.rvExercisesList);
        LinearLayoutManager linearLayoutManager = new LinearLayoutManager(view.getContext());
        recyclerView.setLayoutManager(linearLayoutManager);
        exercisesListAdapter = new ExercisesListAdapter(this, exercises);
        recyclerView.setAdapter(exercisesListAdapter);

        NetworkService networkService = NetworkService.getInstance(this);
        networkService.getExercises(4, false);
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
        Exercise exercise = (Exercise)response;
        exercises.addAll(exercise.getData());
        exercisesListAdapter.notifyDataSetChanged();
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
