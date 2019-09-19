package com.example.use;

public class ExerciseDatum
{
    private long id;
    private long topicId;
    private String description;
    private String img;
    private String rightAnswer;
    private int version;
    private boolean status;

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
