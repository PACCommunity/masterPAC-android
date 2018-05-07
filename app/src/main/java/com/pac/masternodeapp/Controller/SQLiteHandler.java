package com.pac.masternodeapp.Controller;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;

import com.pac.masternodeapp.Model.Masternode;
import com.pac.masternodeapp.Model.MasternodeReader;
import com.pac.masternodeapp.Model.SQLiteModel;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by LazyDude9202 Magno6058 on 3/20/2018.
 */

public class SQLiteHandler {

    private Context context;

    public SQLiteHandler(Context context) {
        this.context = context;
    }

    /*Adds Masternode to Internal Storage*/
    public long addMasternode(Masternode mn) {
        SQLiteModel sqLiteModel = new SQLiteModel(context);
        SQLiteDatabase db = sqLiteModel.getWritableDatabase();
        ContentValues values = new ContentValues();
        values.put(MasternodeReader.MasternodeEntry.COLUMN_NAME_VIN, mn.getVin());
        values.put(MasternodeReader.MasternodeEntry.COLUMN_NAME_STATUS, mn.getStatus());
        values.put(MasternodeReader.MasternodeEntry.COLUMN_NAME_RANK, mn.getRank());
        values.put(MasternodeReader.MasternodeEntry.COLUMN_NAME_IP_ADDRESS, mn.getIp());
        values.put(MasternodeReader.MasternodeEntry.COLUMN_NAME_PROTOCOL, mn.getProtocol());
        values.put(MasternodeReader.MasternodeEntry.COLUMN_NAME_PAYEE, mn.getPayee());
        values.put(MasternodeReader.MasternodeEntry.COLUMN_NAME_ACTIVE_SECONDS, mn.getActiveseconds());
        values.put(MasternodeReader.MasternodeEntry.COLUMN_NAME_LAST_SEEN, mn.getLastseen());
        values.put(MasternodeReader.MasternodeEntry.COLUMN_NAME_REWARD_STATUS, mn.getRewardStatus());
        values.put(MasternodeReader.MasternodeEntry.COLUMN_NAME_ALIAS, mn.getAlias());
        values.put(MasternodeReader.MasternodeEntry.COLUMN_NAME_TOTAL_REWARDS, mn.getBalance());
        values.put(MasternodeReader.MasternodeEntry.COLUMN_NAME_IS_CHECKED, mn.getIsChecked());

        long insertedRows = db.insert(MasternodeReader.MasternodeEntry.TABLE_NAME,
                null, values);
        sqLiteModel.close();
        return insertedRows;
    }

    /*Adds Masternodes to Internal Storage*/
    public long addMasternodes(List<Masternode> mnList) {
        long insertedRows = 0;
        SQLiteModel sqLiteModel = new SQLiteModel(context);
        SQLiteDatabase db = sqLiteModel.getWritableDatabase();

        for (int i = 0; i < mnList.size(); i++) {
            Masternode mn = mnList.get(i);
            ContentValues values = new ContentValues();
            values.put(MasternodeReader.MasternodeEntry.COLUMN_NAME_VIN, mn.getVin());
            values.put(MasternodeReader.MasternodeEntry.COLUMN_NAME_STATUS, mn.getStatus());
            values.put(MasternodeReader.MasternodeEntry.COLUMN_NAME_RANK, mn.getRank());
            values.put(MasternodeReader.MasternodeEntry.COLUMN_NAME_IP_ADDRESS, mn.getIp());
            values.put(MasternodeReader.MasternodeEntry.COLUMN_NAME_PROTOCOL, mn.getProtocol());
            values.put(MasternodeReader.MasternodeEntry.COLUMN_NAME_PAYEE, mn.getPayee());
            values.put(MasternodeReader.MasternodeEntry.COLUMN_NAME_ACTIVE_SECONDS, mn.getActiveseconds());
            values.put(MasternodeReader.MasternodeEntry.COLUMN_NAME_LAST_SEEN, mn.getLastseen());
            values.put(MasternodeReader.MasternodeEntry.COLUMN_NAME_REWARD_STATUS, mn.getRewardStatus());
            values.put(MasternodeReader.MasternodeEntry.COLUMN_NAME_ALIAS, mn.getAlias());
            values.put(MasternodeReader.MasternodeEntry.COLUMN_NAME_TOTAL_REWARDS, mn.getBalance());
            values.put(MasternodeReader.MasternodeEntry.COLUMN_NAME_IS_CHECKED, mn.getIsChecked());

            insertedRows = db.insert(MasternodeReader.MasternodeEntry.TABLE_NAME,
                    null, values);

            if (insertedRows < 0)
                break;
        }

        sqLiteModel.close();
        return insertedRows;
    }

