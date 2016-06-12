package com.soffice.clickandpay.Pojo;

import com.google.gson.annotations.SerializedName;

import java.io.Serializable;

/**
 * Created by Surya on 08-05-2016.
 */
public class StoresPosCode implements Serializable
{
    private static final long serialVersionUID = 5178642649722582661L;
    @SerializedName("merchantid")
    String MerchantID;
    @SerializedName("pos_code")
    String MerchantCode;
}
