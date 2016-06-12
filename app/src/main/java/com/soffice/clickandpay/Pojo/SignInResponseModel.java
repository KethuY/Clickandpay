package com.soffice.clickandpay.Pojo;

import com.google.gson.annotations.SerializedName;

/**
 * Created by Aditya Sharma Malladi on 17/9/15.
 */
public class SignInResponseModel {
    @SerializedName("CODE")
    public String code;
    @SerializedName("MESSAGE")
    public String message;
    @SerializedName("USER_NAME")
    public String USER_NAME;
    @SerializedName("auth_key")
    public String auth_key;
    @SerializedName("USER_STATUS")
    public String USER_STATUS;
    @SerializedName("OTP")
    public String OTP;
}