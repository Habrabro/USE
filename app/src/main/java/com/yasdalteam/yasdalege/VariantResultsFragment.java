package com.yasdalteam.yasdalege;

import android.content.Context;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.LinearLayout;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AlertDialog;
import androidx.fragment.app.FragmentManager;

import com.yasdalteam.yasdalege.Networking.BaseResponse;

import java.util.ArrayList;
import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;

public class VariantResultsFragment extends BaseFragment
{
    private List<Exercise> exercises = new ArrayList<>();

    @BindView(R.id.btnCloseResults)
    Button btnCloseResults;

    public VariantResultsFragment()
    {

    }

    public static VariantResultsFragment newInstance(List<Exercise> exercises)
    {
        VariantResultsFragment fragment = new VariantResultsFragment();
        fragment.exercises.addAll(exercises);
        fragment.exercises.remove(exercises.size() - 1);
        return fragment;
    }

    @OnClick(R.id.btnCloseResults)
    public void onBtnCloseResultsClick()
    {
        AlertDialog.Builder builder = new AlertDialog.Builder(getActivity());
        builder.setTitle("Вы действительно хотите закрыть вариант?");
        builder.setPositiveButton("OK", (dialog, id) ->
        {
            FragmentManager fm = getActivity().getSupportFragmentManager();
            for(int i = 0; i < fm.getBackStackEntryCount(); ++i) {
                fm.popBackStack();
            }
        });
        builder.setNegativeButton("Отмена", (dialog, id) ->
        {
            // User cancelled the dialog
        });
        AlertDialog dialog = builder.create();
        dialog.show();
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
        View view = inflater.inflate(R.layout.variant_results_fragment_layout, container, false);
        return view;
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState)
    {
        super.onViewCreated(view, savedInstanceState);

        ButterKnife.bind(this, view);

        int pointsSummary = 0;

        LayoutInflater inflater = LayoutInflater.from(getContext());
        LinearLayout tableView = view.findViewById(R.id.llResultsTable);
        TextView pointsSummaryCell = view.findViewById(R.id.tvPointsSummaryValueCell);

        for (Exercise exercise : exercises)
        {
            if (!exercise.getRightAnswer().equals(ExercisesListAdapter.EXERCISE_WITH_REQUEST_SECTION_KEY))
            {
                View rowView = inflater.inflate(R.layout.results_table_row, null);
                tableView.addView(rowView);

                TextView numberCol = rowView.findViewById(R.id.tvNumberCol);
                TextView rightAnswerCol = rowView.findViewById(R.id.tvRightAnswerCol);
                TextView answerCol = rowView.findViewById(R.id.tvAnswerCol);
                TextView pointsCol = rowView.findViewById(R.id.tvPointsCol);

                numberCol.setText(Integer.toString(exercise.getNumber()));
                rightAnswerCol.setText(exercise.getRightAnswer());
                answerCol.setText(exercise.getAnswer());
                if (exercise.isAnsweredRight()) {
                    pointsSummary += exercise.getPoints();
                    pointsCol.setText(Integer.toString(exercise.getPoints()));
                } else {
                    pointsCol.setText("0");
                }
            }
        }

        pointsSummaryCell.setText(Integer.toString(pointsSummary));
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
