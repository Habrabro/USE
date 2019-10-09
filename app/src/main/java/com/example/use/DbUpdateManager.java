package com.example.use;

import android.os.AsyncTask;
import android.util.Log;

import com.example.use.Networking.BaseResponse;
import com.example.use.Networking.IResponseReceivable;
import com.example.use.Networking.NetworkService;
import com.example.use.Networking.Subject;
import com.example.use.Networking.SubjectsResponse;
import com.example.use.Networking.Update;
import com.example.use.Networking.UpdatesResponse;
import com.example.use.database.Db;
import com.example.use.database.DbRequestListener;
import com.example.use.database.DbService;
import com.example.use.database.SubjectsDao;

import java.util.Date;
import java.util.HashMap;
import java.util.List;

public class DbUpdateManager
{
    private Object record;
    private Db database;
    private HashMap<String, IDbOperationable> tableOperationsMap = new HashMap<>();

    public DbUpdateManager(Db database, HashMap<String, IDbOperationable> tableOperationsMap)
    {
        this.database = database;
        this.tableOperationsMap = tableOperationsMap;
    }

    public void updateDb(Date lastUpdate, DbRequestListener listener)
    {
        NetworkService.getInstance(new IResponseReceivable()
        {
            @Override
            public void onResponse(BaseResponse response)
            {
                List<Update> updates = ((UpdatesResponse)response).getData();
                for (Update update : updates)
                {
                    DbRequestListener<Long> onOperationCompletedlistener = new DbRequestListener<Long>()
                    {
                        @Override
                        public void onRequestCompleted(Long result)
                        {
                            if (result == updates.get(updates.size() - 1).getId())
                            {
                                listener.onRequestCompleted(null);
                            }
                        }
                    };
                    try
                    {
                        IDbOperationable table = tableOperationsMap.get(update.getTableName());
                        switch (update.getOperation())
                        {
                            case "add":
                            case "edit":
                                table.insertOrUpdate(update, onOperationCompletedlistener);
                                break;
                            case "delete":
                                table.delete(update, onOperationCompletedlistener);
                                break;
                        }
                    }
                    catch (NullPointerException ex)
                    {
                        Log.i("database", "There is no table with provided name");
                    }

                }
            }
            @Override public void onFailure(Throwable t) { }
            @Override public void onError(String error) { }
            @Override public void onDisconnected() { }
        }).getUpdates("2019-09-01", null);
    }
}
