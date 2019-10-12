package com.example.use.database;

import androidx.room.Dao;
import androidx.room.Insert;
import androidx.room.Query;
import androidx.room.Update;

import com.example.use.Networking.Subject;
import com.example.use.Networking.Topic;

import java.util.List;

@Dao
public interface TopicDao
{
    @Query("SELECT * FROM Topic WHERE subjectId = :subjectId")
    List<Topic> getTopics(long subjectId);

    @Query("SELECT * FROM Topic WHERE id = :id LIMIT 1")
    Topic getTopic(long id);

    @Insert
    void insert(Topic topic);

    @Update
    void update(Topic topic);

    @Query("DELETE FROM Topic WHERE id = :id")
    void delete(long id);
}
