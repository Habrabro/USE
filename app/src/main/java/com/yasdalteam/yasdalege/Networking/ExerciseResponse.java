package com.yasdalteam.yasdalege.Networking;

import com.yasdalteam.yasdalege.Exercise;

import java.util.List;

public class ExerciseResponse extends BaseResponse
{
    private List<Exercise> data = null;

    public List<Exercise> getData()
    {
        return data;
    }
}
