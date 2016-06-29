package io.summerx.framework.jms;

public interface MessageHandler<T> {

    void handleMessage(T message);
}
