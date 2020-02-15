package com.yasdalteam.yasdalege;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

public class Request
{
    @SerializedName("id")
    @Expose
    private String id;
    @SerializedName("userId")
    @Expose
    private String userId;
    @SerializedName("exerciseId")
    @Expose
    private String exerciseId;
    @SerializedName("subjectId")
    @Expose
    private String subjectId;
    @SerializedName("attachment")
    @Expose
    private String attachment;
    @SerializedName("text")
    @Expose
    private String text;
    @SerializedName("examinerId")
    @Expose
    private String examinerId;
    @SerializedName("status")
    @Expose
    private String status;
    @SerializedName("tStamp")
    @Expose
    private String tStamp;
    @SerializedName("tStampOfCheck")
    @Expose
    private String tStampOfCheck;
    @SerializedName("message")
    @Expose
    private String message;
    @SerializedName("subjectName")
    @Expose
    private String subjectName;
    @SerializedName("topicNumber")
    @Expose
    private String topicNumber;

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getUserId() {
        return userId;
    }

    public void setUserId(String userId) {
        this.userId = userId;
    }

    public String getExerciseId() {
        return exerciseId;
    }

    public void setExerciseId(String exerciseId) {
        this.exerciseId = exerciseId;
    }

    public String getSubjectId() {
        return subjectId;
    }

    public void setSubjectId(String subjectId) {
        this.subjectId = subjectId;
    }

    public String getAttachment() {
        return attachment;
    }

    public void setAttachment(String attachment) {
        this.attachment = attachment;
    }

    public String getText() {
        return text;
    }

    public void setText(String text) {
        this.text = text;
    }

    public String getExaminerId() {
        return examinerId;
    }

    public void setExaminerId(String examinerId) {
        this.examinerId = examinerId;
    }

    public String getStatus() {
        return status;
    }

    public void setStatus(String status) {
        this.status = status;
    }

    public String getTStamp() {
        return tStamp;
    }

    public void setTStamp(String tStamp) {
        this.tStamp = tStamp;
    }

    public Object getTStampOfCheck() {
        return tStampOfCheck;
    }

    public void setTStampOfCheck(String tStampOfCheck) {
        this.tStampOfCheck = tStampOfCheck;
    }

    public String getMessage() {
        return message;
    }

    public void setMessage(String message) {
        this.message = message;
    }

    public String getSubjectName()
    {
        return subjectName;
    }

    public void setSubjectName(String subjectName)
    {
        this.subjectName = subjectName;
    }

    public String getTopicNumber()
    {
        return topicNumber;
    }

    public void setTopicNumber(String topicNumber)
    {
        this.topicNumber = topicNumber;
    }
}
