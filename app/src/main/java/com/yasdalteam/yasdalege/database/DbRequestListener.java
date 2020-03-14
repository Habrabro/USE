package com.yasdalteam.yasdalege.database;

public interface DbRequestListener<T>
{
    void onRequestCompleted(T result);
}
