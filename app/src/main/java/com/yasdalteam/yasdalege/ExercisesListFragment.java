package com.yasdalteam.yasdalege;

import android.content.Context;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.LinearLayout;
import android.widget.Spinner;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.coordinatorlayout.widget.CoordinatorLayout;
import androidx.fragment.app.FragmentManager;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.yasdalteam.yasdalege.Networking.BaseResponse;
import com.yasdalteam.yasdalege.Networking.ExerciseResponse;

import butterknife.BindView;
import butterknife.ButterKnife;

import java.lang.reflect.Array;
import java.util.ArrayList;
import java.util.List;
import java.util.Objects;
import java.util.function.Consumer;
import java.util.zip.Inflater;

public class ExercisesListFragment extends BaseFragment implements ExercisesListAdapter.Listener, User.IUserObservable, Spinner.OnItemSelectedListener
{
    @BindView(R.id.spnExerciseListSubjectFilter)
    Spinner subjectFilterSpinner;

    @BindView(R.id.llNoContentStub)
    LinearLayout rlNoContentStub;


    public int getItemsPerLoad()
    {
        return itemsPerLoad;
    }
    private final int itemsPerLoad = 99999999;

    public int getPage()
    {
        return page;
    }
    private int page;

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
    private List<Exercise> listData = new ArrayList<>();

    public void setSpinnerModels(List<SubjectSpinnerModel> spinnerModels) {
        this.spinnerModels = spinnerModels;
    }
    private List<SubjectSpinnerModel> spinnerModels = new ArrayList<>();

    public ExercisesListAdapter exercisesListAdapter;

    private @Nullable Subject selectedSubject;

    public ExercisesListFragment() {}

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
        exercisesListAdapter = new ExercisesListAdapter(this, listData);
        App.shared().getUser().addObserver(this);

        if (request != null)
        {
            Loader.show();
            request.send(this);
        }
    }

    @Override
    public void onStart()
    {
        super.onStart();

        checkIfUserAuthorized();
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

        ButterKnife.bind(this, view);

        RecyclerView recyclerView = view.findViewById(R.id.rvExercisesList);
        LinearLayoutManager linearLayoutManager = new MeasurableLinearLayoutManager(view.getContext());
        recyclerView.setLayoutManager(linearLayoutManager);
        recyclerView.setAdapter(exercisesListAdapter);

        setupSpinner();

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
                    Loader.show();
                    request.send(ExercisesListFragment.this);
                    exercisesListAdapter.setDataIsLoading(true);
                }
            }
        });

        rlNoContentStub.setVisibility(View.INVISIBLE);
    }

    private void setupSpinner()
    {
        if (getTag().equals("completedExercises") || getTag().equals("favoriteExercises")) {
            subjectFilterSpinner.setVisibility(View.VISIBLE);
        } else {
            subjectFilterSpinner.setVisibility(View.GONE);
        }

        List<Subject> cachedSubjects = App.shared().getSubjects();
        List<String> spinnerItems = new ArrayList<>();
        for (int i = 0; i < cachedSubjects.size(); i++)
        {
            if (cachedSubjects.get(i).isActive())
            {
                spinnerModels.add(new SubjectSpinnerItemModel(cachedSubjects.get(i)));
                spinnerItems.add(spinnerModels.get(spinnerModels.size() - 1).getTitle());
            }
        }
        spinnerModels.add(0, new SubjectSpinnerModel("Все", SubjectSpinnerModel.SubjectSpinnerModelType.ALL));
        spinnerItems.add(0, spinnerModels.get(0).getTitle());

        ArrayAdapter<String> adapter = new ArrayAdapter<String>(
                Objects.requireNonNull(this.getContext()), android.R.layout.simple_spinner_item, spinnerItems);
        adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        subjectFilterSpinner.setAdapter(adapter);
        subjectFilterSpinner.setOnItemSelectedListener(this);
    }

    @Override
    public void onItemSelected(AdapterView<?> adapterView, View view, int i, long l)
    {
        SubjectSpinnerModel selectedModel = spinnerModels.get(i);
        switch (selectedModel.getType())
        {
            case ALL:
                selectedSubject = null;
                break;
            case ITEM:
                if (selectedModel instanceof SubjectSpinnerItemModel)
                {
                    SubjectSpinnerItemModel selectedItemModel = (SubjectSpinnerItemModel)selectedModel;
                    selectedSubject = selectedItemModel.getSubject();
                }
        }
        filterExercisesBySelectedSubject();
        exercisesListAdapter.notifyDataSetChanged();
    }

    @Override
    public void onNothingSelected(AdapterView<?> adapterView) {}

    private void filterExercisesBySelectedSubject()
    {
        listData.clear();
        if (selectedSubject == null)
        {
            listData.addAll(exercises);
        }
        else
        {
            for (Exercise exercise: exercises)
            {
                if (exercise.getSubjectId() == selectedSubject.getId())
                {
                    listData.add(exercise);
                }
            }
        }

        if (listData.isEmpty())
        {
            rlNoContentStub.setVisibility(View.VISIBLE);
        }
        else
        {
            rlNoContentStub.setVisibility(View.INVISIBLE);
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
        checkIfUserAuthorized();
        exercisesListAdapter.notifyDataSetChanged();
    }

    @Override
    public void OnViewHolderClick(RecyclerView.ViewHolder viewHolder) {}

    @Override
    public void onResponse(BaseResponse response)
    {
        super.onResponse(response);

        Loader.hide();
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
        exercises.clear();
        listData.clear();
        exercises.addAll(exerciseResponse.getData());
        listData.addAll(exercises);
        filterExercisesBySelectedSubject();

        exercisesListAdapter.notifyDataSetChanged();
    }

    @Override
    public void onError(String error)
    {
        super.onError(error);

        Loader.hide();
        if (error.equals("404") && page == 0)
        {
            rlNoContentStub.setVisibility(View.VISIBLE);
        }
    }

    private void checkIfUserAuthorized()
    {
        if (!App.shared().getUser().isAuthorized() && getTag() != null)
        {
            if (getTag().equals("completedExercises") || getTag().equals("favoriteExercises"))
            {
                FragmentManager fragmentManager = App.shared().getCurrentFragment().getActivity().getSupportFragmentManager();
                fragmentManager.popBackStack();
            }
        }
    }
}