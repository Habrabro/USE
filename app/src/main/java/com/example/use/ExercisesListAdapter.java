package com.example.use;

import androidx.recyclerview.widget.RecyclerView;

import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;

import com.bumptech.glide.Glide;
import com.example.use.Networking.ExerciseDatum;

import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;

public class ExercisesListAdapter extends RecyclerView.Adapter<ExercisesListAdapter.ViewHolder>
{
    private LayoutInflater inflater;
    private List<ExerciseDatum> exercises;
    private Listener listener;

    ExercisesListAdapter(Listener listener, List<ExerciseDatum> exercises)
    {
        this.exercises = exercises;
        if (listener instanceof Listener)
        {
            this.listener = listener;
        }
    }
    @Override
    public ExercisesListAdapter.ViewHolder onCreateViewHolder(ViewGroup parent, int viewType)
    {
        inflater = LayoutInflater.from(parent.getContext());
        View view = inflater.inflate(R.layout.fragment_exercises_list_item, parent, false);
        return new ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(ExercisesListAdapter.ViewHolder holder, int position)
    {
        ExerciseDatum exercise = exercises.get(position);
        holder.idView.setText(Long.toString(exercise.getId()));
        holder.descriptionView.setText(exercise.getDescription());
        Glide
                .with(App.getInstance())
                .load(exercise.getImg())
                .placeholder(new ColorDrawable(Color.GREEN))
                .error(new ColorDrawable(Color.RED))
                .fallback(new ColorDrawable(Color.GRAY))
                .into(holder.imageView);
        holder.rightAnswerView.setText(exercise.getRightAnswer());
    }

    @Override
    public int getItemCount()
    {
        return exercises.size();
    }

    public interface Listener
    {
        void OnViewHolderClick(int position);
    }

    class ViewHolder extends RecyclerView.ViewHolder implements View.OnClickListener
    {
        @BindView(R.id.tvId) TextView idView;
        @BindView(R.id.tvDescription) TextView descriptionView;
        @BindView(R.id.ivImage) ImageView imageView;
        @BindView(R.id.etAnswerField) EditText answerFieldView;
        @BindView(R.id.tvRightAnswer) TextView rightAnswerView;

        ViewHolder(View view)
        {
            super(view);
            ButterKnife.bind(this, view);
            view.setOnClickListener(this);
        }

        @Override
        public void onClick(View view)
        {
            listener.OnViewHolderClick(getAdapterPosition());
        }
    }
}
