package com.example.use.Networking;

public class Directory
{
    private long id;
    private String title;

    public Directory(long id, String title, String content)
    {
        this.id = id;
        this.title = title;
        this.content = content;
    }

    private String content;

    public long getId()
    {
        return id;
    }

    public String getTitle()
    {
        return title;
    }

    public String getContent()
    {
        return content;
    }
}
