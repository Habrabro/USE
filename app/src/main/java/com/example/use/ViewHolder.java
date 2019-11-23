package com.example.use;

import android.graphics.drawable.ColorDrawable;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.recyclerview.widget.RecyclerView;

import com.bumptech.glide.Glide;
import com.bumptech.glide.load.engine.DiskCacheStrategy;
import com.example.use.Networking.BaseResponse;
import com.example.use.Networking.IResponseReceivable;
import com.example.use.Networking.NetworkService;
import com.google.android.material.snackbar.Snackbar;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;

public class ViewHolder extends RecyclerView.ViewHolder
{
    protected View view;
    protected ExercisesListAdapter.Listener listener;
    protected boolean instantiated = false;
    protected Exercise exercise;

    @BindView(R.id.tvId)
    TextView idView;
    @BindView(R.id.tvDescription) TextView descriptionView;
    @BindView(R.id.ivImage)
    ImageView imageView;
    @BindView(R.id.btnAddToFavorite) ImageView btnAddToFavorite;
    @BindView(R.id.btnAddToCompleted) ImageView btnAddToCompleted;

    public void bindExercise(Exercise exercise)
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

    ViewHolder(View view, ExercisesListAdapter.Listener listener)
    {
        super(view);
        this.view = view;
        this.listener = listener;
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