    /*Removes masternode from Internal storage*/
    public int removeMasternode(String payee) {
        SQLiteModel sqLiteModel = new SQLiteModel(context);
        SQLiteDatabase db = sqLiteModel.getWritableDatabase();
        String selection = MasternodeReader.MasternodeEntry.COLUMN_NAME_PAYEE + " LIKE ?";
        String[] whereArgs = {payee};
        int deletedRows = db.delete(MasternodeReader.MasternodeEntry.TABLE_NAME, selection, whereArgs);
        sqLiteModel.close();
        return deletedRows;
    }

    /*Gets a list of all the masternodes in the internal storage*/
    public List<Masternode> getMasternodes() {
        SQLiteModel sqLiteModel = new SQLiteModel(context);
        SQLiteDatabase db = sqLiteModel.getReadableDatabase();
        List<Masternode> mnList = new ArrayList<>();
//        String sortOrder = MasternodeReader.MasternodeEntry.COLUMN_NAME_ALIAS;

        Cursor cursor = db.query(
                MasternodeReader.MasternodeEntry.TABLE_NAME, null, null,
                null, null, null, null);

        while (cursor.moveToNext()) {
            Masternode masternode = new Masternode();

            masternode.setVin(cursor.getString(
                    cursor.getColumnIndex(MasternodeReader.MasternodeEntry.COLUMN_NAME_VIN)));
            masternode.setStatus(cursor.getString(
                    cursor.getColumnIndex(MasternodeReader.MasternodeEntry.COLUMN_NAME_STATUS)));
            masternode.setRank(cursor.getInt(
                    cursor.getColumnIndex(MasternodeReader.MasternodeEntry.COLUMN_NAME_RANK)));
            masternode.setIp(
                    cursor.getString(cursor.getColumnIndex(
                            MasternodeReader.MasternodeEntry.COLUMN_NAME_IP_ADDRESS)));
            masternode.setProtocol(cursor.getString(
                    cursor.getColumnIndex(MasternodeReader.MasternodeEntry.COLUMN_NAME_PROTOCOL)));
            masternode.setPayee(cursor.getString(
                    cursor.getColumnIndex(MasternodeReader.MasternodeEntry.COLUMN_NAME_PAYEE)));
            masternode.setActiveseconds(cursor.getString(
                    cursor.getColumnIndex(
                            MasternodeReader.MasternodeEntry.COLUMN_NAME_ACTIVE_SECONDS)));
            masternode.setLastseen(cursor.getString(
                    cursor.getColumnIndex(MasternodeReader.MasternodeEntry.COLUMN_NAME_LAST_SEEN)));
            masternode.setRewardStatus(cursor.getString(
                    cursor.getColumnIndex(
                            MasternodeReader.MasternodeEntry.COLUMN_NAME_REWARD_STATUS)));
            masternode.setAlias(cursor.getString(
                    cursor.getColumnIndex(MasternodeReader.MasternodeEntry.COLUMN_NAME_ALIAS)));
            masternode.setBalance(cursor.getDouble(
                    cursor.getColumnIndex(
                            MasternodeReader.MasternodeEntry.COLUMN_NAME_TOTAL_REWARDS)));

            boolean isChecked = cursor.getInt(cursor.getColumnIndex(
                    MasternodeReader.MasternodeEntry.COLUMN_NAME_IS_CHECKED)) > 0;
            masternode.setIsChecked(isChecked);
            mnList.add(masternode);
        }

        cursor.close();
        sqLiteModel.close();
        return mnList;
    }

