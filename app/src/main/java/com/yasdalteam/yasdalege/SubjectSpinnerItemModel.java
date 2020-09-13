package com.yasdalteam.yasdalege;

import java.security.PublicKey;

public class SubjectSpinnerItemModel extends SubjectSpinnerModel
{
    private Subject subject;
    public Subject getSubject()
    {
        return subject;
    }

    public SubjectSpinnerItemModel(Subject subject)
    {
        super(subject.getName(), SubjectSpinnerModelType.ITEM);

        this.subject = subject;
    }
}
