package com.soffice.clickandpay.Pojo;

import com.google.gson.annotations.SerializedName;

/**
 * Created by Aditya Sharma Malladi on 17/9/15.
 */
public class MerchentCheckResponseModel {
    @SerializedName("CODE")
    public String code;
    @SerializedName("MESSAGE")
    public String message;
    @SerializedName("MERCHANT")
    public MerchantPojo merchat;
}