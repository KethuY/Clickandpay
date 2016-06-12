package com.soffice.clickandpay.Pojo;

import com.google.gson.annotations.SerializedName;

/**
 * Created by Surya on 03-05-2016.
 */
public class StoreProfile
{
    @SerializedName("merchantId")
    String merchantId;
    @SerializedName("merchantFirstName")
    String merchantFirstName;
    @SerializedName("merchantLastName")
    String merchantLastName;
    @SerializedName("merchantMobileNumber")
    String merchantMobileNo;
    @SerializedName("merchantEmailId")
    String merchantEmaiID;
    @SerializedName("merchantLogo")
    String merchantLogo;
    @SerializedName("merchantstore")
    String merchantstore;
    @SerializedName("merchantAddress")
    String merchantAddress;
    @SerializedName("merchantLatitude")
    String merchantLatitude;
    @SerializedName("merchantLongitude")
    String merchantLongitude;
    @SerializedName("distance")
    String distance;


    public String getMerchantId() {
        return merchantId;
    }

    public String getMerchantFirstName() {
        return merchantFirstName;
    }

    public String getMerchantLogo() {
        return merchantLogo;
    }

    public String getMerchantstore() {
        return merchantstore;
    }

    public String getMerchantAddress() {
        return merchantAddress;
    }

    public String getMerchantLatitude() {
        return merchantLatitude;
    }

    public String getMerchantLongitude() {
        return merchantLongitude;
    }

    public String getDistance() {
        return distance;
    }
}
