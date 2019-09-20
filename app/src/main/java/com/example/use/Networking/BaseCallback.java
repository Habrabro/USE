package com.example.use.Networking;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class BaseCallback<T extends BaseResponse> implements Callback<T>
{
    private IResponseReceivable listener;

    public BaseCallback(IResponseReceivable listener)
    {
        this.listener = listener;
    }

    @Override
    public void onResponse(Call<T> call, Response<T> response)
    {
        listener.onResponse(response.body());
    }

    @Override
    public void onFailure(Call<T> call, Throwable t)
    {
        listener.onFailure(t);
    }
}
