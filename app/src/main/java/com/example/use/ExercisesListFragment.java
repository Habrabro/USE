package com.example.use;

import android.content.Context;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.example.use.Networking.BaseResponse;
import com.example.use.Networking.ExerciseResponse;
import com.example.use.Networking.Exercise;
import com.example.use.Networking.NetworkService;
import com.google.android.material.snackbar.Snackbar;

import java.util.ArrayList;
import java.util.List;

public class ExercisesListFragment extends BaseFragment implements ExercisesListAdapter.Listener
{
    private final static String PARAM_1 = "param_1";
    private final static String PARAM_2 = "param_2";

    private final int itemsPerLoad = 30;
    private int page = 0;

    private long topicId, number;

    private Listener mListener;
    private ExercisesListAdapter exercisesListAdapter;

    private List<Exercise> exercises;

    private Snackbar snackbar;

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

        exercises = new ArrayList<>();
        snackbar = Snackbar.make(getView(), "Loading", Snackbar.LENGTH_INDEFINITE);

        RecyclerView recyclerView = view.findViewById(R.id.rvExercisesList);
        LinearLayoutManager linearLayoutManager = new LinearLayoutManager(view.getContext());
        recyclerView.setLayoutManager(linearLayoutManager);
        exercisesListAdapter = new ExercisesListAdapter(this, exercises, number);
        recyclerView.setAdapter(exercisesListAdapter);

        NetworkService networkService = NetworkService.getInstance(this);
        networkService.getExercises(null, topicId, page + "," + itemsPerLoad, true);

        recyclerView.addOnScrollListener(new RecyclerView.OnScrollListener()
        {
            @Override
            public void onScrollStateChanged(RecyclerView recyclerView, int newState)
            {
                super.onScrollStateChanged(recyclerView, newState);

                if (!recyclerView.canScrollVertically(1)
                        && !exercisesListAdapter.isDataIsLoading()
                        && !exercisesListAdapter.isAllDataLoaded())
                {
                    networkService.getExercises(
                            null, topicId, page * itemsPerLoad + "," + itemsPerLoad, true);
                    exercisesListAdapter.setDataIsLoading(true);
                    snackbar.show();
                }
            }
        });
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
        if (!exercisesListAdapter.isDataIsLoading())
        {
            exercises.clear();
        }
        else
        {
            page++;
            exercisesListAdapter.setDataIsLoading(false);
        }
        if (snackbar != null) { snackbar.dismiss(); }
        ExerciseResponse exerciseResponse = (ExerciseResponse)response;
        if (exerciseResponse.getData().size() == 0)
        {
            exercisesListAdapter.setAllDataLoaded(true);
        }
        exercises.addAll(exerciseResponse.getData());
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
