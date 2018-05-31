package com.pac.masternodeapp.Model;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by PACcoin Team on 3/14/2018.
 */

public class Masternode {

    public static final List<Masternode> EmptyList = new ArrayList<>();

    //$PAC
    private String vin;
    private String status;
    private int paymentposition;
    private String ip;
    private String protocol;
    private String payee;
    private String activeseconds;
    private String lastseen;
    private String rewardStatus;
    private String alias;
    private double balance;
    private boolean isChecked;
    private String lastUpdated;
    private boolean isInPaymentQueue;
    private boolean isInPaymentQueueNotification;


    public String getVin() {
        return vin;
    }

    public void setVin(String vin) {
        this.vin = vin;
    }

    public String getStatus() {
        return status;
    }

    public void setStatus(String status) {
        this.status = status;
    }

    public int getRank() {
        return paymentposition;
    }

    public void setRank(int rank) {
        this.paymentposition = rank;
    }

    public String getIp() {
        return ip;
    }

    public void setIp(String ip) {
        this.ip = ip;
    }

    public String getProtocol() {
        return protocol;
    }

    public void setProtocol(String protocol) {
        this.protocol = protocol;
    }

    public String getPayee() {
        return payee;
    }

    public void setPayee(String payee) {
        this.payee = payee;
    }

    public String getActiveseconds() {
        return activeseconds;
    }

    public void setActiveseconds(String activeseconds) {
        this.activeseconds = activeseconds;
    }

    public String getLastseen() {
        return lastseen;
    }

    public void setLastseen(String lastseen) {
        this.lastseen = lastseen;
    }

    public String getRewardStatus() {
        return rewardStatus;
    }

    public void setRewardStatus(String rewardStatus) {
        this.rewardStatus = rewardStatus;
    }

    public String getAlias() {
        return alias;
    }

    public void setAlias(String alias) {
        this.alias = alias;
    }

    public double getBalance() {
        return balance;
    }

    public void setBalance(double balance) {
        this.balance = balance;
    }


    public boolean getIsChecked() {
        return isChecked;
    }

    public void setIsChecked(boolean isChecked) {
        this.isChecked = isChecked;
    }

    public String getLastUpdated() {
        return lastUpdated;
    }

    public void setLastUpdated(String lastUpdated) {
        this.lastUpdated = lastUpdated;
    }

    public boolean isInPaymentQueue() {
        return isInPaymentQueue;
    }

    public void setInPaymentQueue(boolean inPaymentQueue) {
        isInPaymentQueue = inPaymentQueue;
    }

    public boolean isInPaymentQueueNotification() {
        return isInPaymentQueueNotification;
    }

    public void setInPaymentQueueNotification(boolean inPaymentQueueNotification) {
        isInPaymentQueueNotification = inPaymentQueueNotification;
    }
}
