package com.example.use.database;

import androidx.room.Dao;
import androidx.room.Insert;
import androidx.room.Query;
import androidx.room.Update;

import com.example.use.User;

@Dao
public interface UserDao
{
    @Query("SELECT * FROM User LIMIT 1")
    User getUser();

    @Query("SELECT * FROM User WHERE id = :id")
    User getUserById(long id);

    @Insert
    void insert(User user);

    @Update
    void update(User user);
}
