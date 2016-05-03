package com.fireofandroid.crashhandler;

import android.content.Context;
import android.util.Log;

import java.lang.Thread.UncaughtExceptionHandler;
import java.text.SimpleDateFormat;
import java.util.Calendar;

public class CrashHandler implements UncaughtExceptionHandler {

    private static final String TAG = "CrashHandler";

    private static Context sContext;

    private static CrashHandler sCrashHandler;

    private static String sHost;

    private static int sPort;

    private CrashHandler() {
    }

    private static String getCurrentDate() {
        Calendar c = Calendar.getInstance();
        SimpleDateFormat df = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
        String formattedDate = df.format(c.getTime());
        return formattedDate;
    }

    public static CrashHandler getInstance() {
        if(sCrashHandler == null) {
            sCrashHandler = new CrashHandler();
        }

        return sCrashHandler;
    }

    @Override
    public void uncaughtException(Thread thread, Throwable ex) {
        Log.e(TAG, ex.getMessage());
        String message =
                getCurrentDate() + "-"
                + "E-"
                + "Thread:" + thread.getName() + "(" + thread.getId() + ")-"
                + sContext.getPackageName() + "."
                + ex.getCause().getStackTrace()[0].getFileName() + ":"
                + ex.getCause().getStackTrace()[0].getLineNumber() + "-"
                + ex.getCause().getMessage();
        Client.sendMessage(message, sHost, sPort);
    }

    public static void init(Context context, String host, int port) {
        sContext = context;
        sHost = host;
        sPort = port;
        Thread.setDefaultUncaughtExceptionHandler(getInstance());
    }

    public static void reportException(Exception exception) {
        String message =
                getCurrentDate() + "-"
                + "M-"
                + "Thread:" + Thread.currentThread().getName() + "(" + Thread.currentThread().getId() + ")-"
                + sContext.getPackageName() + "."
                + exception.getStackTrace()[0].getFileName() + ":"
                + exception.getStackTrace()[0].getLineNumber() + "-"
                + exception.getMessage();
        Client.sendMessage(message, sHost, sPort);
    }
}