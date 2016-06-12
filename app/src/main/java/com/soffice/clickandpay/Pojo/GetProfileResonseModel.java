package com.soffice.clickandpay.Pojo;

import com.google.gson.annotations.SerializedName;

import java.util.ArrayList;

/**
 * Created by aditya on 20/2/16.
 */
public class GetProfileResonseModel {
    @SerializedName("CODE")
    public String code;
    @SerializedName("MESSAGE")
    public String message;
    @SerializedName("PROFILE")
    public ArrayList<ProfilePojo> profile;
}
