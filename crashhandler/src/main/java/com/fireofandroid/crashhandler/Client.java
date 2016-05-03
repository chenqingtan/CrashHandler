package com.fireofandroid.crashhandler;

import android.os.AsyncTask;
import android.util.Log;
import java.io.IOException;
import java.io.OutputStreamWriter;
import java.net.Socket;

public class Client {

    private static final String TAG = "Client";

    private static Socket sSocket;

    public Client() {
    }

    public static void sendMessage(String msg, String host, int port) {

        new AsyncTask<String, Void, Void>() {
            @Override
            protected Void doInBackground(String... params) {

                try {
                    sSocket = new Socket(params[0], Integer.parseInt(params[1]));
                    Log.v(TAG, "sSocket: " + sSocket.toString());

                    OutputStreamWriter writer = new OutputStreamWriter(sSocket.getOutputStream());
                    writer.write(params[2]);
                    writer.flush();
                    writer.close();
                    sSocket.close();
                } catch (IOException e) {
                    e.printStackTrace();
                    Log.e("Client", e.toString());
                }
                return null;
            }
        }.execute(host, String.valueOf(port), msg);
    }
}
