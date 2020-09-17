package com.yasdalteam.yasdalege;

public class SubjectSpinnerModel
{
    enum SubjectSpinnerModelType
    {
        ALL,
        ITEM,
    }

    private SubjectSpinnerModelType type;
    public SubjectSpinnerModelType getType()
    {
        return type;
    }
    private String title;
    public String getTitle()
    {
        return title;
    }

    public SubjectSpinnerModel(String title, SubjectSpinnerModelType type)
    {
        this.title = title;
        this.type = type;
    }
}
