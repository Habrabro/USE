package com.example.use.database;

import android.os.AsyncTask;

import androidx.room.Room;
import androidx.sqlite.db.SimpleSQLiteQuery;

import com.example.use.App;
import com.example.use.Networking.IResponseReceivable;
import com.example.use.Networking.Subject;
import com.example.use.Networking.SubjectsResponse;
import com.example.use.DbUpdateManager;

import java.util.List;

public class DbService
{
    private static DbService instance;
    private Db database;
    private DbUpdateManager dbUpdateManager;
    private IResponseReceivable listener;

    DbService()
    {
        database = Room
                .databaseBuilder(App.getInstance(), Db.class, "database")
                .build();
        dbUpdateManager = new DbUpdateManager();
    }

    public static DbService getInstance()
    {
        if (instance == null)
        {
            instance = new DbService();
        }
        return instance;
    }

    public void setListener(IResponseReceivable listener)
    {
        this.listener = listener;
    }

    public Db getDatabase() { return database; }

    public void getRecord(String tableName, long id, IDbResponseReceivable listener)
    {
        AsyncTask<Void, Void, Object> asyncTask = new AsyncTask<Void, Void, Object>()
        {
            public List<Object> records;

            @Override
            protected Object doInBackground(Void... voids)
            {
                RawDao rawDao = database.rawDao();
                SimpleSQLiteQuery query = new SimpleSQLiteQuery(
                        "SELECT * FROM ? WHERE id = ? LIMIT 1",
                        new Object[]{tableName, id});
                List<Object> records = rawDao.queryWithReturn(query);
                return records.get(0);
            }

            @Override
            protected void onPostExecute(Object record)
            {
                super.onPostExecute(record);
                listener.onResponse(record);
            }
        };
        asyncTask.executeOnExecutor(AsyncTask.THREAD_POOL_EXECUTOR, null);
    }

    public void rawQuery(String query)
    {
        AsyncTask<Void, Void, Void> asyncTask = new AsyncTask<Void, Void, Void>()
        {
            @Override
            protected Void doInBackground(Void... voids)
            {
                RawDao rawDao = database.rawDao();
                SimpleSQLiteQuery sqLiteQuery = new SimpleSQLiteQuery(query);
//                rawDao.query(sqLiteQuery);
                return null;
            }
        };
        asyncTask.executeOnExecutor(AsyncTask.THREAD_POOL_EXECUTOR, null);
    }

    public void getSubjects()
    {
        AsyncTask<Void, Void, List<Subject>> asyncTask = new AsyncTask<Void, Void, List<Subject>>()
        {
            public List<Subject> subjects;

            @Override
            protected List<Subject> doInBackground(Void... voids)
            {
                SubjectsDao subjectsDao = database.subjectsDao();
                List<Subject> subjects = subjectsDao.getAll();
                return subjects;
            }

            @Override
            protected void onPostExecute(List<Subject> subjects)
            {
                super.onPostExecute(subjects);
                SubjectsResponse subjectsResponse = new SubjectsResponse(subjects);
                listener.onResponse(subjectsResponse);
            }
        };
        asyncTask.executeOnExecutor(AsyncTask.THREAD_POOL_EXECUTOR, null);
    }

    public void getSubject(long id)
    {
        AsyncTask<Long, Void, List<Subject>> asyncTask = new AsyncTask<Long, Void, List<Subject>>()
        {
            public List<Subject> subjects;

            @Override
            protected List<Subject> doInBackground(Long... longs)
            {
                SubjectsDao subjectsDao = database.subjectsDao();
                Subject subject = subjectsDao.getSubject(id);
                subjects.add(subject);
                return subjects;
            }

            @Override
            protected void onPostExecute(List<Subject> subjects)
            {
                super.onPostExecute(subjects);
                SubjectsResponse subjectsResponse = new SubjectsResponse(subjects);
                listener.onResponse(subjectsResponse);
            }
        };
        asyncTask.executeOnExecutor(AsyncTask.THREAD_POOL_EXECUTOR, null);
    }

    public void insertSubject(Subject subject)
    {
        AsyncTask<Subject, Void, Void> asyncTask = new AsyncTask<Subject, Void, Void>()
        {
            @Override
            protected Void doInBackground(Subject... subjects)
            {
                SubjectsDao subjectsDao = database.subjectsDao();
                subjectsDao.insert(subjects[0]);
                return null;
            }
        };
        asyncTask.executeOnExecutor(AsyncTask.THREAD_POOL_EXECUTOR, null);
    }
}
