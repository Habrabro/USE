package com.example.use.database;

import androidx.room.Dao;
import androidx.room.Insert;
import androidx.room.Query;

import com.example.use.Networking.Subject;

import java.util.List;

@Dao
public interface SubjectsDao
{
    @Query("SELECT * FROM Subject")
    List<Subject> getAll();

    @Query("SELECT * FROM Subject WHERE id = :id LIMIT 1")
    Subject getSubject(long id);

    @Insert
    void insert(Subject subject);
}
