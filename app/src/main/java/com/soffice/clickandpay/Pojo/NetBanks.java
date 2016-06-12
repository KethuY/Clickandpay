package com.soffice.clickandpay.Pojo;

import android.os.Parcel;
import android.os.Parcelable;

import java.io.Serializable;

/**
 * Created by Surya on 16-04-2016.
 */
public class NetBanks implements Parcelable
{
    String cardName;
    String cardType;
    String payOptType;
    String payOptDesc;
    String dataAcceptedAt;
    String status;
    String statusMessage;

    public NetBanks(String cardName, String cardType, String payOptType, String payOptDesc, String dataAcceptedAt, String status, String statusMessage) {
        this.cardName = cardName;
        this.cardType = cardType;
        this.payOptType = payOptType;
        this.payOptDesc = payOptDesc;
        this.dataAcceptedAt = dataAcceptedAt;
        this.status = status;
        this.statusMessage = statusMessage;
    }

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

    public NetBanks(Parcel in){
        String[] data = new String[3];

        in.readStringArray(data);
        this.cardName = data[0];
        this.cardType = data[1];
        this.payOptType = data[3];
        this.payOptDesc = data[4];
        this.dataAcceptedAt = data[5];
        this.status = data[6];
        this.statusMessage = data[7];
    }

    @Override
    public String toString() {
        return cardName;
    }

    @Override
    public int describeContents(){
        return 0;
    }

    @Override
    public void writeToParcel(Parcel dest, int flags) {
        dest.writeStringArray(new String[] {this.cardName,
                this.cardType,
                this.payOptType,this.payOptDesc,this.dataAcceptedAt,this.status,this.statusMessage});
    }
    public static final Parcelable.Creator CREATOR = new Parcelable.Creator() {
        public NetBanks createFromParcel(Parcel in) {
            return new NetBanks(in);
        }

        public NetBanks[] newArray(int size) {
            return new NetBanks[size];
        }
    };

}
