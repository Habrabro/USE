package com.example.use;

public class Exercise
{
    private int number;
    private long id;
    private long topicId;
    private String description;
    private String img;
    private String rightAnswer;
    private int version;
    private boolean status;

    public boolean isCompleted()
    {
        return isCompleted;
    }

    public boolean isFavorite()
    {
        return isFavorite;
    }

    public void switchIsCompleted()
    {
        isCompleted = !isCompleted;
    }

    public void switchIsFavorite()
    {
        isFavorite = !isFavorite;
    }

    private boolean isCompleted;
    private boolean isFavorite;

    public int getNumber()
    {
        return number;
    }

    public long getId() {
        return id;
    }

    public long getTopicId() {
        return topicId;
    }

    public String getDescription() {
        return description;
    }

    public String getImg() {
        return img;
    }

    public String getRightAnswer() {
        return rightAnswer;
    }

    public int getVersion() {
        return version;
    }

    public boolean getStatus() {
        return status;
    }
}
