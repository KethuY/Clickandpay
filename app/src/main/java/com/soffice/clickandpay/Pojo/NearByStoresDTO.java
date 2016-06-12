package com.soffice.clickandpay.Pojo;

import com.google.gson.annotations.SerializedName;

import java.util.List;

/**
 * Created by Surya on 03-05-2016.
 */
public class NearByStoresDTO
{
    @SerializedName("CODE")
    String Code;
    @SerializedName("storesData")
    List<StoresData> Stores;

    public String getCode() {
        return Code;
    }

    public List<StoresData> getStores() {
        return Stores;
    }


}
