package net.qiujuer.lesson.sample.server.serverInterface;

import net.qiujuer.lesson.sample.server.handle.ClientHandler;

public interface ClientHandlerCallback {
    void onSelfClosed(ClientHandler handler);
    void onNewMessageArrived(ClientHandler clientHandler,String msg);
}
