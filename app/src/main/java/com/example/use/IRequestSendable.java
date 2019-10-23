package com.example.use;

import com.example.use.Networking.IResponseReceivable;

public interface IRequestSendable
{
    void send(IResponseReceivable listener);
}
