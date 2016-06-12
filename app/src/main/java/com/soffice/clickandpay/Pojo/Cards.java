package com.soffice.clickandpay.Pojo;

import java.io.Serializable;

/**
 * Created by Surya on 16-04-2016.
 */
public class Cards implements Serializable
{
    String cardName;
    String cardType;
    String payOptType;
    String payOptDesc;
    String dataAcceptedAt;
    String status;
    String statusMessage;

    public String getCardName() {
        return cardName;
    }

    public void setCardName(String cardName) {
        this.cardName = cardName;
    }

    public String getCardType() {
        return cardType;
    }

    public void setCardType(String cardType) {
        this.cardType = cardType;
    }

    public String getPayOptType() {
        return payOptType;
    }

    public void setPayOptType(String payOptType) {
        this.payOptType = payOptType;
    }

    public String getPayOptDesc() {
        return payOptDesc;
    }

    public void setPayOptDesc(String payOptDesc) {
        this.payOptDesc = payOptDesc;
    }

    public String getDataAcceptedAt() {
        return dataAcceptedAt;
    }

    public void setDataAcceptedAt(String dataAcceptedAt) {
        this.dataAcceptedAt = dataAcceptedAt;
    }

    public String getStatus() {
        return status;
    }

    public void setStatus(String status) {
        this.status = status;
    }

    public String getStatusMessage() {
        return statusMessage;
    }

    public void setStatusMessage(String statusMessage) {
        this.statusMessage = statusMessage;
    }
}
