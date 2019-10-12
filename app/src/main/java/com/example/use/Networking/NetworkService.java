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
    private SubjectsResponse savedSubjectResponse;
    private TopicResponse savedTopicResponseResponse;
    private ExerciseResponse savedExerciseResponseResponse;
    private Retrofit retrofit;
    private ServerAPI serverAPI;

    private NetworkService()
    {
        retrofit = new Retrofit.Builder()
                .baseUrl(baseURL)
                .addConverterFactory(GsonConverterFactory.create())
                .build();
        serverAPI = retrofit.create(ServerAPI.class);
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

    public void getSubjects(Long id, boolean forceUpdate)
    {
        if (checkNetworkService() && (savedSubjectResponse == null || forceUpdate))
        {
            savedSubjectResponse = null;
            serverAPI
                    .getSubjects(id)
                    .enqueue(new BaseCallback<SubjectsResponse>(listener)
                    {
                        @Override
                        public void onResponse(Call<SubjectsResponse> call, Response<SubjectsResponse> response)
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

    public void getTopics(Long id, Long subjectId, boolean updateData)
    {
        if (checkNetworkService() && (savedTopicResponseResponse == null || updateData))
        {
            savedTopicResponseResponse = null;
            serverAPI
                    .getTopics(id, subjectId)
                    .enqueue(new BaseCallback<TopicResponse>(listener)
                    {
                        @Override
                        public void onResponse(Call<TopicResponse> call, Response<TopicResponse> response)
                        {
                            super.onResponse(call, response);
                            savedTopicResponseResponse = response.body();
                        }
                    });
        }
        else
        {
            listener.onResponse(savedTopicResponseResponse);
        }
    }

    public void getExercises(Long id, Long topicId, String limit, boolean updateData)
    {
        if (checkNetworkService() && (savedExerciseResponseResponse == null || updateData))
        {
            savedExerciseResponseResponse = null;
            serverAPI
                    .getExercises(id, topicId, limit)
                    .enqueue(new BaseCallback<ExerciseResponse>(listener)
                    {
                        @Override
                        public void onResponse(Call<ExerciseResponse> call, Response<ExerciseResponse> response)
                        {
                            super.onResponse(call, response);
                            savedExerciseResponseResponse = response.body();
                        }
                    });
        }
        else
        {
            listener.onResponse(savedExerciseResponseResponse);
        }
    }

    public void getDirectories(Long id, Long subjectId, String limit)
    {
        if (checkNetworkService())
        {
            serverAPI
                    .getDirectories(id, subjectId, limit)
                    .enqueue(new BaseCallback<DirectoryResponse>(listener)
                    {
                        @Override
                        public void onResponse(Call<DirectoryResponse> call, Response<DirectoryResponse> response)
                        {
                            super.onResponse(call, response);
                        }
                    });
        }
        else
        {
            listener.onResponse(savedExerciseResponseResponse);
        }
    }

    public void login(String login, String password)
    {
        if (checkNetworkService())
        {
            serverAPI
                    .login(login, password)
                    .enqueue(new BaseCallback<UserResponse>(listener));
        }
    }

    public void getUpdates(String afterDate, String afterTime)
    {
        if (isNetworkConnected())
        {
            serverAPI
                    .getUpdates(afterDate, afterTime)
                    .enqueue(new BaseCallback<UpdatesResponse>(listener));
        }
    }

    private boolean checkNetworkService()
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
        serverAPI = retrofit.create(ServerAPI.class);
    }

    private boolean isNetworkConnected()
    {
        ConnectivityManager cm = (ConnectivityManager) App.getInstance()
                .getSystemService(Context.CONNECTIVITY_SERVICE);
        boolean isNetworkConnected = cm.getActiveNetworkInfo() != null && cm.getActiveNetworkInfo().isConnected();
        if (!isNetworkConnected)
        {
            listener.onDisconnected();
        }
        return isNetworkConnected;
    }

    public interface ServerAPI
    {
        @GET("getSubjects.php")
        Call<SubjectsResponse> getSubjects(@Query("id") Long id);
        @GET("getTopics.php")
        Call<TopicResponse> getTopics(@Query("id") Long id, @Query("subjectId") Long subjectId);
        @GET("getExercises.php")
        Call<ExerciseResponse> getExercises(@Query("id") Long id,
                                            @Query("topicId") Long topicId,
                                            @Query("limit") String limit);
        @GET("getDirectoryTopics.php")
        Call<DirectoryResponse> getDirectories(@Query("id") Long id,
                                               @Query("topicId") Long subjectId,
                                               @Query("limit") String limit);

        @GET("login.php")
        Call<UserResponse> login(@Query("login") String login, @Query("password") String password);

        @GET("getUpdates.php")
        Call<UpdatesResponse> getUpdates(
                @Query("afterDate") String afterDate,
                @Query("afterTime") String afterTime);
    }
}