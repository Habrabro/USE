package com.example.use;

import android.content.Context;
import android.graphics.Bitmap;
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

import retrofit2.Response;

public class ExerciseFragment extends Fragment
{
    private OnFragmentInteractionListener mListener;

    private TextView idView, descriptionView;
    private EditText answerFieldView;
    private ImageView imageView;

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
        return view;
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState)
    {
        super.onViewCreated(view, savedInstanceState);

        idView = view.findViewById(R.id.tvId);
        descriptionView = view.findViewById(R.id.tvDescription);
        imageView = view.findViewById(R.id.ivImage);
        answerFieldView = view.findViewById(R.id.etAnswerField);

        NetworkService networkService = NetworkService.getInstance(this);
        networkService.getRequests(0, false);
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

    public void onDataReceived(Response<Exercise> response)
    {
        Exercise exercise = response.body();
        idView.setText(Long.toString(exercise.getId()));
        descriptionView.setText(exercise.getDescription());
        UrlToBitmap urlToBitmap = new UrlToBitmap(new OnBitmapTaskExecutedListener()
        {
            @Override
            public void onBitmapTaskExecuted(Bitmap result)
            {
                imageView.setImageBitmap(result);
            }
        });
        urlToBitmap.execute(
                "http://storage.mds.yandex.net/get-mturk/1136717/a2a954b4-5ec8-4dac-9411-eb5768f80a33");
        Log.i("Tag", exercise.getDescription());
    }

    public interface OnFragmentInteractionListener
    {
        void onFragmentInteraction(Uri uri);
    }
}
