package com.yasdalteam.yasdalege.database;

import android.os.AsyncTask;

import androidx.room.Room;

import com.yasdalteam.yasdalege.App;
import com.yasdalteam.yasdalege.IDbOperationable;
import com.yasdalteam.yasdalege.Networking.BaseResponse;
import com.yasdalteam.yasdalege.Networking.IResponseReceivable;
import com.yasdalteam.yasdalege.Networking.NetworkService;
import com.yasdalteam.yasdalege.Subject;
import com.yasdalteam.yasdalege.Networking.SubjectsResponse;
import com.yasdalteam.yasdalege.DbUpdateManager;
import com.yasdalteam.yasdalege.Topic;
import com.yasdalteam.yasdalege.Networking.TopicResponse;
import com.yasdalteam.yasdalege.Update;
import com.yasdalteam.yasdalege.User;

import java.util.Date;
import java.util.HashMap;
import java.util.List;

public class DbService
{
    private static DbService instance;
    private Db database;
    private Date lastUpdate;

    DbService()
    {
        database = Room
                .databaseBuilder(App.shared(), Db.class, "database")
                .fallbackToDestructiveMigration()
                .build();
    }

    public static DbService getInstance()
    {
        if (instance == null) {
            instance = new DbService();
        }
        return instance;
    }

    public void setLastUpdate(Date lastUpdate)
    {
        this.lastUpdate = lastUpdate;
        App.shared().getUser().setLastUpdate(lastUpdate);
        new AsyncTask<Void, Void, Void>()
        {
            @Override
            protected Void doInBackground(Void... voids)
            {
                UserDao userDao = getDatabase().userDao();
                userDao.update(App.shared().getUser());
                return null;
            }
        }.execute();
    }

    public Db getDatabase() { return database; }

    public void insertOrUpdateUser(User user)
    {
        new AsyncTask<Void, Void, Void>()
        {
            @Override
            protected Void doInBackground(Void... voids)
            {
                UserDao userDao = database.userDao();
                User currentUser = userDao.getUserById(user.getId());
                if (currentUser == null)
                {
                    userDao.insert(user);
                }
                else
                {
                    userDao.update(user);
                }
                return null;
            }
        }.execute();
    }

    public void getUser(DbRequestListener<User> listener)
    {
        new AsyncTask<Void, Void, User>()
        {
            @Override
            protected User doInBackground(Void... voids)
            {
                UserDao userDao = getDatabase().userDao();
                User user = userDao.getUser();
                if (user == null)
                {
                    user = new User(new Date(0));
                    userDao.insert(user);
                }
                lastUpdate = user.getLastUpdate();
                return user;
            }

            @Override
            protected void onPostExecute(User user)
            {
                super.onPostExecute(user);
                listener.onRequestCompleted(user);
            }
        }.execute();
    }
}
