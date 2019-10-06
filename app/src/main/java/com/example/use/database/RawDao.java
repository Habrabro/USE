package com.example.use.database;

import androidx.room.Dao;
import androidx.room.RawQuery;
import androidx.sqlite.db.SupportSQLiteQuery;

import java.util.List;

@Dao
public interface RawDao
{
    @RawQuery
    List<Object> queryWithReturn(SupportSQLiteQuery query);
}
