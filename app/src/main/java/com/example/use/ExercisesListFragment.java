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
    public int getItemsPerLoad()
    {
        return itemsPerLoad;
    }

    private final int itemsPerLoad = 30;

    public int getPage()
    {
        return page;
    }

    private int page;

    private ExercisesListAdapter exercisesListAdapter;

    public void setRequest(IRequestSendable request)
    {
        this.request = request;
    }

    private IRequestSendable request;
    private List<Exercise> exercises;

    public ExercisesListFragment()
    {

    }

    public static ExercisesListFragment newInstance(IRequestSendable request)
    {
        ExercisesListFragment fragment = new ExercisesListFragment();
        fragment.setRequest(request);
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

        exercises = new ArrayList<>();
        page = 0;

        RecyclerView recyclerView = view.findViewById(R.id.rvExercisesList);
        LinearLayoutManager linearLayoutManager = new LinearLayoutManager(view.getContext());
        recyclerView.setLayoutManager(linearLayoutManager);
        exercisesListAdapter = new ExercisesListAdapter(this, exercises);
        recyclerView.setAdapter(exercisesListAdapter);

        request.send(this);

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
                    request.send(ExercisesListFragment.this);
                    exercisesListAdapter.setDataIsLoading(true);
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
    }

    @Override
    public void onResponse(BaseResponse response)
    {
        super.onResponse(response);
        if (!exercisesListAdapter.isDataIsLoading())
        {
            exercises.clear();
        }
        else
        {
            page++;
            exercisesListAdapter.setDataIsLoading(false);
        }
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
}