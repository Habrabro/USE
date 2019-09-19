package com.example.use;

import android.content.Context;
import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.net.Uri;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.recyclerview.widget.RecyclerView;

import com.bumptech.glide.Glide;

import java.util.ArrayList;
import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;

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

        RecyclerView recyclerView = view.findViewById(R.id.rvList);
        exercisesListAdapter = new ExercisesListAdapter(this, exercises);
        recyclerView.setAdapter(exercisesListAdapter);

        NetworkService networkService = NetworkService.getInstance(this);
        networkService.getExercises(false);
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
    public void onFailure()
    {

    }
    @Override
    public void onError()
    {

    }
    @Override
    public void onDisconnected()
    {

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
