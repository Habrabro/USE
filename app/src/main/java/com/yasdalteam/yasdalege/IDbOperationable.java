package com.yasdalteam.yasdalege;

import com.yasdalteam.yasdalege.database.DbRequestListener;

public interface IDbOperationable
{
    void insertOrUpdate(Update update, DbRequestListener listener);
    void delete(Update update, DbRequestListener listener);
}
