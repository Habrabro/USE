package com.example.use.database;

import androidx.room.RoomDatabase;

import com.example.use.Subject;
import com.example.use.Topic;
import com.example.use.User;

@androidx.room.Database(entities = {Subject.class, Topic.class, User.class}, version = 12)
public abstract class Db extends RoomDatabase
{
    public abstract SubjectDao subjectsDao();
    public abstract TopicDao topicDao();
    public abstract UserDao userDao();
}
