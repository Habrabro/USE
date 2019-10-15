package com.example.use.Networking;

import android.content.Context;
import android.net.ConnectivityManager;
import android.util.Log;
import android.widget.FrameLayout;

import androidx.fragment.app.Fragment;

import com.example.use.App;
import com.example.use.BaseFragment;
import com.example.use.R;
import com.google.android.material.snackbar.Snackbar;

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

    public void getSubjects(Long id)
    {
        if (isNetworkConnected())
        {
            savedSubjectResponse = null;
            serverAPI
                    .getSubjects(id)
                    .enqueue(new BaseCallback<SubjectsResponse>(listener));
        }
    }

    public void getTopics(Long id, Long subjectId)
    {
        if (isNetworkConnected())
        {
            savedTopicResponseResponse = null;
            serverAPI
                    .getTopics(id, subjectId)
                    .enqueue(new BaseCallback<TopicResponse>(listener));
        }
    }

    public void getExercises(Long id, Long topicId, String limit, boolean showLoadingSnackbar)
    {
        if (isNetworkConnected())
        {
            savedExerciseResponseResponse = null;
            serverAPI
                    .getExercises(id, topicId, limit)
                    .enqueue(new BaseCallback<ExerciseResponse>(listener));
            if (showLoadingSnackbar)
            {
                BaseFragment.snackbar = Snackbar.make(
                        App.getInstance().getCurrentFragment().getActivity().findViewById(R.id.fragmentContainer),
                        "Loading",
                        Snackbar.LENGTH_INDEFINITE);
                BaseFragment.snackbar.show();
            }
        }

    }

    public void getDirectories(Long id, Long subjectId, String limit, boolean showLoadingSnackbar)
    {
        if (isNetworkConnected())
        {
            serverAPI
                    .getDirectories(id, subjectId, limit)
                    .enqueue(new BaseCallback<DirectoryResponse>(listener));
            if (showLoadingSnackbar)
            {
                BaseFragment.snackbar = Snackbar.make(
                        App.getInstance().getCurrentFragment().getActivity().findViewById(R.id.fragmentContainer),
                        "Loading",
                        Snackbar.LENGTH_INDEFINITE);
                BaseFragment.snackbar.show();
            }
        }
    }

    public void login(String login, String password)
    {
        if (isNetworkConnected())
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
            App.getInstance().onResponse(null);
        }
        else
        {
            App.getInstance().onDisconnected();
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
                                               @Query("subjectId") Long subjectId,
                                               @Query("limit") String limit);

        @GET("login.php")
        Call<UserResponse> login(@Query("login") String login, @Query("password") String password);

        @GET("getUpdates.php")
        Call<UpdatesResponse> getUpdates(
                @Query("afterDate") String afterDate,
                @Query("afterTime") String afterTime);
    }
}