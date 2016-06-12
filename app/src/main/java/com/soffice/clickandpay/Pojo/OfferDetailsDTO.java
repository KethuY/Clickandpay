package com.soffice.clickandpay.Pojo;

import com.google.gson.annotations.SerializedName;


/**
 * Created by Surya on 23-04-2016.
 */
public class OfferDetailsDTO {
    @SerializedName("CODE")
    String Status;
    @SerializedName("MESSAGE")
    String Message;
    @SerializedName("OFFERS")
    Offeritem Offers;

    public OfferDetailsDTO(String status, String message, Offeritem offers) {
        Status = status;
        Message = message;
        Offers = offers;
    }

    public String getStatus() {
        return Status;
    }

    public void setStatus(String status) {
        Status = status;
    }

    public String getMessage() {
        return Message;
    }

    public void setMessage(String message) {
        Message = message;
    }

    public Offeritem getOffers() {
        return Offers;
    }

    public void setOffers(Offeritem offers) {
        Offers = offers;
    }
}
