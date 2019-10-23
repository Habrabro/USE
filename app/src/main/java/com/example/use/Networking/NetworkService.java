package com.example.use.Networking;

import android.content.Context;
import android.net.ConnectivityManager;

import com.example.use.App;
import com.example.use.R;
import com.google.android.material.snackbar.Snackbar;

import retrofit2.Call;
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

    public Snackbar getLoadingSnackbar()
    {
        return loadingSnackbar;
    }

    private Snackbar loadingSnackbar;

    private NetworkService()
    {
        retrofit = new Retrofit.Builder()
                .baseUrl(baseURL)
                .addConverterFactory(GsonConverterFactory.create())
                .build();
        serverAPI = retrofit.create(ServerAPI.class);
        loadingSnackbar = Snackbar.make(
                App.getInstance().getCurrentFragment().getActivity().findViewById(R.id.fragmentContainer),
                "Loading",
                Snackbar.LENGTH_INDEFINITE);
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

    public void getExercises(Long id, Long topicId, String limit, boolean showLoader)
    {
        if (isNetworkConnected())
        {
            savedExerciseResponseResponse = null;
            serverAPI
                    .getExercises(id, topicId, limit)
                    .enqueue(new BaseCallback<ExerciseResponse>(listener));
            if (showLoader)
            {
                App.getInstance().getCurrentFragment().setSnackbar(loadingSnackbar);
                App.getInstance().getCurrentFragment().getSnackbar().show();
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
                App.getInstance().getCurrentFragment().setSnackbar(loadingSnackbar);
                App.getInstance().getCurrentFragment().getSnackbar().show();
            }
        }
    }

    public void getFavoriteExercises(long userId, String limit, boolean showLoader)
    {
        if (isNetworkConnected())
        {
            serverAPI
                    .getFavoriteExercises(userId, limit)
                    .enqueue(new BaseCallback<ExerciseResponse>(listener));
            if (showLoader)
            {
                App.getInstance().getCurrentFragment().setSnackbar(loadingSnackbar);
                App.getInstance().getCurrentFragment().getSnackbar().show();
            }
        }
    }

    public void getCompletedExercises(long userId, String limit, boolean showLoader)
    {
        if (isNetworkConnected())
        {
            serverAPI
                    .getCompletedExercises(userId, limit)
                    .enqueue(new BaseCallback<ExerciseResponse>(listener));
            if (showLoader)
            {
                App.getInstance().getCurrentFragment().setSnackbar(loadingSnackbar);
                App.getInstance().getCurrentFragment().getSnackbar().show();
            }
        }
    }

    public void getRandomExercise(long userId, long topicId, boolean showLoader)
    {
        if (isNetworkConnected())
        {
            serverAPI
                    .getRandomExercise(userId, topicId)
                    .enqueue(new BaseCallback<ExerciseResponse>(listener));
            if (showLoader)
            {
                App.getInstance().getCurrentFragment().setSnackbar(loadingSnackbar);
                App.getInstance().getCurrentFragment().getSnackbar().show();
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

    public void logout()
    {
        if (isNetworkConnected())
        {
            serverAPI
                    .logout()
                    .enqueue(new BaseCallback<UserResponse>(listener));
        }
    }

    public void register(String login, String password, String email)
    {
        if (isNetworkConnected())
        {
            serverAPI
                    .register(login, password, email)
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
        if (isNetworkConnected)
        {
            if (App.getInstance().getCurrentFragment().getSnackbar() != null)
            {
                App.getInstance().getCurrentFragment().getSnackbar().dismiss();
            }
        }
        else
        {
            ((IResponseReceivable)App.getInstance().getCurrentFragment()).onDisconnected();
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
        Call<ExerciseResponse> getExercises(
                @Query("id") Long id,
                @Query("topicId") Long topicId,
                @Query("limit") String limit);

        @GET("getDirectoryTopics.php")
        Call<DirectoryResponse> getDirectories(
                @Query("id") Long id,
                @Query("subjectId") Long subjectId,
                @Query("limit") String limit);

        @GET("getFavoriteExercises.php")
        Call<ExerciseResponse> getFavoriteExercises(
                @Query("userId") long userId, @Query("limit") String limit);

        @GET("getCompleted.php")
        Call<ExerciseResponse> getCompletedExercises(
                @Query("userId") long userId, @Query("limit") String limit);

        @GET("getRandomExercise.php")
        Call<ExerciseResponse> getRandomExercise(
                @Query("userId") long userId,
                @Query("topicId") long topicId);

        @GET("login.php")
        Call<UserResponse> login(@Query("login") String login, @Query("password") String password);

        @GET("logout.php")
        Call<UserResponse> logout();

        @GET("register.php")
        Call<UserResponse> register(
                @Query("login") String login,
                @Query("password") String password,
                @Query("email") String email);

        @GET("getUpdates.php")
        Call<UpdatesResponse> getUpdates(
                @Query("afterDate") String afterDate,
                @Query("afterTime") String afterTime);
    }
}