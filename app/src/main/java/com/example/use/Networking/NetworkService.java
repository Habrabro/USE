package com.example.use.Networking;

import android.content.Context;
import android.net.ConnectivityManager;
import android.util.Log;

import com.example.use.App;

import retrofit2.Call;
import retrofit2.Response;
import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;
import retrofit2.http.GET;
import retrofit2.http.Query;

public class NetworkService
{
    private static NetworkService instance;
    private String baseURL = "https://usetrainingadmin.000webhostapp.com/api/";
    private static IResponseReceivable listener;
    private Subject savedSubjectResponse;
    private Topic savedTopicResponse;
    private Exercise savedExerciseResponse;
    private Retrofit retrofit;
    private ServerAPI serverAPI;

    private NetworkService()
    {
        retrofit = new Retrofit.Builder()
                .baseUrl(baseURL)
                .addConverterFactory(GsonConverterFactory.create())
                .build();
    }

    public static NetworkService getInstance(IResponseReceivable _listener)
    {
        if (instance == null)
        {
            instance = new NetworkService();
        }
        listener = _listener;
        return instance;
    }

    public void getSubjects(boolean updateData)
    {
        if (checkNetworkService(updateData) && (savedSubjectResponse == null || updateData))
        {
            Log.i("networking", "subjects is loading");
            savedSubjectResponse = null;
            serverAPI = retrofit.create(ServerAPI.class);
            serverAPI
                    .getSubjects()
                    .enqueue(new BaseCallback<Subject>(listener)
                    {
                        @Override
                        public void onResponse(Call<Subject> call, Response<Subject> response)
                        {
                            super.onResponse(call, response);
                            savedSubjectResponse = response.body();
                        }
                    });
        }
        else
        {
            listener.onResponse(savedSubjectResponse);
        }
    }

    public void getTopics(long subjectId, boolean updateData)
    {
        if (checkNetworkService(updateData) && (savedTopicResponse == null || updateData))
        {
            savedTopicResponse = null;
            serverAPI = retrofit.create(ServerAPI.class);
            serverAPI
                    .getTopics(subjectId)
                    .enqueue(new BaseCallback<Topic>(listener)
                    {
                        @Override
                        public void onResponse(Call<Topic> call, Response<Topic> response)
                        {
                            super.onResponse(call, response);
                            savedTopicResponse = response.body();
                        }
                    });
        }
        else
        {
            listener.onResponse(savedTopicResponse);
        }
    }

    public void getExercises(long topicId, boolean updateData)
    {
        if (checkNetworkService(updateData) && (savedExerciseResponse == null || updateData))
        {
            savedExerciseResponse = null;
            serverAPI = retrofit.create(ServerAPI.class);
            serverAPI
                    .getExercises(topicId)
                    .enqueue(new BaseCallback<Exercise>(listener)
                    {
                        @Override
                        public void onResponse(Call<Exercise> call, Response<Exercise> response)
                        {
                            super.onResponse(call, response);
                            savedExerciseResponse = response.body();
                        }
                    });
        }
        else
        {
            listener.onResponse(savedExerciseResponse);
        }
    }

    public void getExercises(long topicId, long id, boolean updateData)
    {
        if (checkNetworkService(updateData) && (savedExerciseResponse == null || updateData))
        {
            savedExerciseResponse = null;
            serverAPI = retrofit.create(ServerAPI.class);
            serverAPI
                    .getExercises(topicId, id)
                    .enqueue(new BaseCallback<Exercise>(listener)
                    {
                        @Override
                        public void onResponse(Call<Exercise> call, Response<Exercise> response)
                        {
                            super.onResponse(call, response);
                            savedExerciseResponse = response.body();
                        }
                    });
        }
        else
        {
            listener.onResponse(savedExerciseResponse);
        }
    }

    private boolean checkNetworkService(boolean updateData)
    {
        if (isNetworkConnected())
        {
            return true;
        }
        else
        {
            listener.onDisconnected();
            return false;
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
        ConnectivityManager cm = (ConnectivityManager) App.getInstance().getSystemService(Context.CONNECTIVITY_SERVICE);
        return cm.getActiveNetworkInfo() != null && cm.getActiveNetworkInfo().isConnected();
    }

    public interface ServerAPI
    {
        @GET("getSubjects.php")
        Call<Subject> getSubjects();
        @GET("getTopics.php")
        Call<Topic> getTopics(@Query("subjectId") long subjectId);
        @GET("getExercises.php")
        Call<Exercise> getExercises(@Query("topicId") long topicId, @Query("id") long id);
        @GET("getExercises.php")
        Call<Exercise> getExercises(@Query("topicId") long topicId);
    }
}