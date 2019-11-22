package com.example.use;

import androidx.recyclerview.widget.RecyclerView;

import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.graphics.drawable.GradientDrawable;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
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
import java.util.Set;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;

public class ExercisesListAdapter extends RecyclerView.Adapter<RecyclerView.ViewHolder>
{
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
        switch (viewType)
        {
            case 0:
                view = inflater.inflate(R.layout.fragment_exercises_list_item, parent, false);
                return new ViewHolder(view);
            case 1:
                view = inflater.inflate(R.layout.variant_fragment_last_item, parent, false);
                return new CompleteVariantViewHolder(view);
            default:
                return null;
        }
    }

    @Override
    public void onBindViewHolder(RecyclerView.ViewHolder holder, int position)
    {
        switch (holder.getItemViewType())
        {
            case 0:
                Exercise exercise = exercises.get(position);
                ((ViewHolder)holder).setExercise(exercise);
            case 1:
                break;
        }
    }

    @Override
    public int getItemViewType(int position)
    {
        if (exercises.get(position).getId() == -1)
        {
            return 1;
        }
        else
        {
            return 0;
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

    class ViewHolder extends RecyclerView.ViewHolder implements AnswerSection.ViewHolder
    {
        AnswerSection answerSection;
        RequestForm requestForm;
        View view;
        private boolean instantiated = false;

        @BindView(R.id.tvId) TextView idView;
        @BindView(R.id.tvDescription) TextView descriptionView;
        @BindView(R.id.ivImage) ImageView imageView;
        @BindView(R.id.btnAddToFavorite) ImageView btnAddToFavorite;
        @BindView(R.id.btnAddToCompleted) ImageView btnAddToCompleted;

        public void setExercise(Exercise exercise)
        {
            this.exercise = exercise;
            idView.setText(exercise.getNumber() + "." + Long.toString(exercise.getId()));
            descriptionView.setText(exercise.getDescription());
            String nullImgUrl = App.getInstance().getServerBaseUrl() + "img/uploads/exercises_images/";
            if (!exercise.getImg().equals(nullImgUrl))
            {
                Glide
                        .with(App.getInstance())
                        .load(exercise.getImg())
                        .placeholder(new ColorDrawable(
                                App.getInstance().getResources().getColor(R.color.glidePlaceholderColor)))
                        .error(R.drawable.ic_broken_image)
                        .fallback(R.drawable.ic_broken_image)
                        .diskCacheStrategy(DiskCacheStrategy.RESOURCE)
                        .into(imageView);
            }
            if (!exercise.getRightAnswer().equals("c"))
            {

                answerSection = new AnswerSection(
                        exercise, this, view, instantiated);
            }
            else
            {
                requestForm = new RequestForm((BaseFragment) listener, view, exercise, instantiated);
            }
            instantiated = true;

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
            this.view = view;
            ButterKnife.bind(this, view);
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
                                    "Добавлено в \"Избранное\"",
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
                                    "Удалено из \"Избранного\"",
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
                        "Войдите для этого действия",
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
                                    "Добавлено в \"Выполненные\"",
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
                                    "Удалено из \"Выполненных\"",
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
                        "Войдите для этого действия",
                        Snackbar.LENGTH_SHORT).show();
            }
        }
    }
}
