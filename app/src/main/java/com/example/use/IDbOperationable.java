package com.example.use;

import com.example.use.database.DbRequestListener;

public interface IDbOperationable
{
    void insertOrUpdate(Update update, DbRequestListener listener);
    void delete(Update update, DbRequestListener listener);
}
