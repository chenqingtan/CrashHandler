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
        StackTraceElement stackTraceElement = ex.getCause().getStackTrace()[0];
        String className = stackTraceElement.getClassName();
        int pos = className.lastIndexOf('.');
        String packageName = className.substring(0, pos);
        CrashMsg message = new CrashMsg(
                getCurrentDate(),
                'E',
                "Thread:" + thread.getName() + "(" + thread.getId() + ")",
                packageName + "."
                + stackTraceElement.getFileName() + ":"
                + stackTraceElement.getMethodName() + "("
                + "Line " + stackTraceElement.getLineNumber() + ")-"
                + ex.getCause().getMessage(),
                null);

        Client.sendMessage(message, sHost, sPort);
    }

    public static void init(Context context, String host, int port) {
        sContext = context;
        sHost = host;
        sPort = port;
        Thread.setDefaultUncaughtExceptionHandler(getInstance());
    }

    public static void reportException(Exception exception) {
        Log.v(TAG, exception.getMessage());
        StackTraceElement stackTraceElement = exception.getStackTrace()[0];
        CrashMsg message = new CrashMsg(
                getCurrentDate(),
                'M',
                "Thread:" + Thread.currentThread().getName() + "(" + Thread.currentThread().getId() + ")",
                sContext.getPackageName() + "."
                + stackTraceElement.getFileName() + ":"
                + stackTraceElement.getMethodName() + "("
                + "Line " + stackTraceElement.getLineNumber() + ")-"
                + exception.getMessage(),
                null);

        Client.sendMessage(message, sHost, sPort);
    }
}