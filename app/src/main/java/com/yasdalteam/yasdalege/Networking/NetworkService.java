package com.yasdalteam.yasdalege.Networking;

import android.content.Context;
import android.net.ConnectivityManager;

import com.yasdalteam.yasdalege.App;
import com.yasdalteam.yasdalege.MainActivity;
import com.yasdalteam.yasdalege.Payments.PaymentResponse;
import com.google.android.material.snackbar.Snackbar;
import com.google.gson.Gson;
import com.google.gson.GsonBuilder;

import java.util.List;
import java.util.concurrent.TimeUnit;

import okhttp3.MultipartBody;
import okhttp3.OkHttpClient;
import okhttp3.RequestBody;
import retrofit2.Call;
import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;
import retrofit2.http.Field;
import retrofit2.http.FormUrlEncoded;
import retrofit2.http.GET;
import retrofit2.http.Multipart;
import retrofit2.http.POST;
import retrofit2.http.Part;
import retrofit2.http.Query;

public class NetworkService
{
    private static NetworkService instance;
    private String baseURL = App.shared().SERVER_BASE_URL;
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
    }

    private OkHttpClient provideOkHttpClient()
    {
        OkHttpClient.Builder okhttpClientBuilder = new OkHttpClient.Builder();
        okhttpClientBuilder.connectTimeout(60, TimeUnit.SECONDS);
        okhttpClientBuilder.readTimeout(60, TimeUnit.SECONDS);
        okhttpClientBuilder.writeTimeout(60, TimeUnit.SECONDS);
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
                ((MainActivity)App.shared().getCurrentFragment().getActivity()).onLoad();
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
                ((MainActivity)App.shared().getCurrentFragment().getActivity()).onLoad();
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
                ((MainActivity)App.shared().getCurrentFragment().getActivity()).onLoad();
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
                ((MainActivity)App.shared().getCurrentFragment().getActivity()).onLoad();
            }
        }
    }

    public void getRandomExercise(Long userId, long topicId, boolean showLoader)
    {
        serverAPI
                .getRandomExercise(userId, topicId)
                .enqueue(new BaseCallback<ExerciseResponse>(listener));
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

    public void getProfile()
    {
        if (isNetworkConnected())
        {
            serverAPI
                    .getProfile()
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

    public boolean isNetworkConnected()
    {
        ConnectivityManager cm = (ConnectivityManager) App.shared()
                .getSystemService(Context.CONNECTIVITY_SERVICE);
        boolean isNetworkConnected = cm.getActiveNetworkInfo() != null && cm.getActiveNetworkInfo().isConnected();
        if (isNetworkConnected)
        {
            if (App.shared().getCurrentFragment() != null) {
                ((MainActivity)App.shared().getCurrentFragment().getActivity()).onLoaded();
            }
        }
        else
        {
            if (App.shared().getCurrentFragment() != null)
            {
                ((MainActivity)App.shared().getCurrentFragment().getActivity()).onDisconnected();
            }
            listener.onDisconnected();
        }
        return isNetworkConnected;
    }

    public void createRequest(RequestBody exerciseId, List<MultipartBody.Part> attachment, RequestBody text)
    {
        if (isNetworkConnected())
        {
            //setBaseURL("http://f906161m.bget.ru/use_training_admin/");
            serverAPI
                    .createRequest(exerciseId, attachment, text)
                    .enqueue(new BaseCallback<>(listener));
        }
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

    public void getUserRequests()
    {
        if (isNetworkConnected())
        {
            serverAPI
                    .getUserRequests()
                    .enqueue(new BaseCallback<>(listener));
            ((MainActivity)App.shared().getCurrentFragment().getActivity()).onLoad();
        }
    }

    public void getPriceList()
    {
        if (isNetworkConnected())
        {
            serverAPI
                    .getPriceList()
                    .enqueue(new BaseCallback<>(listener));
            ((MainActivity)App.shared().getCurrentFragment().getActivity()).onLoad();
        }
    }

    public void createPayment(String paymentToken, String description)
    {
        if (isNetworkConnected())
        {
            serverAPI
                    .createPayment(paymentToken, description)
                    .enqueue(new BaseCallback<>(listener));
            ((MainActivity)App.shared().getCurrentFragment().getActivity()).onLoad();
        }
    }

    public interface ServerAPI
    {
        @GET("api/getSubjects.php")
        Call<SubjectsResponse> getSubjects(@Query("id") Long id);

        @GET("api/getTopics.php")
        Call<TopicResponse> getTopics(@Query("id") Long id, @Query("subjectId") Long subjectId);

        @GET("api/getExercises.php")
        Call<ExerciseResponse> getExercises(
                @Query("id") Long id,
                @Query("topicId") Long topicId,
                @Query("limit") String limit);

        @GET("api/getDirectoryTopics.php")
        Call<DirectoryResponse> getDirectories(
                @Query("id") Long id,
                @Query("subjectId") Long subjectId,
                @Query("limit") String limit);

        @GET("api/getFavoriteExercises.php")
        Call<ExerciseResponse> getFavoriteExercises(
                @Query("userId") long userId, @Query("limit") String limit);

        @GET("api/getCompleted.php")
        Call<ExerciseResponse> getCompletedExercises(
                @Query("userId") long userId, @Query("limit") String limit);

        @GET("api/getRandomExercise.php")
        Call<ExerciseResponse> getRandomExercise(
                @Query("userId") Long userId,
                @Query("topicId") long topicId);

        @FormUrlEncoded
        @POST("api/addFavoriteExercise.php")
        Call<UserResponse> addFavoriteExercise(@Field("exerciseId") long exerciseId);

        @FormUrlEncoded
        @POST("api/removeFavoriteExercise.php")
        Call<UserResponse> removeFavoriteExercise(@Field("exerciseId") long exerciseId);

        @FormUrlEncoded
        @POST("api/addCompleted.php")
        Call<UserResponse> addCompletedExercise(@Field("exerciseId") long exerciseId);

        @FormUrlEncoded
        @POST("api/removeCompleted.php")
        Call<UserResponse> removeCompletedExercise(@Field("exerciseId") long exerciseId);

        @FormUrlEncoded
        @POST("api/login.php")
        Call<UserResponse> login(@Field("login") String login, @Field("password") String password);

        @GET("api/logout.php")
        Call<UserResponse> logout();

        @GET("api/get_profile.php")
        Call<UserResponse> getProfile();

        @FormUrlEncoded
        @POST("api/register.php")
        Call<RegisterResponse> register(
                @Field("login") String login,
                @Field("password") String password);

        @GET("api/getUpdates.php")
        Call<UpdatesResponse> getUpdates(
                @Query("afterDate") String afterDate,
                @Query("afterTime") String afterTime);

        @FormUrlEncoded
        @POST("api/vk_auth.php")
        Call<UserResponse> vkAuth(
                @Field("accessToken") String accessToken);

        @Multipart
        @POST("create_request.php")
        Call<BaseResponse> createRequest(
                @Part("exerciseId") RequestBody exerciseId,
                @Part List<MultipartBody.Part> attachment,
                @Part("text") RequestBody text);

        @GET("/api/getUserRequests.php")
        Call<RequestResponse> getUserRequests();

        @GET("/api/getPriceList.php")
        Call<PriceListResponse> getPriceList();

        @FormUrlEncoded
        @POST("/api/create_payment.php")
        Call<PaymentResponse> createPayment(
                @Field("paymentToken") String paymentToken,
                @Field("description") String description);
    }

    public interface VKApi
    {
        @GET("account.getProfileInfo")
        Call<VKApiResponse> getProfileInfo(
                @Query("access_token") String accessToken,
                @Query("v") float version);
    }
}