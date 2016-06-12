package com.soffice.clickandpay.Pojo;

import com.google.gson.annotations.SerializedName;

import java.util.ArrayList;

/**
 * Created by vidyaranya on 10/3/16.
 */
public class TransactionResponseModel {

    @SerializedName("CODE")
    public String code;
    @SerializedName("MESSAGE")
    public String message;
    @SerializedName("TRANSACTIONS")
    public ArrayList<TransactionPojo> transactionPojos;

}
