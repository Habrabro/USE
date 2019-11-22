package com.example.use;

import android.content.ClipData;
import android.content.Context;
import android.content.Intent;
import android.icu.text.Transliterator;
import android.net.Uri;
import android.os.Bundle;
import android.provider.ContactsContract;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.core.text.ICUCompat;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.example.use.Networking.BaseResponse;
import com.example.use.Networking.ExerciseResponse;
import com.example.use.Networking.IResponseReceivable;
import com.example.use.Networking.NetworkService;

import java.io.File;
import java.net.URI;
import java.util.ArrayList;
import java.util.List;

import okhttp3.MediaType;
import okhttp3.MultipartBody;
import okhttp3.RequestBody;
import retrofit2.Call;

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

    public ExercisesListAdapter exercisesListAdapter;

    public void setRequest(IRequestSendable request)
    {
        this.request = request;
    }

    private IRequestSendable request;

    public void setExercises(List<Exercise> exercises)
    {
        this.exercises = exercises;
    }

    private List<Exercise> exercises = new ArrayList<>();

    public ExercisesListFragment()
    {

    }

    public static ExercisesListFragment newInstance(IRequestSendable request)
    {
        ExercisesListFragment fragment = new ExercisesListFragment();
        fragment.setRequest(request);
        return fragment;
    }

    public static ExercisesListFragment newInstance(List<Exercise> exercises)
    {
        ExercisesListFragment fragment = new ExercisesListFragment();
        fragment.setExercises(exercises);
        return fragment;
    }

    @Override
    public void onCreate(Bundle savedInstanceState)
    {
        super.onCreate(savedInstanceState);

        page = 0;
        exercisesListAdapter = new ExercisesListAdapter(this, exercises);

        if (request != null)
        {
            request.send(this);
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
        recyclerView.setAdapter(exercisesListAdapter);

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
    public void OnViewHolderClick(RecyclerView.ViewHolder viewHolder)
    {

    }
}