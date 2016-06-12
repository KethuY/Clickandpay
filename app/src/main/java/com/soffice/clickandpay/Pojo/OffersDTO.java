package com.soffice.clickandpay.Pojo;

import com.google.gson.annotations.SerializedName;

import java.util.List;

/**
 * Created by Surya on 22-04-2016.
 */
public class OffersDTO
{
    @SerializedName("CODE")
    String Status;
    @SerializedName("MESSAGE")
    String Message;
    @SerializedName("OFFERS")
    List<Offeritem> Offers;
    @SerializedName("BANNEROFFERS")
    List<Offeritem> banneroffers;

    public OffersDTO(String status, String message, List<Offeritem> offers, List<Offeritem> banneroffers) {
        Status = status;
        Message = message;
        Offers = offers;
        this.banneroffers = banneroffers;
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

    public List<Offeritem> getOffers() {
        return Offers;
    }

    public void setOffers(List<Offeritem> offers) {
        Offers = offers;
    }

    public List<Offeritem> getBanneroffers() {
        return banneroffers;
    }

    public void setBanneroffers(List<Offeritem> banneroffers) {
        this.banneroffers = banneroffers;
    }
}
