package com.wsw.client.param;

import java.util.concurrent.atomic.AtomicLong;

public class ClientRequest {
    private long id;
    private Object content;
    private final AtomicLong aid = new AtomicLong(1);
    private String command;

    public String getCommand() {
        return command;
    }

    public void setCommand(String command) {
        this.command = command;
    }

    public ClientRequest() {
        id = aid.incrementAndGet();
    }

    public long getId() {
        return id;
    }

    public void setId(long id) {
        this.id = id;
    }

    public Object getContent() {
        return content;
    }

    public void setContent(Object content) {
        this.content = content;
    }
}
