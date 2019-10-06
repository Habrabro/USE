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

    public long getId() {
        return id;
    }
    public String getName() {
        return name;
    }
    public String getImg() {
        return img;
    }

    public void setId(long id) { this.id = id; }
    public void setName(String name) { this.name = name; }
    public void setImg(String img) { this.img = img; }
}
