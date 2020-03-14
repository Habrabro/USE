package com.yasdalteam.yasdalege;

import com.yasdalteam.yasdalege.Networking.IResponseReceivable;

public interface IRequestSendable
{
    void send(IResponseReceivable listener);
}
