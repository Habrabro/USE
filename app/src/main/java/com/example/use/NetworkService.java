package com.example.use;

import android.app.Application;
import android.content.Context;
import android.net.ConnectivityManager;
import android.util.Log;

import androidx.annotation.NonNull;

import java.util.ArrayList;
import java.util.List;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;
import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;
import retrofit2.http.GET;
import retrofit2.http.Path;

public class NetworkService
{
    private static NetworkService instance;
    private String baseURL = "https://my-json-server.typicode.com/Habrabro/JSON_server/";
    private static ExerciseFragment listener;
    private Retrofit retrofit;
    private ServerAPI serverAPI;

    private NetworkService()
    {
        retrofit = new Retrofit.Builder()
                .baseUrl(baseURL)
                .addConverterFactory(GsonConverterFactory.create())
                .build();
    }

    public static NetworkService getInstance(ExerciseFragment _listener)
    {
        if (instance == null)
        {
            instance = new NetworkService();
        }
        listener = _listener;
        return instance;
    }

    public void getRequests(long id, boolean updateData)
    {
        if (serverAPI == null || updateData)
        {
            if (isNetworkConnected())
            {
                serverAPI = retrofit.create(ServerAPI.class);
                serverAPI
                        .getExercise(id)
                        .enqueue(new BaseCallback<Exercise>(listener));
            }
            else
            {
                if (listener != null)
                {

                }
            }
        }
    }

    public void getRequestsList(long id, boolean updateData)
    {
        if (serverAPI == null || updateData)
        {
            if (isNetworkConnected())
            {
                serverAPI = retrofit.create(ServerAPI.class);
                serverAPI
                        .getExercisesList()
                        .enqueue(new BaseCallback<Exercise>(listener));
            }
            else
            {
                if (listener != null)
                {

                }
            }
        }
    }

    public void setBaseURL(String url)
    {
        baseURL = url;
        retrofit = new Retrofit.Builder()
                .baseUrl(baseURL)
                .addConverterFactory(GsonConverterFactory.create())
                .build();
    }

    private boolean isNetworkConnected()
    {
        ConnectivityManager cm = (ConnectivityManager)App.getInstance().getSystemService(Context.CONNECTIVITY_SERVICE);
        return cm.getActiveNetworkInfo() != null && cm.getActiveNetworkInfo().isConnected();
    }

    public interface ServerAPI
    {
        @GET("getExercise/{id}")
        Call<Exercise> getExercise(@Path("id") long id);
        @GET("getExercise")
        Call<Exercise> getExercisesList();
    }
}