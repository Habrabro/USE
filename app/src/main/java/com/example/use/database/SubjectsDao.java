package com.example.use.database;

import androidx.room.Query;

import com.example.use.Networking.SubjectDatum;

import java.util.List;

public interface SubjectsDao
{
    @Query("SELECT * FROM subjectDatum")
    List<SubjectDatum> getAll();
}
