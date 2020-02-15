package com.yasdalteam.yasdalege;

import androidx.recyclerview.widget.RecyclerView;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import java.util.List;

public class ExercisesListAdapter extends RecyclerView.Adapter<RecyclerView.ViewHolder>
{
    public final static String EXERCISE_WITH_REQUEST_SECTION_KEY = "z";

    private LayoutInflater inflater;
    private List<Exercise> exercises;
    private Listener listener;
    private boolean dataIsLoading = true;
    private boolean allDataLoaded = false;
    private long number;
    private int fixedSize = -1;

    public boolean isDataIsLoading()
    {
        return dataIsLoading;
    }

    public void setDataIsLoading(boolean dataIsLoading)
    {
        this.dataIsLoading = dataIsLoading;
    }

    public boolean isAllDataLoaded()
    {
        return allDataLoaded;
    }

    public void setAllDataLoaded(boolean allDataLoaded)
    {
        this.allDataLoaded = allDataLoaded;
    }

    ExercisesListAdapter(Listener listener, List<Exercise> exercises)
    {
        this.exercises = exercises;
        if (listener instanceof Listener)
        {
            this.listener = listener;
        }
    }
    @Override
    public RecyclerView.ViewHolder onCreateViewHolder(ViewGroup parent, int viewType)
    {
        View view;
        inflater = LayoutInflater.from(parent.getContext());
        ViewHolderTypes type = ViewHolderTypes.values()[viewType];
        switch (type)
        {
            case TEST_SECTION_EXERCISE:
                view = inflater.inflate(R.layout.answer_section_layout, parent, false);
                return new AnswerSection(view, listener);
            case EXERCISE_WITH_REQUEST_SECTION:
                view = inflater.inflate(R.layout.request_form, parent, false);
                return new RequestForm((BaseFragment)listener, view, listener);
            case COMPLETE_BUTTON:
                view = inflater.inflate(R.layout.variant_fragment_last_item, parent, false);
                return new CompleteVariantViewHolder(view);
            default:
                return null;
        }
    }

    @Override
    public void onBindViewHolder(RecyclerView.ViewHolder holder, int position)
    {
        Exercise exercise = exercises.get(position);
        ViewHolderTypes type = ViewHolderTypes.values()[holder.getItemViewType()];
        switch (type)
        {
            case TEST_SECTION_EXERCISE:
                ((AnswerSection)holder).bindExercise(exercise);
                break;
            case EXERCISE_WITH_REQUEST_SECTION:
                ((RequestForm)holder).bindExercise(exercise);
                break;
            case COMPLETE_BUTTON:
                break;
        }
    }

    @Override
    public int getItemViewType(int position)
    {
        if (exercises.get(position).getId() == -1)
        {
            return ViewHolderTypes.COMPLETE_BUTTON.getType();
        }
        else if (exercises.get(position).getRightAnswer().equals(EXERCISE_WITH_REQUEST_SECTION_KEY))
        {
            return ViewHolderTypes.EXERCISE_WITH_REQUEST_SECTION.getType();
        }
        else
        {
            return ViewHolderTypes.TEST_SECTION_EXERCISE.getType();
        }
    }

    @Override
    public int getItemCount()
    {
        return exercises.size();
    }

    public interface Listener
    {
        void OnViewHolderClick(RecyclerView.ViewHolder viewHolder);
    }

    class CompleteVariantViewHolder extends RecyclerView.ViewHolder implements View.OnClickListener
    {
        public CompleteVariantViewHolder(View view)
        {
            super(view);
            view.findViewById(R.id.btnCompleteVariant).setOnClickListener(this);
        }

        @Override
        public void onClick(View view)
        {
            listener.OnViewHolderClick(this);
        }
    }

    public enum ViewHolderTypes
    {
        COMPLETE_BUTTON(0),
        TEST_SECTION_EXERCISE(1),
        EXERCISE_WITH_REQUEST_SECTION(2),
        ADS_BLOCK(3);

        public int getType()
        {
            return type;
        }

        private int type;

        ViewHolderTypes(int type)
        {
            this.type = type;
        }
    }
}
