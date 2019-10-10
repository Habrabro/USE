package com.example.use.database;

import androidx.room.RoomDatabase;

import com.example.use.Networking.Subject;
import com.example.use.User;

@androidx.room.Database(entities = {Subject.class, User.class}, version = 2)
public abstract class Db extends RoomDatabase
{
    public abstract SubjectsDao subjectsDao();
    public abstract UserDao userDao();
}
