package com.example.use;

import androidx.room.Entity;
import androidx.room.PrimaryKey;

@Entity
public class Subject
{
    @PrimaryKey
    private long id;
    private String name;
    private String img;
    private boolean isActive;
    private boolean hasDirectoryTopics;

    public Subject(long id, String name, String img, boolean isActive, boolean hasDirectoryTopics)
    {
        this.id = id;
        this.name = name;
        this.img = img;
        this.isActive = isActive;
        this.hasDirectoryTopics = hasDirectoryTopics;
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
    public boolean isActive()
    {
        return isActive;
    }
    public boolean hasDirectoryTopics()
    {
        return hasDirectoryTopics;
    }
}
