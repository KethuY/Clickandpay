package com.soffice.clickandpay.Pojo;

import com.google.gson.annotations.SerializedName;

/**
 * Created by sys2033 on 14/2/16.
 */
public class TransactionHistoryPojo {
    @SerializedName("transactionId")
    public String transactionId;
    @SerializedName("transactionAmount")
    public String transactionAmount;
    @SerializedName("transactionDescription")
    public String transactionDescription;
    @SerializedName("receiver")
    public String receiver;
    @SerializedName("date")
    public String date;
    @SerializedName("time")
    public String time;
}

