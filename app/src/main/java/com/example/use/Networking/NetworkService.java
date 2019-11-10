package com.example.use.Networking;

import android.content.Context;
import android.net.ConnectivityManager;

import com.example.use.App;
import com.example.use.MainActivity;
import com.example.use.R;
import com.google.android.material.snackbar.Snackbar;
import com.google.gson.Gson;
import com.google.gson.GsonBuilder;

import java.util.concurrent.TimeUnit;

import okhttp3.OkHttpClient;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;
import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;
import retrofit2.http.Field;
import retrofit2.http.FormUrlEncoded;
import retrofit2.http.GET;
import retrofit2.http.POST;
import retrofit2.http.Query;

public class NetworkService
{
    private static NetworkService instance;
    private String baseURL = "https://usetrainingadmin.000webhostapp.com/api/";
    private String vkApiBaseURL = "https://api.vk.com/method/";
    private static IResponseReceivable listener;
    private SubjectsResponse savedSubjectResponse;
    private TopicResponse savedTopicResponseResponse;
    private ExerciseResponse savedExerciseResponseResponse;
    private Retrofit retrofit;
    private ServerAPI serverAPI;
    private Retrofit VKRetrofit;
    private VKApi vkApi;

    public Snackbar getLoadingSnackbar()
    {
        return loadingSnackbar;
    }

    private Snackbar loadingSnackbar;

    private NetworkService()
    {
        GsonBuilder builder = new GsonBuilder();
        builder.registerTypeAdapter(Boolean.class, new BooleanTypeAdapter());
        builder.registerTypeAdapter(boolean.class, new BooleanTypeAdapter());
        Gson gson = builder.create();

        retrofit = new Retrofit.Builder()
                .baseUrl(baseURL)
                .client(provideOkHttpClient())
                .addConverterFactory(GsonConverterFactory.create(gson))
                .build();

        VKRetrofit = new Retrofit.Builder()
                .baseUrl(vkApiBaseURL)
                .client(provideOkHttpClient())
                .addConverterFactory(GsonConverterFactory.create(gson))
                .build();

        serverAPI = retrofit.create(ServerAPI.class);
        vkApi = VKRetrofit.create(VKApi.class);

        loadingSnackbar = Snackbar.make(
                App.getInstance().getCurrentFragment().getActivity().findViewById(R.id.fragmentContainer),
                "Loading",
                Snackbar.LENGTH_INDEFINITE);
    }

    private OkHttpClient provideOkHttpClient()
    {
        OkHttpClient.Builder okhttpClientBuilder = new OkHttpClient.Builder();
        okhttpClientBuilder.connectTimeout(30, TimeUnit.SECONDS);
        okhttpClientBuilder.readTimeout(30, TimeUnit.SECONDS);
        okhttpClientBuilder.writeTimeout(30, TimeUnit.SECONDS);
        okhttpClientBuilder.interceptors().add(new AddCookiesInterceptor());
        okhttpClientBuilder.interceptors().add(new ReceivedCookiesInterceptor());
        return okhttpClientBuilder.build();
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
            serverAPI
                    .getExercises(id, topicId, limit)
                    .enqueue(new BaseCallback<ExerciseResponse>(listener));
            if (showLoader)
            {
                ((MainActivity)App.getInstance().getCurrentFragment().getActivity()).onLoad();
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
                ((MainActivity)App.getInstance().getCurrentFragment().getActivity()).onLoad();
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
                ((MainActivity)App.getInstance().getCurrentFragment().getActivity()).onLoad();
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
                ((MainActivity)App.getInstance().getCurrentFragment().getActivity()).onLoad();
            }
        }
    }

    public void getRandomExercise(Long userId, long topicId, boolean showLoader)
    {
        if (isNetworkConnected())
        {
            serverAPI
                    .getRandomExercise(userId, topicId)
                    .enqueue(new BaseCallback<ExerciseResponse>(listener));
            if (showLoader)
            {
                ((MainActivity)App.getInstance().getCurrentFragment().getActivity()).onLoad();
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

    public void register(String login, String password)
    {
        if (isNetworkConnected())
        {
            serverAPI
                    .register(login, password)
                    .enqueue(new BaseCallback<RegisterResponse>(listener));
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

    public void addFavoriteExercise(long exerciseId)
    {
        if (isNetworkConnected())
        {
            serverAPI
                    .addFavoriteExercise(exerciseId)
                    .enqueue(new BaseCallback<UserResponse>(listener));
        }
    }

    public void removeFavoriteExercise(long exerciseId)
    {
        if (isNetworkConnected())
        {
            serverAPI
                    .removeFavoriteExercise(exerciseId)
                    .enqueue(new BaseCallback<UserResponse>(listener));
        }
    }

    public void addCompletedExercise(long exerciseId)
    {
        if (isNetworkConnected())
        {
            serverAPI
                    .addCompletedExercise(exerciseId)
                    .enqueue(new BaseCallback<UserResponse>(listener));
        }
    }

    public void removeCompletedExercise(long exerciseId)
    {
        if (isNetworkConnected())
        {
            serverAPI
                    .removeCompletedExercise(exerciseId)
                    .enqueue(new BaseCallback<UserResponse>(listener));
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
            ((MainActivity)App.getInstance().getCurrentFragment().getActivity()).onLoaded();
        }
        else
        {
            ((MainActivity)App.getInstance().getCurrentFragment().getActivity()).onDisconnected();
            listener.onDisconnected();
        }
        return isNetworkConnected;
    }

    public void vkAuth(String accessToken)
    {
        if (isNetworkConnected())
        {
            serverAPI
                    .vkAuth(accessToken)
                    .enqueue(new BaseCallback<>(listener));
        }
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
                @Query("userId") Long userId,
                @Query("topicId") long topicId);

        @FormUrlEncoded
        @POST("addFavoriteExercise.php")
        Call<UserResponse> addFavoriteExercise(@Field("exerciseId") long exerciseId);

        @FormUrlEncoded
        @POST("removeFavoriteExercise.php")
        Call<UserResponse> removeFavoriteExercise(@Field("exerciseId") long exerciseId);

        @FormUrlEncoded
        @POST("addCompleted.php")
        Call<UserResponse> addCompletedExercise(@Field("exerciseId") long exerciseId);

        @FormUrlEncoded
        @POST("removeCompleted.php")
        Call<UserResponse> removeCompletedExercise(@Field("exerciseId") long exerciseId);

        @FormUrlEncoded
        @POST("login.php")
        Call<UserResponse> login(@Field("login") String login, @Field("password") String password);

        @GET("logout.php")
        Call<UserResponse> logout();

        @FormUrlEncoded
        @POST("register.php")
        Call<RegisterResponse> register(
                @Field("login") String login,
                @Field("password") String password);

        @GET("getUpdates.php")
        Call<UpdatesResponse> getUpdates(
                @Query("afterDate") String afterDate,
                @Query("afterTime") String afterTime);

        @FormUrlEncoded
        @POST("vk_auth.php")
        Call<UserResponse> vkAuth(
                @Field("accessToken") String accessToken);
    }

    public interface VKApi
    {
        @GET("account.getProfileInfo")
        Call<VKApiResponse> getProfileInfo(
                @Query("access_token") String accessToken,
                @Query("v") float version);
    }
}