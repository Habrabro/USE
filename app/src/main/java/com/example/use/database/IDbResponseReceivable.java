package com.example.use.database;

import com.example.use.Networking.BaseResponse;
import com.example.use.Networking.Subject;

import java.util.List;

public interface IDbResponseReceivable
{
    void onResponse(Object record);
}
