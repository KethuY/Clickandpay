package com.soffice.clickandpay.Pojo;

import com.google.gson.annotations.SerializedName;

import java.io.Serializable;

/**
 * Created by vidyaranya on 10/3/16.
 */
public class TransactionPojo implements Serializable{
    private static final long serialVersionUID = -901346947603853609L;
    @SerializedName("loginId")
    public String loginId;
    @SerializedName("transactionId")
    public String transactionId;
    @SerializedName("transactionDate")
    public String transactionDate;
    @SerializedName("transactionTime")
    public String transactionTime;
    @SerializedName("amount")
    public String amount;
    @SerializedName("transactionStatus")
    public String transactionStatus;
    @SerializedName("receiver")
    public String receiver;
    @SerializedName("sender")
    public String Sender;
    @SerializedName("merchantid")
    public String merchantid;
    @SerializedName("merchantAddress")
    public String merchantAddress;
    @SerializedName("merchantFirstName")
    public String merchantFirstName;
    @SerializedName("merchantLogo")
    public String merchantLogo;
    @SerializedName("merchantstore")
    public String merchantstore;
    @SerializedName("city")
    public String city;
    @SerializedName("city_name")
    public String city_name;
    @SerializedName("state")
    public String state;
    @SerializedName("transactionDescription")
    public String TransactionDescription;
    @SerializedName("cptransid")
    public String AppTransactionID;
}
