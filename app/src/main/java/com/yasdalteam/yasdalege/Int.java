package com.yasdalteam.yasdalege;

public class Int
{
    public int getValue()
    {
        return value;
    }

    public void setValue(int value)
    {
        this.value = value;
    }

    private int value;

    public Int(int value)
    {
        this.value = value;
    }

    public Int inc()
    {
        this.value++;
        return this;
    }
}
