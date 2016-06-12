package com.soffice.clickandpay.Pojo;

import com.google.gson.annotations.SerializedName;

/**
 * Created by Surya on 02-05-2016.
 */
public class ResetModelDTO  {
    @SerializedName("CODE")
    public String Code;
    @SerializedName("MESSAGE")
    public String Message;
    @SerializedName("otp")
    public String OTP;
    @SerializedName("auth_key")
    public String AuthKey;
}
