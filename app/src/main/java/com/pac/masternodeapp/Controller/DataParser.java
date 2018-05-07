package com.pac.masternodeapp.Controller;

import android.util.Log;

import com.pac.masternodeapp.Model.Masternode;
import com.google.gson.Gson;
import com.google.gson.JsonArray;
import com.google.gson.JsonObject;
import com.google.gson.reflect.TypeToken;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.lang.reflect.Type;
import java.util.ArrayList;
import java.util.Collection;
import java.util.Iterator;
import java.util.List;

/**
 * Created by LazyDude9202 Magno6058 on 3/14/2018.
 */

public class DataParser {

    /*Receives Masternode List data and returns a collection of Masternode Objects*/
    public Collection<Masternode> GetMasternodeList(JSONArray mnArray) {
        Log.d("Json Data", mnArray.toString());

        Gson gson = new Gson();
        Type collectionType = new TypeToken<Collection<Masternode>>() {
        }.getType();

        return gson.fromJson(String.valueOf(mnArray), collectionType);
    }

    public Masternode GetMasternode(JSONArray mnArray) {
        Log.d("Json Data", mnArray.toString());

        Gson gson = new Gson();
        Type collectionType = new TypeToken<Collection<Masternode>>() {
        }.getType();

        List<Masternode> mnList = gson.fromJson(String.valueOf(mnArray), collectionType);
        Masternode mn = mnList.get(0);

        return mn;
    }

    public String GetTotalRewards(String payee) {
        return "";
    }
}
