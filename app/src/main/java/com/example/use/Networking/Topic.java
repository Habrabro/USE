package com.example.use.Networking;

import androidx.room.Entity;
import androidx.room.PrimaryKey;

@Entity
public class Topic
{
    @PrimaryKey
    private long id;
    private long number;
    private String title;
    private long subjectId;

    public Topic(long id, long number, String title, long subjectId)
    {
        this.id = id;
        this.number = number;
        this.title = title;
        this.subjectId = subjectId;
    }

    public long getId() {
        return id;
    }

    public long getNumber() {
        return number;
    }

    public String getTitle() {
        return title;
    }

    public long getSubjectId() {
        return subjectId;
    }
}
