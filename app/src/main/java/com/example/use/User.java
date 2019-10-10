package com.example.use;

import androidx.room.Entity;
import androidx.room.Ignore;
import androidx.room.PrimaryKey;
import androidx.room.TypeConverters;

import com.example.use.database.DateConverter;

import java.util.Date;
import java.util.GregorianCalendar;

@Entity
@TypeConverters(DateConverter.class)
public class User
{
    @PrimaryKey(autoGenerate = true)
    public long id;
    private Date lastUpdate;

    @Ignore
    private long sessionId;
    @Ignore
    private boolean isAuthorized = false;

    public long getId() { return id; }
    public Date getLastUpdate() { return  lastUpdate; }
    public boolean isAuthorized() { return isAuthorized; }

    public void setLastUpdate(Date lastUpdate) { this.lastUpdate = lastUpdate; }

    public User(Date lastUpdate)
    {
        this.lastUpdate = lastUpdate;
    }

    public void authorize(long id)
    {
        this.sessionId = id;
        isAuthorized = true;
    }
}
