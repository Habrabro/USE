package com.yasdalteam.yasdalege;

import android.content.Context;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.yasdalteam.yasdalege.Networking.BaseResponse;
import com.yasdalteam.yasdalege.Networking.ExerciseResponse;
import com.yasdalteam.yasdalege.Networking.IResponseReceivable;
import com.yasdalteam.yasdalege.Networking.NetworkService;
import com.yasdalteam.yasdalege.database.DbRequestListener;
import com.yasdalteam.yasdalege.database.DbService;

import java.util.ArrayList;
import java.util.List;

public class VariantFragment extends BaseFragment implements ExercisesListAdapter.Listener
{
    private final static String PARAM_1 = "param_1";
    private long subjectId;

    private boolean created = false;

    public ExercisesListAdapter exercisesListAdapter;

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
        exercises = new ArrayList<>();
        topics = new ArrayList<>();
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

        RecyclerView recyclerView = view.findViewById(R.id.rvExercisesList);
        LinearLayoutManager linearLayoutManager = new LinearLayoutManager(view.getContext());
        recyclerView.setLayoutManager(linearLayoutManager);

        exercisesListAdapter = new ExercisesListAdapter(this, exercises);

        recyclerView.setAdapter(exercisesListAdapter);

        if (!created)
        {
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
                        if (exercise != null)
                        {
                            exercises.add(exercise);
                            exercisesListAdapter.notifyDataSetChanged();

                            if (exercise.getTopicId() == topics.get(0).getId())
                            {
                                if (getActivity() != null)
                                {
                                    ((MainActivity)VariantFragment.this.getActivity()).onLoad();
                                }
                            }
                            if (exercise.getTopicId() == topics.get(topics.size() - 1).getId())
                            {
                                if (getActivity() != null)
                                {
                                    ((MainActivity)VariantFragment.this.getActivity()).onLoaded();
                                }
                                Exercise completeButton = new Exercise();
                                completeButton.setId(-1);
                                exercises.add(completeButton);
                            }
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
                if (networkService.isNetworkConnected())
                {
                    networkService.getRandomExercise(
                            App.getInstance().getUser().getSessionId(), topics.get(i.getValue()).getId(), false);
                }
            });
            created = true;
        }
    }

    public void OnViewHolderClick(RecyclerView.ViewHolder viewHolder)
    {
        int vhIntType = viewHolder.getItemViewType();
        ExercisesListAdapter.ViewHolderTypes type = ExercisesListAdapter.ViewHolderTypes.values()[vhIntType];
        switch (type)
        {
            case COMPLETE_BUTTON:
                VariantResultsFragment fragment = VariantResultsFragment.newInstance(exercises);
                ((MainActivity)getActivity()).replaceFragment(fragment, "variantResultsFragment");
                break;
        }
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
