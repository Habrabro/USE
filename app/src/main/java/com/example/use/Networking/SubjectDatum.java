package com.example.use.Networking;

import androidx.room.Entity;
import androidx.room.PrimaryKey;

@Entity
public class SubjectDatum
{
    @PrimaryKey
    private long id;
    private String name;
    private String img;

    public long getId() {
        return id;
    }
    public String getName() {
        return name;
    }
    public String getImg() {
        return img;
    }
}
