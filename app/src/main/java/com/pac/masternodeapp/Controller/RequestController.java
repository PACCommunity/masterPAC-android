package com.pac.masternodeapp.Controller;

import android.os.AsyncTask;
import android.util.Log;

import com.pac.masternodeapp.Model.Constants;
import com.pac.masternodeapp.Model.SocketCallback;

import org.json.JSONException;
import org.json.JSONObject;

import java.io.BufferedReader;
import java.io.DataInputStream;
import java.io.DataOutputStream;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.PrintWriter;
import java.net.InetAddress;
import java.net.Socket;

/**
 * Created by PACcoin Team on 3/14/2018.
 */

public class RequestController extends AsyncTask<String, Void, String> {

    private SocketCallback listener;

    public RequestController(SocketCallback listener) {
        this.listener = listener;
    }

    public String SocketRequest(String method) {
        DataInputStream is;
        DataOutputStream os;
        String noReset = "Could not reset.";
        String reset = "The server has been reset.";

        String urlServer = "";

        try {

            Socket socket = createSocket(Constants.SERVER_URL_1, 50001);
            if (socket  != null && socket.isConnected()) {
                String request = BuildMethod(method);

                return getRequest(socket, request);
            } else {
                socket = createSocket(Constants.SERVER_URL_2, 50001);

                if (socket != null && socket.isConnected()) {
                    String request = BuildMethod(method);

                    return getRequest(socket, request);
                }
            }


        } catch (Exception e) {
            Log.d("Json", noReset);
            e.printStackTrace();
            return e.toString();
        }

        return "";
    }

    String getRequest(Socket socket, String request) {
        try {
            String noReset = "Could not reset.";

            DataInputStream is;
            DataOutputStream os;

            is = new DataInputStream(socket.getInputStream());
            os = new DataOutputStream(socket.getOutputStream());
            PrintWriter pw = new PrintWriter(os);
            pw.println(request);
            Log.d("PrintWriter", request);
            pw.flush();

            BufferedReader in = new BufferedReader(new InputStreamReader(is));
            JSONObject json = new JSONObject(in.readLine());
            Log.d("Json", json.toString());
            if (!json.has("result")) {
                Log.d("Json", noReset);
            }
            is.close();
            os.close();
            return json.toString();
        } catch (Exception ex) {
            return "";
        }
    }

    Socket createSocket(String url, int port) {
        try {
            Socket socket = new Socket(InetAddress.getByName(url), port);

            return socket;
        } catch (IOException e) {
            e.printStackTrace();

            return null;
        }
    }

    @Override
    protected String doInBackground(String... strings) {
        return SocketRequest(strings[0]);
    }

    @Override
    protected void onPostExecute(String result) {
        listener.onCallResponse(result);
    }

    private String BuildMethod(String params) {
        String request;

        String[] split = params.split("/");

        if (split.length <= 1) {
            request = String.format("{\"id\":1,\"method\":\"%s\"}", split[0]);
            return request;
        } else {
            request = String.format("{\"id\":1,\"method\":\"%s\",\"params\":[\"%s\"]}", split[0], split[1]);
            return request;
        }
    }
}
