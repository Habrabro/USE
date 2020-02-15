package com.yasdalteam.yasdalege.database;

import androidx.room.RoomDatabase;

import com.yasdalteam.yasdalege.Subject;
import com.yasdalteam.yasdalege.Topic;
import com.yasdalteam.yasdalege.User;

@androidx.room.Database(entities = {Subject.class, Topic.class, User.class}, version = 17)
public abstract class Db extends RoomDatabase
{
    public abstract SubjectDao subjectsDao();
    public abstract TopicDao topicDao();
    public abstract UserDao userDao();
}
