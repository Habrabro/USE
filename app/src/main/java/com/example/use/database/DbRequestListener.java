package com.example.use.database;

public interface DbRequestListener<T>
{
    void onRequestCompleted(T result);
}