    /*Updates masternode entry*/
    public int updateMasternode(Masternode newMasternode) {
        SQLiteModel sqLiteModel = new SQLiteModel(context);
        SQLiteDatabase db = sqLiteModel.getReadableDatabase();
        ContentValues values = new ContentValues();
        String payee = newMasternode.getPayee();

        values.put(MasternodeReader.MasternodeEntry.COLUMN_NAME_VIN,
                newMasternode.getVin());
        values.put(MasternodeReader.MasternodeEntry.COLUMN_NAME_STATUS,
                newMasternode.getStatus());
        values.put(MasternodeReader.MasternodeEntry.COLUMN_NAME_RANK,
                newMasternode.getRank());
        values.put(MasternodeReader.MasternodeEntry.COLUMN_NAME_IP_ADDRESS,
                newMasternode.getIp());
        values.put(MasternodeReader.MasternodeEntry.COLUMN_NAME_PROTOCOL,
                newMasternode.getProtocol());
        values.put(MasternodeReader.MasternodeEntry.COLUMN_NAME_ACTIVE_SECONDS,
                newMasternode.getActiveseconds());
        values.put(MasternodeReader.MasternodeEntry.COLUMN_NAME_LAST_SEEN,
                newMasternode.getLastseen());
        values.put(MasternodeReader.MasternodeEntry.COLUMN_NAME_REWARD_STATUS,
                newMasternode.getRewardStatus());
        values.put(MasternodeReader.MasternodeEntry.COLUMN_NAME_ALIAS,
                newMasternode.getAlias());
        values.put(MasternodeReader.MasternodeEntry.COLUMN_NAME_TOTAL_REWARDS,
                newMasternode.getBalance());
        values.put(MasternodeReader.MasternodeEntry.COLUMN_NAME_IS_CHECKED,
                newMasternode.getIsChecked());

        String selection = MasternodeReader.MasternodeEntry.COLUMN_NAME_PAYEE + " LIKE ?";
        String[] whereArgs = {payee};

        int updatedRows = db.update(MasternodeReader.MasternodeEntry.TABLE_NAME, values, selection, whereArgs);
        sqLiteModel.close();

        return updatedRows;
    }

    /*Adds passcode to internal storage*/
    public long addPasscode(String passcode) {

        SQLiteModel sqLiteModel = new SQLiteModel(context);
        SQLiteDatabase db = sqLiteModel.getWritableDatabase();
        ContentValues values = new ContentValues();
        values.put(MasternodeReader.PasscodeEntry.COLUMN_NAME_PASSCODE, passcode);
        long rows = db.insert(MasternodeReader.PasscodeEntry.TABLE_NAME,
                null, values);
        sqLiteModel.close();
        return rows;
    }

    /*Removes passcode from internal storage*/
    public int removePasscode(String passcode) {
        SQLiteModel sqLiteModel = new SQLiteModel(context);
        SQLiteDatabase db = sqLiteModel.getWritableDatabase();
        String selection = MasternodeReader.PasscodeEntry.COLUMN_NAME_PASSCODE + " LIKE ?";
        String[] whereArgs = {passcode};
        int deletedRows = db.delete(MasternodeReader.PasscodeEntry.TABLE_NAME, selection, whereArgs);

        sqLiteModel.close();
        return deletedRows;
    }

    public void removePasscode() {
        SQLiteModel sqLiteModel = new SQLiteModel(context);
        SQLiteDatabase db = sqLiteModel.getWritableDatabase();
        db.execSQL("DELETE FROM " + MasternodeReader.PasscodeEntry.TABLE_NAME);
        sqLiteModel.close();
    }

    /*Updates passcode from internal storage */
    public int updatePasscode(String oldPasscode, String newPasscode) {

        SQLiteModel sqLiteModel = new SQLiteModel(context);
        SQLiteDatabase db = sqLiteModel.getWritableDatabase();
        ContentValues values = new ContentValues();

        values.put(MasternodeReader.PasscodeEntry.COLUMN_NAME_PASSCODE, newPasscode);
        String selection = MasternodeReader.PasscodeEntry.COLUMN_NAME_PASSCODE + " LIKE ?";
        String[] whereArgs = {oldPasscode};
        int updatedRows = db.update(MasternodeReader.PasscodeEntry.TABLE_NAME, values,
                selection, whereArgs);
        sqLiteModel.close();

        return updatedRows;
    }

    /*Verifies passcode */
    public boolean verifyPasscode(String passcode) {
        boolean verification = false;
        SQLiteModel sqLiteModel = new SQLiteModel(context);
        SQLiteDatabase db = sqLiteModel.getReadableDatabase();
        String[] projection = {MasternodeReader.PasscodeEntry.COLUMN_NAME_PASSCODE};
        String selection = MasternodeReader.PasscodeEntry.COLUMN_NAME_PASSCODE + " = ?";
        String[] whereArgs = {passcode};
        String sortOrder = MasternodeReader.PasscodeEntry.COLUMN_NAME_PASSCODE;
        Cursor cursor = db.query(
                MasternodeReader.PasscodeEntry.TABLE_NAME, projection, selection,
                whereArgs, null, null, sortOrder);

        while (cursor.moveToNext()) {
            String savedPasscode = cursor.getString(
                    cursor.getColumnIndex(MasternodeReader.PasscodeEntry.COLUMN_NAME_PASSCODE));
            verification = savedPasscode.equals(passcode);
        }
        cursor.close();
        sqLiteModel.close();
        return verification;
    }


}
