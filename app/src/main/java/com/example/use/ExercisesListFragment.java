package com.example.use;

import android.content.Context;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.example.use.Networking.BaseResponse;
import com.example.use.Networking.Exercise;
import com.example.use.Networking.ExerciseDatum;
import com.example.use.Networking.NetworkService;

import java.util.ArrayList;
import java.util.List;

public class ExercisesListFragment extends BaseFragment implements ExercisesListAdapter.Listener
{
    private final static String PARAM_1 = "param_1";
    private final static String PARAM_2 = "param_2";
    private long topicId, number;

    private Listener mListener;
    private ExercisesListAdapter exercisesListAdapter;

    private List<ExerciseDatum> exercises = new ArrayList<>();

    public ExercisesListFragment()
    {

    }

    public static ExercisesListFragment newInstance(long topicId, long number)
    {
        Bundle bundle = new Bundle();
        bundle.putLong(PARAM_1, topicId);
        bundle.putLong(PARAM_2, number);
        ExercisesListFragment fragment = new ExercisesListFragment();
        fragment.setArguments(bundle);
        return fragment;
    }

    @Override
    public void onCreate(Bundle savedInstanceState)
    {
        super.onCreate(savedInstanceState);
        if (getArguments() != null)
        {
            topicId = getArguments().getLong(PARAM_1);
            number = getArguments().getLong(PARAM_2);
        }
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
        exercisesListAdapter = new ExercisesListAdapter(this, exercises, number);
        recyclerView.setAdapter(exercisesListAdapter);

        NetworkService networkService = NetworkService.getInstance(this);
        networkService.getExercises(topicId, true);
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

    public interface Listener
    {
        void onFragmentInteraction();
    }
}
