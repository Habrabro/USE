package com.example.use.Networking;

import com.example.use.Directory;

import java.util.List;

public class DirectoryResponse extends BaseResponse
{
    private List<Directory> data = null;

    public List<Directory> getData()
    {
        return data;
    }
}
