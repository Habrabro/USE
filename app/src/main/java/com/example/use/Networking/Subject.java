package com.example.use.Networking;

import androidx.room.Entity;
import androidx.room.PrimaryKey;

@Entity
public class Subject
{
    @PrimaryKey
    private long id;
    private String name;
    private String img;

    public Subject(long id, String name, String img)
    {
        this.id = id;
        this.name = name;
        this.img = img;
    }

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
