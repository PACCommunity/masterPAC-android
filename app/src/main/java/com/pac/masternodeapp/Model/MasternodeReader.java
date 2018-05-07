package com.pac.masternodeapp.Model;

import android.provider.BaseColumns;

/**
 * Created by LazyDude9202 Magno6058 3/21/2018.
 */

public class MasternodeReader {

    private MasternodeReader() {
    }

    public static class MasternodeEntry implements BaseColumns {
        public static final String TABLE_NAME = "masternode";
        public static final String COLUMN_NAME_VIN = "vin";
        public static final String COLUMN_NAME_STATUS = "status";
        public static final String COLUMN_NAME_RANK = "rank";
        public static final String COLUMN_NAME_IP_ADDRESS = "ipAddress";
        public static final String COLUMN_NAME_PROTOCOL = "protocol";
        public static final String COLUMN_NAME_PAYEE = "payee";
        public static final String COLUMN_NAME_ACTIVE_SECONDS = "activeSeconds";
        public static final String COLUMN_NAME_LAST_SEEN = "lastSeen";
        public static final String COLUMN_NAME_REWARD_STATUS = "rewardStatus";
        public static final String COLUMN_NAME_ALIAS = "alias";
        public static final String COLUMN_NAME_TOTAL_REWARDS = "totalRewards";
        public static final String COLUMN_NAME_IS_CHECKED = "isChecked";
    }

    public static class PasscodeEntry implements BaseColumns {
        public static final String TABLE_NAME = "appPasscode";
        public static final String COLUMN_NAME_PASSCODE = "passcode";
    }

}
