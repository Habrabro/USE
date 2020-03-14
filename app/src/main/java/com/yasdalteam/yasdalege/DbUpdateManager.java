package com.yasdalteam.yasdalege;

import android.util.Log;

import com.yasdalteam.yasdalege.Networking.BaseResponse;
import com.yasdalteam.yasdalege.Networking.IResponseReceivable;
import com.yasdalteam.yasdalege.Networking.NetworkService;
import com.yasdalteam.yasdalege.Networking.UpdatesResponse;
import com.yasdalteam.yasdalege.database.Db;
import com.yasdalteam.yasdalege.database.DbRequestListener;
import com.yasdalteam.yasdalege.database.DbService;

import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.TimeZone;

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
        SimpleDateFormat dateFormat = new SimpleDateFormat("yyyy-MM-dd");
        SimpleDateFormat timeFormat = new SimpleDateFormat("HH-mm-ss");
        dateFormat.setTimeZone(TimeZone.getTimeZone("UTC"));
        timeFormat.setTimeZone(TimeZone.getTimeZone("UTC"));
        String dateTime = dateFormat.format(lastUpdate) + " " +
                timeFormat.format(lastUpdate);
        NetworkService.getInstance(new IResponseReceivable()
        {
            @Override
            public void onResponse(BaseResponse response)
            {
                List<Update> updates = ((UpdatesResponse)response).getData();
                Int i = new Int(0);
                for (Update update : updates)
                {
                    DbRequestListener<Long> onOperationCompletedListener = result -> {
                        if (i.getValue() == 1)
                        {
                            ((MainActivity)App.shared().getCurrentFragment().getActivity()).onLoad();
                        }
                        if (i.getValue() == updates.size() || updates.size() == 0)
                        {
                            DbService.getInstance().setLastUpdate(new Date());
                            listener.onRequestCompleted(null);
                            ((MainActivity)App.shared().getCurrentFragment().getActivity()).onLoaded();
                        }
                    };
                    i.inc();
                    try
                    {
                        IDbOperationable table = tableOperationsMap.get(update.getTableName());
                        switch (update.getOperation())
                        {
                            case "add":
                            case "edit":
                                table.insertOrUpdate(update, onOperationCompletedListener);
                                break;
                            case "delete":
                                table.delete(update, onOperationCompletedListener);
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
        }).getUpdates(dateFormat.format(lastUpdate), timeFormat.format(lastUpdate));
    }
}
