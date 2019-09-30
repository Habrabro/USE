package com.example.use.database;

import androidx.room.Database;
import androidx.room.RoomDatabase;

import com.example.use.Networking.SubjectDatum;

@androidx.room.Database(entities = {SubjectDatum.class}, version = 1)
public abstract class Db extends RoomDatabase
{
    public abstract SubjectsDao subjectsDao();
}
