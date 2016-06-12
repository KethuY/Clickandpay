package com.soffice.clickandpay.Pojo;

import com.google.gson.annotations.SerializedName;

/**
 * Created by Aditya Sharma Malladi on 17/9/15.
 */
public class SendMoneyResponseModel {
    @SerializedName("CODE")
    public String code;
    @SerializedName("MESSAGE")
    public String message;
    @SerializedName("WALLET")
    public String WALLET_BAL;
}
