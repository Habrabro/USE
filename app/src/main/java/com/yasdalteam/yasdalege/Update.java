package com.yasdalteam.yasdalege;

public class Update
{
    private long id;
    private String tableName;
    private String operation;
    private long rowId;
    private String tStamp;

    public long getId() { return id; }
    public String getTableName() { return tableName; }
    public String getOperation() { return operation; }
    public long getRowId() { return rowId; }
    public String getTStamp() { return tStamp; }
}
