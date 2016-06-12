package com.soffice.clickandpay.Pojo;

import com.google.gson.annotations.SerializedName;

/**
 * Created by sys2033 on 14/2/16.
 */
public class MerchantPojo {
    @SerializedName("merchantid")
    public String merchantid;
    @SerializedName("pos_code")
    public String pos_code;
    @SerializedName("mwid")
    public String mwid;
    @SerializedName("walletbalance")
    public String walletbalance;
    @SerializedName("mpid")
    public String mpid;
    @SerializedName("merchantFirstName")
    public String merchantFirstName;
    @SerializedName("merchantLastName")
    public String merchantLastName;
    @SerializedName("merchantstore")
    public String merchantStore;
    @SerializedName("merchantLogo")
    public String merchantLogo;
    @SerializedName("merchantAddress")
    public String merchantAddress;
}