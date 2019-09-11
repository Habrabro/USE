package com.example.use;

import retrofit2.Call;
import retrofit2.Response;

public class Callback<T> implements retrofit2.Callback<T>
{
    public Callback()
    {

    }

    @Override
    public void onResponse(Call<T> call, Response<T> response)
    {

    }

    @Override
    public void onFailure(Call<T> call, Throwable t)
    {

    }
}
