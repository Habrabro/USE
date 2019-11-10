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
import com.example.use.Networking.IResponseReceivable;
import com.example.use.Networking.NetworkService;
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
            idView.setText(exercise.getNumber() + "." + Long.toString(exercise.getId()));
            descriptionView.setText(exercise.getDescription());
            Glide
                    .with(App.getInstance())
                    .load(exercise.getImg())
                    .placeholder(new ColorDrawable(
                            App.getInstance().getResources().getColor(R.color.glidePlaceholderColor)))
                    .error(R.drawable.ic_broken_image)
                    .fallback(R.drawable.ic_broken_image)
                    .diskCacheStrategy(DiskCacheStrategy.RESOURCE)
                    .into(imageView);
            rightAnswerView.setText(exercise.getRightAnswer());

            if (exercise.isCompleted())
            {
                btnAddToCompleted.setImageResource(R.drawable.ic_check);
            }
            else
            {
                btnAddToCompleted.setImageResource(R.drawable.ic_uncompleted);
            }
            if (exercise.isFavorite())
            {
                btnAddToFavorite.setImageResource(R.drawable.ic_star);
            }
            else
            {
                btnAddToFavorite.setImageResource(R.drawable.ic_unstar);
            }
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
            if (App.getInstance().getUser().isAuthorized())
            {
                if (!exercise.isFavorite())
                {
                    btnAddToFavorite.setImageResource(R.drawable.ic_star);
                    NetworkService.getInstance(new IResponseReceivable()
                    {
                        @Override
                        public void onResponse(BaseResponse response)
                        {
                            exercise.switchIsFavorite();
                            Snackbar.make(
                                    App.getInstance().getCurrentFragment().getView(),
                                    "Added to favorites",
                                    Snackbar.LENGTH_SHORT).show();
                        }
                        @Override public void onFailure(Throwable t) { } @Override public void onError(String error) { } @Override public void onDisconnected() { }
                    }).addFavoriteExercise(exercise.getId());
                }
                else
                {
                    btnAddToFavorite.setImageResource(R.drawable.ic_unstar);
                    NetworkService.getInstance(new IResponseReceivable()
                    {
                        @Override
                        public void onResponse(BaseResponse response)
                        {
                            exercise.switchIsFavorite();
                            Snackbar.make(
                                    App.getInstance().getCurrentFragment().getView(),
                                    "Removed from favorites",
                                    Snackbar.LENGTH_SHORT).show();
                        }
                        @Override public void onFailure(Throwable t) { } @Override public void onError(String error) { } @Override public void onDisconnected() { }
                    }).removeFavoriteExercise(exercise.getId());
                }
            }
            else
            {
                Snackbar.make(
                        App.getInstance().getCurrentFragment().getView(),
                        "You need to authorize",
                        Snackbar.LENGTH_SHORT).show();
            }
        }

        @OnClick(R.id.btnAddToCompleted)
        public void onAddToCompletedClick()
        {
            if (App.getInstance().getUser().isAuthorized())
            {
                if (!exercise.isCompleted())
                {
                    btnAddToCompleted.setImageResource(R.drawable.ic_check);
                    NetworkService.getInstance(new IResponseReceivable()
                    {
                        @Override
                        public void onResponse(BaseResponse response)
                        {
                            exercise.switchIsCompleted();
                            Snackbar.make(
                                    App.getInstance().getCurrentFragment().getView(),
                                    "Added to completed",
                                    Snackbar.LENGTH_SHORT).show();
                        }
                        @Override public void onFailure(Throwable t) { } @Override public void onError(String error) { } @Override public void onDisconnected() { }
                    }).addCompletedExercise(exercise.getId());
                }
                else
                {
                    btnAddToCompleted.setImageResource(R.drawable.ic_uncompleted);
                    NetworkService.getInstance(new IResponseReceivable()
                    {
                        @Override
                        public void onResponse(BaseResponse response)
                        {
                            exercise.switchIsCompleted();
                            Snackbar.make(
                                    App.getInstance().getCurrentFragment().getView(),
                                    "Removed from completed",
                                    Snackbar.LENGTH_SHORT).show();
                        }
                        @Override public void onFailure(Throwable t) { } @Override public void onError(String error) { } @Override public void onDisconnected() { }
                    }).removeCompletedExercise(exercise.getId());
                }
            }
            else
            {
                Snackbar.make(
                        App.getInstance().getCurrentFragment().getView(),
                        "You need to authorize",
                        Snackbar.LENGTH_SHORT).show();
            }
        }

        @Override
        public void onClick(View view)
        {
            listener.OnViewHolderClick(getAdapterPosition());
        }
    }
}
