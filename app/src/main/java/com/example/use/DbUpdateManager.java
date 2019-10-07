package com.example.use;

import android.os.AsyncTask;
import android.widget.Adapter;

import androidx.recyclerview.widget.RecyclerView;
import androidx.sqlite.db.SimpleSQLiteQuery;

import com.example.use.Networking.BaseResponse;
import com.example.use.Networking.IResponseReceivable;
import com.example.use.Networking.NetworkService;
import com.example.use.Networking.Subject;
import com.example.use.Networking.SubjectsResponse;
import com.example.use.Networking.Update;
import com.example.use.database.Db;
import com.example.use.database.IDbResponseReceivable;
import com.example.use.database.OnDbUpdatedListener;
import com.example.use.database.RawDao;
import com.example.use.database.DbService;
import com.example.use.database.SubjectsDao;

import java.util.Date;
import java.util.HashMap;
import java.util.List;

public class DbUpdateManager
{
    private Date lastUpdate;
    private Object record;
    private Db database;

    private HashMap<String, IDbOperationable> tableMap = new HashMap<>();

    public DbUpdateManager(Db database)
    {
        this.database = database;
    }

    public void update(List<Update> updates, OnDbUpdatedListener listener)
    {
        tableMap.put("subjects", new IDbOperationable()
        {
            AsyncTask<Subject, Void, Void> asyncTask = new AsyncTask<Subject, Void, Void>()
            {
                @Override
                protected Void doInBackground(Subject... subjects)
                {
                    Subject serverSubject = subjects[0];
                    Subject subject = new Subject(
                            serverSubject.getId(),
                            serverSubject.getName(),
                            serverSubject.getImg());
                    SubjectsDao subjectsDao = database.subjectsDao();
                    Subject localSubject = subjectsDao.getSubject(serverSubject.getId());
                    if (localSubject == null)
                    {
                        subjectsDao.insert(subject);
                    }
                    else
                    {
                        subjectsDao.update(subject);
                    }
                    return null;
                }

                @Override
                protected void onPostExecute(Void aVoid)
                {
                    super.onPostExecute(aVoid);
                    listener.onDbUpdated();
                }
            };

            @Override
            public void insertOrUpdate(Update update)
            {
                NetworkService.getInstance(new IResponseReceivable()
                {
                    @Override
                    public void onResponse(BaseResponse response)
                    {
                        Subject serverSubject = ((SubjectsResponse)response).getData().get(0);
                        asyncTask.execute(serverSubject);
                    }
                    @Override public void onFailure(Throwable t) { }
                    @Override public void onError(String error) { }
                    @Override public void onDisconnected() { }
                }).getSubjects(update.getRowId(), true);
            }
        });

        for (Update update : updates)
        {
            switch (update.getOperation())
            {
                case "add":
                case "edit":
                    try {
                    tableMap.get(update.getTableName()).insertOrUpdate(update); }
                    catch (NullPointerException ex){}
            }
        }

    }

    public void onResponse(Object record)
    {

    }
}
