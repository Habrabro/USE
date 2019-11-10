package com.example.use.database;

import androidx.room.Dao;
import androidx.room.Insert;
import androidx.room.Query;
import androidx.room.Update;

import com.example.use.Subject;

import java.util.List;

@Dao
public interface SubjectDao
{
    @Query("SELECT * FROM Subject")
    List<Subject> getAll();

    @Query("SELECT * FROM Subject WHERE id = :id LIMIT 1")
    Subject getSubject(long id);

    @Insert
    void insert(Subject subject);

    @Update
    void update(Subject subject);

    @Query("DELETE FROM Subject WHERE id = :id")
    void delete(long id);
}
