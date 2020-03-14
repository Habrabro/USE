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
import com.yasdalteam.yasdalege.Networking.ResponseHandler;
import com.yasdalteam.yasdalege.Networking.TopicResponse;
import com.yasdalteam.yasdalege.database.DbRequestListener;
import com.yasdalteam.yasdalege.database.DbService;

import java.util.ArrayList;
import java.util.List;

public class VariantFragment extends BaseFragment implements ExercisesListAdapter.Listener, User.IUserObservable
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

        App.shared().getUser().addObserver(this);
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
            Loader.show();
            NetworkService.getInstance(new ResponseHandler() {
                @Override
                public void onResponse(BaseResponse response)
                {
                    super.onResponse(response);
                    List<Topic> topics = ((TopicResponse)response).getData();
                    VariantFragment.this.topics.addAll(topics);
                    generate();
                }
            }).getTopics(null, subjectId);
            created = true;
        }
    }

    @Override
    public void onAuthorize(User user)
    {
        exercisesListAdapter.notifyDataSetChanged();
    }

    @Override
    public void onLogout()
    {
        exercisesListAdapter.notifyDataSetChanged();
    }

    private void generate()
    {
        Int i = new Int(0);
        NetworkService networkService = NetworkService.getInstance(new ResponseHandler()
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
//                        Loader.show();
                    }
                    if (exercise.getTopicId() == topics.get(topics.size() - 1).getId())
                    {
                        Loader.hide();
                        Exercise completeButton = new Exercise();
                        completeButton.setId(-1);
                        exercises.add(completeButton);
                    }
                }

                if (i.getValue() < topics.size() - 1)
                {
                    i.inc();
                    NetworkService.getInstance(this).getRandomExercise(
                            App.shared().getUser().getSessionId(), topics.get(i.getValue()).getId(), false);
                }
            }
        });
        if (networkService.isNetworkConnected())
        {
            networkService.getRandomExercise(
                    App.shared().getUser().getSessionId(), topics.get(i.getValue()).getId(), false);
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
