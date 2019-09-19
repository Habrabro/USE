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
    private String baseURL = "https://usetrainingadmin.000webhostapp.com/api/";
    private static BaseFragment listener;
    private BaseResponse savedResponse;
    private Retrofit retrofit;
    private ServerAPI serverAPI;

    public BaseResponse getSavedResponse()
    {
        return savedResponse;
    }

    private NetworkService()
    {
        retrofit = new Retrofit.Builder()
                .baseUrl(baseURL)
                .addConverterFactory(GsonConverterFactory.create())
                .build();
    }

    public static NetworkService getInstance(BaseFragment _listener)
    {
        if (instance == null)
        {
            instance = new NetworkService();
        }
        listener = _listener;
        return instance;
    }

    public void getExercises(long id, boolean updateData)
    {
        if (serverAPI == null || updateData)
        {
            if (isNetworkConnected())
            {
                serverAPI = retrofit.create(ServerAPI.class);
                serverAPI
                        .getExercises(id)
                        .enqueue(new BaseCallback<Exercise>(listener)
                        {
                            @Override
                            public void onResponse(Call<Exercise> call, Response<Exercise> response)
                            {
                                super.onResponse(call, response);
                                savedResponse = response.body();
                            }
                        });
            }
            else
            {
                listener.onDisconnected();
            }
        }
        else
        {
            listener.onResponse(savedResponse);
        }
    }

    public void getExercises(boolean updateData)
    {
        if (serverAPI == null || updateData)
        {
            if (isNetworkConnected())
            {
                serverAPI = retrofit.create(ServerAPI.class);
                serverAPI
                        .getExercises()
                        .enqueue(new BaseCallback<Exercise>(listener)
                        {
                            @Override
                            public void onResponse(Call<Exercise> call, Response<Exercise> response)
                            {
                                super.onResponse(call, response);
                                savedResponse = response.body();
                            }
                        });
            }
            else
            {
                listener.onDisconnected();
            }
        }
        else
        {
            Log.i("ret", "using savedResponse");
            listener.onResponse(savedResponse);
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
        @GET("getExercises.php/{id}")
        Call<Exercise> getExercises(@Path("id") long id);
        @GET("getExercises.php")
        Call<Exercise> getExercises();
    }
}