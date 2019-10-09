package com.example.use.database;

import android.os.AsyncTask;

import androidx.room.Room;

import com.example.use.App;
import com.example.use.IDbOperationable;
import com.example.use.Networking.BaseResponse;
import com.example.use.Networking.IResponseReceivable;
import com.example.use.Networking.NetworkService;
import com.example.use.Networking.Subject;
import com.example.use.Networking.SubjectsResponse;
import com.example.use.DbUpdateManager;
import com.example.use.Networking.Update;

import java.util.Date;
import java.util.HashMap;
import java.util.List;

public class DbService
{
    private static DbService instance;
    private Db database;
    private Date lastUpdate;
    private DbUpdateManager dbUpdateManager;

    DbService()
    {
        database = Room
                .databaseBuilder(App.getInstance(), Db.class, "database")
                .build();
        dbUpdateManager = new DbUpdateManager(database, tableOperationsMapInit());
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
    }

    public Db getDatabase() { return database; }

    public DbUpdateManager getUpdateManager() { return dbUpdateManager; }

    public void updateDb(DbRequestListener listener)
    {
        dbUpdateManager.updateDb(lastUpdate, listener);
    }

    public void getSubjects(DbRequestListener listener)
    {
        new AsyncTask<Void, Void, List<Subject>>()
        {
            @Override
            protected List<Subject> doInBackground(Void... voids)
            {
                SubjectsDao subjectsDao = DbService.getInstance().getDatabase().subjectsDao();
                List<Subject> _subjects = subjectsDao.getAll();
                return _subjects;
            }

            @Override
            protected void onPostExecute(List<Subject> _subjects)
            {
                super.onPostExecute(_subjects);
                listener.onRequestCompleted(_subjects);
            }
        }.execute();
    }

    HashMap<String, IDbOperationable> tableOperationsMapInit()
    {
        HashMap<String, IDbOperationable> tableOperationsMap = new HashMap<>();
        tableOperationsMap.put("subjects", new IDbOperationable()
        {
            @Override
            public void insertOrUpdate(Update update, DbRequestListener listener)
            {
                NetworkService.getInstance(new IResponseReceivable()
                {
                    @Override
                    public void onResponse(BaseResponse response)
                    {
                        Subject serverSubject = ((SubjectsResponse)response).getData().get(0);
                        new AsyncTask<Subject, Void, Void>()
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
                                listener.onRequestCompleted(update.getId());
                            }
                        }.execute(serverSubject);
                    }
                    @Override public void onFailure(Throwable t) { }
                    @Override public void onError(String error) { }
                    @Override public void onDisconnected() { }
                }).getSubjects(update.getRowId(), true);
            }

            public void delete(Update update, DbRequestListener listener)
            {
                new AsyncTask<Subject, Void, Void>()
                {
                    @Override
                    protected Void doInBackground(Subject... subjects)
                    {
                        SubjectsDao subjectsDao = database.subjectsDao();
                        subjectsDao.delete(update.getRowId());
                        return null;
                    }

                    @Override
                    protected void onPostExecute(Void aVoid)
                    {
                        super.onPostExecute(aVoid);
                        listener.onRequestCompleted(update.getId());
                    }
                }.execute();
            }
        });
        return tableOperationsMap;
    }
}
