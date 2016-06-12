package com.soffice.clickandpay.Pojo;

import com.google.gson.annotations.SerializedName;

import java.util.ArrayList;

/**
 * Created by Surya on 09-05-2016.
 */
public class LastTransactionResponseModel
{
        @SerializedName("CODE")
        public String code;
        @SerializedName("MESSAGE")
        public String message;
        @SerializedName("TRANSACTIONS")
        public ArrayList<TransactionPojo> transactionPojos;
}
