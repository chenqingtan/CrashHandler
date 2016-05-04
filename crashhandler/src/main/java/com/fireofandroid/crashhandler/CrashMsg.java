package com.fireofandroid.crashhandler;

import java.io.Serializable;

/**
 * Created by Kailani on 5/4/2016.
 */
public class CrashMsg implements Serializable {

    private static final long serialVersionUID = 0L;

    private String date;

    private char type;

    private String thread;

    private String body;

    private String stackTraceUp;

    public CrashMsg(String date, char type, String thread, String body, String stackTraceUp) {
        this.date = date;
        this.type = type;
        this.thread = thread;
        this.body = body;
        this.stackTraceUp = stackTraceUp;
    }

    public String getDate() {
        return date;
    }

    public char getType() {
        return type;
    }

    public String getThread() {
        return thread;
    }

    public String getBody() {
        return body;
    }

    public String getStackTraceUp() {
        return stackTraceUp;
    }
}
