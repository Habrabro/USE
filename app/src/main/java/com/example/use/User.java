package com.example.use;

import androidx.room.Entity;
import androidx.room.Ignore;
import androidx.room.PrimaryKey;
import androidx.room.TypeConverters;

import com.example.use.Networking.NetworkService;
import com.example.use.database.DateConverter;
import com.example.use.database.DbService;

import java.util.Date;
import java.util.GregorianCalendar;

@Entity
@TypeConverters(DateConverter.class)
public class User
{
    @PrimaryKey(autoGenerate = true)
    public long id;
    private Date lastUpdate;
    private Long sessionId = null;

    private boolean isAuthorized = false;

    public long getId() { return id; }
    public Date getLastUpdate() { return  lastUpdate; }
    public Long getSessionId()
    {
        return sessionId;
    }
    public void setSessionId(Long sessionId)
    {
        this.sessionId = sessionId;
    }
    public boolean isAuthorized() { return isAuthorized; }
    public void setAuthorized(boolean authorized)
    {
        isAuthorized = authorized;
    }

    public void setLastUpdate(Date lastUpdate) { this.lastUpdate = lastUpdate; }

    public User(Date lastUpdate)
    {
        this.lastUpdate = lastUpdate;
    }

    public void authorize(Long id)
    {
        this.sessionId = id;
        isAuthorized = true;
        DbService.getInstance().insertOrUpdateUser(this);
    }

    public void logout()
    {
        sessionId = null;
        isAuthorized = false;
        DbService.getInstance().insertOrUpdateUser(this);
    }
}
