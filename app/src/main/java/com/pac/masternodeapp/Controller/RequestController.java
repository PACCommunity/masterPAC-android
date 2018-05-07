package com.pac.masternodeapp.Controller;

import android.os.AsyncTask;
import android.util.Log;

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
 * Created by LazyDude9202 Magno6058 on 3/14/2018.
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

        try {
            Socket socket = new Socket(InetAddress.getByName("electro-pac.paccoin.io"), 50001);
            String request = BuildMethod(method);
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

        } catch (IOException e) {
            Log.d("Json", noReset);
            e.printStackTrace();
            return e.toString();
        } catch (JSONException e) {
            Log.d("Json", noReset);
            e.printStackTrace();
            return e.toString();
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
