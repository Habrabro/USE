package com.example.use;

import androidx.sqlite.db.SimpleSQLiteQuery;

import com.example.use.Networking.Subject;
import com.example.use.Networking.Update;
import com.example.use.database.IDbResponseReceivable;
import com.example.use.database.RawDao;
import com.example.use.database.DbService;

import java.util.Date;
import java.util.HashMap;
import java.util.List;

public class DbUpdateManager
{
    private Date lastUpdate;
    private Object record;

    private HashMap<String, IDbOperationable> tableMap = new HashMap<>();

    public void update(List<Update> updates)
    {
        tableMap.put("subjects", new IDbOperationable()
        {
            @Override
            public void insertOrUpdate(Update update)
            {
                Subject subject = DbService.getInstance().getDatabase().subjectsDao().getSubject(update.getId());
            }
        });

        IDbResponseReceivable getRecordListener = new IDbResponseReceivable()
        {
            @Override
            public void onResponse(Object record)
            {
                DbUpdateManager.this.record = record;
                if (record != null)
                {
                    DbService.getInstance().rawQuery();
                }


            }
        };

        for (Update update : updates)
        {
            switch (update.getOperation())
            {
                case "add":
                case "edit":
                    DbService.getInstance().getRecord(
                            update.getTableName(), update.getId(), getRecordListener);
            }
            SimpleSQLiteQuery query = new SimpleSQLiteQuery(
                    "INSERT INTO ? (name, img)" +
                            "VALUES (?, ?)",
                    new Object[]{update.getTableName()});
            RawDao rawDao = DbService.getInstance().getDatabase().rawDao();
            rawDao.queryWithReturn(query);
        }

    }

    public void onResponse(Object record)
    {

    }
}
