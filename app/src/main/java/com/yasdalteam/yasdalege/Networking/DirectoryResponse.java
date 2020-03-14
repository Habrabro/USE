package com.yasdalteam.yasdalege.Networking;

import com.yasdalteam.yasdalege.Directory;

import java.util.List;

public class DirectoryResponse extends BaseResponse
{
    private List<Directory> data = null;

    public List<Directory> getData()
    {
        return data;
    }
}
