package com.example.use.Networking;

import com.example.use.Exercise;

import java.util.List;

public class ExerciseResponse extends BaseResponse
{
    private List<Exercise> data = null;

    public List<Exercise> getData()
    {
        return data;
    }
}
