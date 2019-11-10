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
import com.example.use.Networking.ExerciseResponse;
import com.example.use.Networking.IResponseReceivable;
import com.example.use.Networking.NetworkService;
import com.example.use.database.DbRequestListener;
import com.example.use.database.DbService;

import java.util.ArrayList;
import java.util.List;

public class VariantFragment extends BaseFragment implements ExercisesListAdapter.Listener
{
    private final static String PARAM_1 = "param_1";
    private long subjectId;

    private ExercisesListAdapter exercisesListAdapter;

    private List<Topic> topics;
    private List<Exercise> exercises;

    public VariantFragment()
    {

    }

    public static VariantFragment newInstance(long subjectId)
    {
        Bundle bundle = new Bundle();
        bundle.putLong(PARAM_1, subjectId);
        VariantFragment fragment = new VariantFragment();
        fragment.setArguments(bundle);
        return fragment;
    }

    @Override
    public void onCreate(Bundle savedInstanceState)
    {
        super.onCreate(savedInstanceState);
        if (getArguments() != null)
        {
            subjectId = getArguments().getLong(PARAM_1);
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
        topics = new ArrayList<>();

        RecyclerView recyclerView = view.findViewById(R.id.rvExercisesList);
        LinearLayoutManager linearLayoutManager = new LinearLayoutManager(view.getContext());
        recyclerView.setLayoutManager(linearLayoutManager);

        exercisesListAdapter = new ExercisesListAdapter(this, exercises);
        recyclerView.setAdapter(exercisesListAdapter);

        DbService.getInstance().getTopics(subjectId, (DbRequestListener<List<Topic>>) topics ->
        {
            Int i = new Int(0);
            VariantFragment.this.topics.addAll(topics);
            NetworkService networkService = NetworkService.getInstance(new IResponseReceivable()
            {
                @Override
                public void onResponse(BaseResponse response)
                {
                    Exercise exercise = ((ExerciseResponse)response).getData().get(0);
                    exercises.add(exercise);
                    exercisesListAdapter.notifyDataSetChanged();

                    if (exercise.getTopicId() == topics.get(0).getId())
                    {
                        ((MainActivity)getActivity()).onLoad();
                    }
                    if (exercise.getTopicId() == topics.get(topics.size() - 1).getId())
                    {
                        ((MainActivity)getActivity()).onLoaded();
                    }

                    if (i.getValue() < topics.size() - 1)
                    {
                        i.inc();
                        NetworkService.getInstance(this).getRandomExercise(
                                App.getInstance().getUser().getSessionId(), topics.get(i.getValue()).getId(), false);
                    }
                }
                @Override
                public void onFailure(Throwable t) { }
                @Override
                public void onError(String error) { }
                @Override
                public void onDisconnected() { }
            });
            networkService.getRandomExercise(
                    App.getInstance().getUser().getSessionId(), topics.get(i.getValue()).getId(), false);
        });
    }

    public void OnViewHolderClick(int position)
    {

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
    }
}
