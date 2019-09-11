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
    private String baseURL = "https://glabstore.blob.core.windows.net/test/";
    private static NetworkServiceListener listener;
    private List<Request> requests = new ArrayList<>();
    private Retrofit retrofit;
    private ServerAPI serverAPI;

    private NetworkService()
    {
        retrofit = new Retrofit.Builder()
                .baseUrl(baseURL)
                .addConverterFactory(GsonConverterFactory.create())
                .build();
    }

    public static NetworkService getInstance(NetworkServiceListener _listener)
    {
        if (instance == null)
        {
            instance = new NetworkService();
        }
        listener = _listener;
        return instance;
    }

    public List<Request> getRequests(boolean updateData)
    {
        if (serverAPI == null || updateData)
        {
            if (isNetworkConnected())
            {
                requests.clear();
                serverAPI = retrofit.create(ServerAPI.class);
                serverAPI
                        .getRequests()
                        .enqueue(new Callback<ListResponse>()
                        {
                            @Override
                            public void onResponse(@NonNull Call<ListResponse> call, @NonNull Response<ListResponse> response)
                            {
                                ListResponse listResponse = response.body();
                                requests.clear();
                                requests.addAll(listResponse.getData());
                                if (listener instanceof NetworkServiceListener.ListResponseReceiver)
                                {
                                    if (listResponse.isStatus())
                                    {
                                        ((NetworkServiceListener.ListResponseReceiver)listener)
                                                .onListResponseReceived(listResponse);
                                    }
                                    else
                                    {
                                        listener.onError(listResponse);
                                    }
                                }
                            }

                            @Override
                            public void onFailure(@NonNull Call<ListResponse> call, @NonNull Throwable t)
                            {
                                Log.i("Tag", "Fail");
                            }
                        });
            }
            else
            {
                if (listener != null)
                {
                    listener.onDisconnected();
                }
            }
        }
        return requests;
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
        @GET("list.json")
        Call<ListResponse> getRequests();
    }
}