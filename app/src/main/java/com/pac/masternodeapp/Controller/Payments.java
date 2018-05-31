package com.pac.masternodeapp.Controller;

public class Payments {

    public boolean inPaymentQueue(int totalMN, int mnRank) {
        final float paymentPercentage = 0.10f;
        int paymentQueue = Math.round(totalMN * paymentPercentage);
        return mnRank >= 1 && mnRank <= paymentQueue;
    }

}
