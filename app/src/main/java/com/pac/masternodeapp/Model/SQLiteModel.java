package com.pac.masternodeapp.Model;

import android.content.Context;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;
import android.util.Log;


/**
 * Created by PACcoin Team on 3/14/2018.
 */

public class SQLiteModel extends SQLiteOpenHelper {

    private static final int DATABASE_VERSION = 3;
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
                    MasternodeReader.MasternodeEntry.COLUMN_NAME_TOTAL_REWARDS + " TEXT," +
                    MasternodeReader.MasternodeEntry.COLUMN_NAME_LAST_UPDATED + " TEXT," +
                    MasternodeReader.MasternodeEntry.COLUMN_NAME_IN_PAYMENT_QUEUE + " INTEGER," +
                    MasternodeReader.MasternodeEntry.COLUMN_NAME_IN_PAYMENT_QUEUE_NOTIFICATION + " INTEGER) ";

    private static final String SQL_CREATE_TABLE_PASSCODE =
            "CREATE TABLE " + MasternodeReader.PasscodeEntry.TABLE_NAME + "(" +
                    MasternodeReader.PasscodeEntry.COLUMN_NAME_PASSCODE + " TEXT PRIMARY KEY) ";

    private static final String SQL_CREATE_TABLE_MASTERNODE_NETWORK =
            "CREATE TABLE " + MasternodeReader.NetworkEntry.TABLE_NAME + "(" +
                    MasternodeReader.NetworkEntry.COLUMN_NAME_TOTAL_MASTERNODES + " INTEGER) ";

    private static final String SQL_DELETE_TABLE_MASTERNODE = "DROP TABLE IF EXISTS "
            + MasternodeReader.MasternodeEntry.TABLE_NAME;

    private static final String SQL_DELETE_TABLE_MASTERNODE_NETWORK = "DROP TABLE IF EXISTS "
            + MasternodeReader.NetworkEntry.TABLE_NAME;

    private static final String SQL_DELETE_TABLE_PASSCODE = "DROP TABLE IF EXISTS "
            + MasternodeReader.PasscodeEntry.TABLE_NAME;

    private static final String SQL_ALTER_TABLE_MASTERNODE = "ALTER TABLE " +
            MasternodeReader.MasternodeEntry.TABLE_NAME + " ADD COLUMN " +
            MasternodeReader.MasternodeEntry.COLUMN_NAME_LAST_UPDATED + " TEXT";

    private static final String SQL_ALTER_TABLE_MASTERNODE_PAYMENT_QUEUE_NOTIFICATION = "ALTER TABLE " +
            MasternodeReader.MasternodeEntry.TABLE_NAME + " ADD COLUMN " +
            MasternodeReader.MasternodeEntry.COLUMN_NAME_IN_PAYMENT_QUEUE_NOTIFICATION + " INTEGER";

    private static final String SQL_ALTER_TABLE_MASTERNODE_PAYMENT_QUEUE = "ALTER TABLE " +
            MasternodeReader.MasternodeEntry.TABLE_NAME + " ADD COLUMN " +
            MasternodeReader.MasternodeEntry.COLUMN_NAME_IN_PAYMENT_QUEUE + " INTEGER";

    public SQLiteModel(Context context) {
        super(context, DATABASE_NAME, null, DATABASE_VERSION);
    }

    @Override
    public void onCreate(SQLiteDatabase db) {
        Log.d("DB Create", "Database created");
        db.execSQL(SQL_CREATE_TABLE_MASTERNODE);
        db.execSQL(SQL_CREATE_TABLE_PASSCODE);
        db.execSQL(SQL_CREATE_TABLE_MASTERNODE_NETWORK);
    }

    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
        //TODO: Save data & then upgrade.
        Log.d("DB Upgrade", "Upgrading internal storage from version " + oldVersion + " to " + newVersion);
        switch (oldVersion) {
            case 1:
                db.execSQL(SQL_ALTER_TABLE_MASTERNODE);
            case 2:
                db.execSQL(SQL_DELETE_TABLE_MASTERNODE_NETWORK);
                db.execSQL(SQL_CREATE_TABLE_MASTERNODE_NETWORK);
                db.execSQL(SQL_ALTER_TABLE_MASTERNODE_PAYMENT_QUEUE);
                db.execSQL(SQL_ALTER_TABLE_MASTERNODE_PAYMENT_QUEUE_NOTIFICATION);
        }
    }

    @Override
    public void onDowngrade(SQLiteDatabase db, int oldVersion, int newVersion) {
        Log.d("DB Downgrade", "Downgrading internal storage from version " + newVersion + " to " + oldVersion);
        onUpgrade(db, oldVersion, newVersion);
    }


}
