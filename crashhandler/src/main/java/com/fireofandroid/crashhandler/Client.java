package com.fireofandroid.crashhandler;

import android.os.AsyncTask;
import android.util.Log;

import java.io.BufferedOutputStream;
import java.io.IOException;
import java.io.ObjectOutputStream;
import java.net.Socket;

public final class Client {

    private static final String TAG = "Client";

    private static Socket sSocket;

    private Client() {

    }

    public static void sendMessage(CrashMsg msg, final String host, final int port) {

        new AsyncTask<CrashMsg, Void, Void>() {
            @Override
            protected Void doInBackground(CrashMsg... params) {

                try {
                    sSocket = null;
                    sSocket = new Socket(host, port);
                    Log.v(TAG, "create new socket: " + sSocket.toString());

                    ObjectOutputStream oos = new ObjectOutputStream(
                            new BufferedOutputStream(sSocket.getOutputStream()));
                    oos.writeObject(params[0]);
                    oos.flush();
                    oos.close();//close the returned OutputStream will close the associated socket.
                } catch (IOException e) {
                    e.printStackTrace();
                    Log.e(TAG, e.toString());
                }

                return null;
            }
        }.execute(msg);
    }
}
