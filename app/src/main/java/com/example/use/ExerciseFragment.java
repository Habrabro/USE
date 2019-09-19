package com.example.use;

import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.graphics.drawable.Drawable;
import android.net.Uri;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;

import com.bumptech.glide.Glide;

import butterknife.BindView;
import butterknife.ButterKnife;
import retrofit2.Response;

public class ExerciseFragment extends BaseFragment
{
    private OnFragmentInteractionListener mListener;

    @BindView(R.id.tvId) TextView idView;
    @BindView(R.id.tvDescription) TextView descriptionView;
    @BindView(R.id.ivImage) ImageView imageView;
    @BindView(R.id.etAnswerField) EditText answerFieldView;

    public ExerciseFragment()
    {

    }

    public static ExerciseFragment newInstance()
    {
        ExerciseFragment fragment = new ExerciseFragment();
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
        View view = inflater.inflate(R.layout.fragment_exercise, container, false);
        ButterKnife.bind(this, view);
        return view;
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState)
    {
        super.onViewCreated(view, savedInstanceState);

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
        idView.setText(Long.toString(exercise.getData().get(0).getId()));
        descriptionView.setText(exercise.getData().get(0).getDescription());
        Glide
                .with(this)
                .load(exercise.getData().get(0).getImg())
                .placeholder(new ColorDrawable(Color.GREEN))
                .error(new ColorDrawable(Color.RED))
                .fallback(new ColorDrawable(Color.GRAY))
                .into(imageView);
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

    public interface OnFragmentInteractionListener
    {
        void onFragmentInteraction(Uri uri);
    }
}
