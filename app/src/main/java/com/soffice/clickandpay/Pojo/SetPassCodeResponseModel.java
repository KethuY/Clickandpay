package com.soffice.clickandpay.Pojo;

import com.google.gson.annotations.SerializedName;

/**
 * Created by Aditya Sharma Malladi on 17/9/15.
 */
public class SetPassCodeResponseModel {
    @SerializedName("CODE")
    public String code;
    @SerializedName("MESSAGE")
    public String message;
    @SerializedName("USERNAME")
    public String Username;
    @SerializedName("WALLET")
    public String wallet_amount;
    @SerializedName("UNREADOFFERS")
    public String unreadOffers;
    @SerializedName("MAAL")
    public String month_trans;
    @SerializedName("customer_identifier")
    public String customer_identifier;
}