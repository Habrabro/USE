package com.example.use;

import androidx.recyclerview.widget.RecyclerView;

import android.graphics.drawable.ColorDrawable;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;

import com.bumptech.glide.Glide;
import com.bumptech.glide.load.engine.DiskCacheStrategy;
import com.example.use.Networking.BaseResponse;
import com.example.use.Networking.Exercise;
import com.example.use.Networking.IResponseReceivable;
import com.example.use.Networking.NetworkService;
import com.example.use.Networking.Topic;
import com.example.use.database.DbRequestListener;
import com.example.use.database.DbService;
import com.google.android.material.snackbar.Snackbar;

import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;

public class ExercisesListAdapter extends RecyclerView.Adapter<ExercisesListAdapter.ViewHolder>
{
    private LayoutInflater inflater;
    private List<Exercise> exercises;
    private Listener listener;
    private boolean dataIsLoading = true;
    private boolean allDataLoaded = false;
    private long number;

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
    public ExercisesListAdapter.ViewHolder onCreateViewHolder(ViewGroup parent, int viewType)
    {
        inflater = LayoutInflater.from(parent.getContext());
        View view = inflater.inflate(R.layout.fragment_exercises_list_item, parent, false);
        return new ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(ExercisesListAdapter.ViewHolder holder, int position)
    {
        Exercise exercise = exercises.get(position);
        holder.setExercise(exercise);
        holder.idView.setText(Long.toString(exercise.getId()));
        holder.descriptionView.setText(exercise.getDescription());
        Glide
                .with(App.getInstance())
                .load(exercise.getImg())
                .placeholder(new ColorDrawable(
                        App.getInstance().getResources().getColor(R.color.glidePlaceholderColor)))
                .error(R.drawable.ic_broken_image)
                .fallback(R.drawable.ic_broken_image)
                .diskCacheStrategy(DiskCacheStrategy.RESOURCE)
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
        @BindView(R.id.btnAddToFavorite) ImageView btnAddToFavorite;
        @BindView(R.id.btnAddToCompleted) ImageView btnAddToCompleted;

        public void setExercise(Exercise exercise)
        {
            this.exercise = exercise;
        }
        private Exercise exercise;

        ViewHolder(View view)
        {
            super(view);
            ButterKnife.bind(this, view);
            view.setOnClickListener(this);
        }

        @OnClick(R.id.btnAddToFavorite)
        public void onAddToFavoriteClick()
        {
            NetworkService.getInstance(new IResponseReceivable()
            {
                @Override
                public void onResponse(BaseResponse response)
                {
                    App.getInstance().getCurrentFragment().setSnackbar(
                            Snackbar.make(
                                App.getInstance().getCurrentFragment().getView(),
                                "Added to favorites",
                                Snackbar.LENGTH_INDEFINITE));
                }

                @Override
                public void onFailure(Throwable t)
                {

                }

                @Override
                public void onError(String error)
                {

                }

                @Override
                public void onDisconnected()
                {

                }
            }).addFavoriteExercise(exercise.getId());

            btnAddToFavorite.setImageResource(R.drawable.ic_star);
        }

        @Override
        public void onClick(View view)
        {
            listener.OnViewHolderClick(getAdapterPosition());
        }
    }
}
