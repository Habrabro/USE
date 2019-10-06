package com.example.use.database;

import androidx.room.RoomDatabase;

import com.example.use.Networking.Subject;

@androidx.room.Database(entities = {Subject.class}, version = 1)
public abstract class Db extends RoomDatabase
{
    public abstract SubjectsDao subjectsDao();
    public abstract RawDao rawDao();
}
