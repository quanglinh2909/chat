package com.example.chatgrount.MainActyvity.Model;

public class Conv {
    public boolean seen;
    public long timestamp;

    public Conv() {
    }

    public boolean isSeen() {
        return seen;
    }

    public void setSeen(boolean seen) {
        this.seen = seen;
    }

    public long getTimestamp() {
        return timestamp;
    }

    public void setTimestamp(long timestamp) {
        this.timestamp = timestamp;
    }
}
