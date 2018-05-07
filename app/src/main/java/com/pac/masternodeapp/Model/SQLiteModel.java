package com.pac.masternodeapp.Model;

import android.content.Context;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;
import android.util.Log;


/**
 * Created by LazyDude9202 Magno6058 on 3/20/2018.
 */

public class SQLiteModel extends SQLiteOpenHelper {

    private static final int DATABASE_VERSION = 1;
    private static final String DATABASE_NAME = "Masternode.db";
    private static final String SQL_CREATE_TABLE_MASTERNODE =
            "CREATE TABLE " + MasternodeReader.MasternodeEntry.TABLE_NAME + "(" +
                    MasternodeReader.MasternodeEntry.COLUMN_NAME_PAYEE + " TEXT PRIMARY KEY," +
                    MasternodeReader.MasternodeEntry.COLUMN_NAME_VIN + " TEXT," +
                    MasternodeReader.MasternodeEntry.COLUMN_NAME_STATUS + " TEXT," +
                    MasternodeReader.MasternodeEntry.COLUMN_NAME_RANK + " INTEGER," +
                    MasternodeReader.MasternodeEntry.COLUMN_NAME_IP_ADDRESS + " TEXT," +
                    MasternodeReader.MasternodeEntry.COLUMN_NAME_PROTOCOL + " TEXT," +
                    MasternodeReader.MasternodeEntry.COLUMN_NAME_ACTIVE_SECONDS + " TEXT," +
                    MasternodeReader.MasternodeEntry.COLUMN_NAME_LAST_SEEN + " TEXT," +
                    MasternodeReader.MasternodeEntry.COLUMN_NAME_REWARD_STATUS + " TEXT," +
                    MasternodeReader.MasternodeEntry.COLUMN_NAME_ALIAS + " TEXT," +
                    MasternodeReader.MasternodeEntry.COLUMN_NAME_IS_CHECKED + " INTEGER," +
                    MasternodeReader.MasternodeEntry.COLUMN_NAME_TOTAL_REWARDS + " TEXT)";

    private static final String SQL_CREATE_TABLE_PASSCODE =
            "CREATE TABLE " + MasternodeReader.PasscodeEntry.TABLE_NAME + "(" +
                    MasternodeReader.PasscodeEntry.COLUMN_NAME_PASSCODE + " TEXT PRIMARY KEY) ";

    private static final String SQL_DELETE_TABLE_MASTERNODE = "DROP TABLE IF EXISTS "
            + MasternodeReader.MasternodeEntry.TABLE_NAME;

    private static final String SQL_DELETE_TABLE_PASSCODE = "DROP TABLE IF EXISTS "
            + MasternodeReader.PasscodeEntry.TABLE_NAME;

    public SQLiteModel(Context context) {
        super(context, DATABASE_NAME, null, DATABASE_VERSION);
    }

    @Override
    public void onCreate(SQLiteDatabase db) {
        Log.d("DB Create", "Database created");
        db.execSQL(SQL_CREATE_TABLE_MASTERNODE);
        db.execSQL(SQL_CREATE_TABLE_PASSCODE);
    }

    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
        //TODO: Save data & then upgrade.
        Log.d("DB Upgrade", "Upgrading internal storage from version " + oldVersion + " to " + newVersion);
        db.execSQL(SQL_DELETE_TABLE_MASTERNODE);
        db.execSQL(SQL_DELETE_TABLE_PASSCODE);
        onCreate(db);
    }

    @Override
    public void onDowngrade(SQLiteDatabase db, int oldVersion, int newVersion) {
        Log.d("DB Downgrade", "Downgrading internal storage from version " + newVersion + " to " + oldVersion);
        onUpgrade(db, oldVersion, newVersion);
    }


}
