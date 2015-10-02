package com.erstudio.guper.service;

import com.erstudio.guper.model.WSMessage;

/**
 * Created by Евгений on 20.09.2015.
 */
public interface GuperWSServiceListener {
    public void onConnect();
    public void onDisconnect();
    public void onError(Exception ex);
    public void onStatusChange(GuperWSServiceStatus status);

    public void onConnecting();
    public void onDisconnecting();

    public void onPing(String s);
    public void onPong(String s);

    public void onSend(WSMessage wsMessage);    // отправка сообщения
    public void onReceive(WSMessage wsMessage); // получение сообщения

    public void onConfirm(WSMessage wsMessage); // подтверждение отправленного сообщения
}
