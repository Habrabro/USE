package com.example.use;

public class User
{
    private boolean isAuthorized = false;
    private long id;

    public long getId() { return id; }
    public boolean isAuthorized() { return isAuthorized; }
    public void authorize(long id)
    {
        this.id = id;
        isAuthorized = true;
    }
}
