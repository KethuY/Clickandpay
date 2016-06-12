package com.soffice.clickandpay.Pojo;

import com.google.gson.annotations.SerializedName;

import java.util.List;

/**
 * Created by Surya on 08-05-2016.
 */
public class StoresData {

    @SerializedName("profile")
    public List<StoreProfile> StoreDetails;
    @SerializedName("poscodes")
    public List<StoresPosCode> StorePosCodes;

}
