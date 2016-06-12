package com.soffice.clickandpay.Pojo;

import com.google.gson.annotations.SerializedName;

import java.io.Serializable;

/**
 * Created by Surya on 22-04-2016.
 */
public class Offeritem implements Serializable
{
    private static final long serialVersionUID = -6041415110348316844L;
    @SerializedName("offerId")
    public String OfferID;
    @SerializedName("offerMaxCap")
    public String OfferMaxCap;
    @SerializedName("offerValidity")
    public String OfferValidty;
    @SerializedName("offerPercentage")
    public String OfferPercentage;
    @SerializedName("offerCode")
    public String OfferCode;
    @SerializedName("offerTransactionCap")
    public String OfferTransactionCap;
    @SerializedName("offersDescription")
    public String OfferDescription;
    @SerializedName("offersUploadDocument")
    public String OfferUploadDoc;
    @SerializedName("offersStartDate")
    public String OfferStartDate;
    @SerializedName("offersStatus")
    public String OfferStatus;
    @SerializedName("offersName")
    public String OfferName;
    @SerializedName("offersapplicable")
    public String Offerapplicable;
    @SerializedName("description_tag")
    public String Descriptiontag;
    @SerializedName("actual_price")
    public String Actualprice;
    @SerializedName("discounted_price")
    public String DiscountedPrice;
    @SerializedName("offers_usage")
    public String OfferUsage;
    @SerializedName("offer_popular")
    public String OfferPopular;
    @SerializedName("offerscat")
    public String OfferCat;
    @SerializedName("offer_image")
    public String OfferImage;
    @SerializedName("paid")
    public String OfferPaid;
    @SerializedName("merchantid")
    public String MerchantID;
    @SerializedName("merchantFirstName")
    public String MerchantFirstName;
    @SerializedName("merchantLogo")
    public String MerchantLogo;
    @SerializedName("merchantLatitude")
    public String MerchantLatitude;
    @SerializedName("merchantLongitude")
    public String MerchantLongitude;
    @SerializedName("place")
    public String Place;
    @SerializedName("merchant_categoryname")
    public String MerchantCategory;
    @SerializedName("merchant_cat_background")
    public String MerchantCatBackground;


    public Offeritem(String offerID, String offerMaxCap, String offerValidty, String offerPercentage, String offerCode, String offerTransactionCap, String offerDescription, String offerUploadDoc, String offerStartDate, String offerStatus, String offerName, String offerapplicable, String descriptiontag, String actualprice, String discountedPrice, String offerUsage, String offerPopular, String offerCat, String offerImage, String offerPaid, String merchantID, String merchantFirstName, String merchantLogo, String merchantLatitude, String merchantLongitude, String place, String merchantCategory, String merchantCatBackground) {
        OfferID = offerID;
        OfferMaxCap = offerMaxCap;
        OfferValidty = offerValidty;
        OfferPercentage = offerPercentage;
        OfferCode = offerCode;
        OfferTransactionCap = offerTransactionCap;
        OfferDescription = offerDescription;
        OfferUploadDoc = offerUploadDoc;
        OfferStartDate = offerStartDate;
        OfferStatus = offerStatus;
        OfferName = offerName;
        Offerapplicable = offerapplicable;
        Descriptiontag = descriptiontag;
        Actualprice = actualprice;
        DiscountedPrice = discountedPrice;
        OfferUsage = offerUsage;
        OfferPopular = offerPopular;
        OfferCat = offerCat;
        OfferImage = offerImage;
        OfferPaid = offerPaid;
        MerchantID = merchantID;
        MerchantFirstName = merchantFirstName;
        MerchantLogo = merchantLogo;
        MerchantLatitude = merchantLatitude;
        MerchantLongitude = merchantLongitude;
        Place = place;
        MerchantCategory = merchantCategory;
        MerchantCatBackground = merchantCatBackground;
    }

    public String getOfferID() {
        return OfferID;
    }

    public void setOfferID(String offerID) {
        OfferID = offerID;
    }

    public String getOfferMaxCap() {
        return OfferMaxCap;
    }

    public void setOfferMaxCap(String offerMaxCap) {
        OfferMaxCap = offerMaxCap;
    }

    public String getOfferValidty() {
        return OfferValidty;
    }

    public void setOfferValidty(String offerValidty) {
        OfferValidty = offerValidty;
    }

    public String getOfferPercentage() {
        return OfferPercentage;
    }

    public void setOfferPercentage(String offerPercentage) {
        OfferPercentage = offerPercentage;
    }

    public String getOfferCode() {
        return OfferCode;
    }

    public void setOfferCode(String offerCode) {
        OfferCode = offerCode;
    }

    public String getOfferTransactionCap() {
        return OfferTransactionCap;
    }

    public void setOfferTransactionCap(String offerTransactionCap) {
        OfferTransactionCap = offerTransactionCap;
    }

    public String getOfferDescription() {
        return OfferDescription;
    }

    public void setOfferDescription(String offerDescription) {
        OfferDescription = offerDescription;
    }

    public String getOfferUploadDoc() {
        return OfferUploadDoc;
    }

    public void setOfferUploadDoc(String offerUploadDoc) {
        OfferUploadDoc = offerUploadDoc;
    }

    public String getOfferStartDate() {
        return OfferStartDate;
    }

    public void setOfferStartDate(String offerStartDate) {
        OfferStartDate = offerStartDate;
    }

    public String getOfferStatus() {
        return OfferStatus;
    }

    public void setOfferStatus(String offerStatus) {
        OfferStatus = offerStatus;
    }

    public String getOfferName() {
        return OfferName;
    }

    public void setOfferName(String offerName) {
        OfferName = offerName;
    }

    public String getOfferapplicable() {
        return Offerapplicable;
    }

    public void setOfferapplicable(String offerapplicable) {
        Offerapplicable = offerapplicable;
    }

    public String getDescriptiontag() {
        return Descriptiontag;
    }

    public void setDescriptiontag(String descriptiontag) {
        Descriptiontag = descriptiontag;
    }

    public String getActualprice() {
        return Actualprice;
    }

    public void setActualprice(String actualprice) {
        Actualprice = actualprice;
    }

    public String getDiscountedPrice() {
        return DiscountedPrice;
    }

    public void setDiscountedPrice(String discountedPrice) {
        DiscountedPrice = discountedPrice;
    }

    public String getOfferUsage() {
        return OfferUsage;
    }

    public void setOfferUsage(String offerUsage) {
        OfferUsage = offerUsage;
    }

    public String getOfferPopular() {
        return OfferPopular;
    }

    public void setOfferPopular(String offerPopular) {
        OfferPopular = offerPopular;
    }

    public String getMerchantID() {
        return MerchantID;
    }

    public void setMerchantID(String merchantID) {
        MerchantID = merchantID;
    }

    public String getMerchantFirstName() {
        return MerchantFirstName;
    }

    public void setMerchantFirstName(String merchantFirstName) {
        MerchantFirstName = merchantFirstName;
    }

    public String getMerchantLogo() {
        return MerchantLogo;
    }

    public void setMerchantLogo(String merchantLogo) {
        MerchantLogo = merchantLogo;
    }

    public String getMerchantLatitude() {
        return MerchantLatitude;
    }

    public void setMerchantLatitude(String merchantLatitude) {
        MerchantLatitude = merchantLatitude;
    }

    public String getMerchantLongitude() {
        return MerchantLongitude;
    }

    public void setMerchantLongitude(String merchantLongitude) {
        MerchantLongitude = merchantLongitude;
    }

    public String getPlace() {
        return Place;
    }

    public void setPlace(String place) {
        Place = place;
    }

    public String getMerchantCategory() {
        return MerchantCategory;
    }

    public void setMerchantCategory(String merchantCategory) {
        MerchantCategory = merchantCategory;
    }

    public String getMerchantCatBackground() {
        return MerchantCatBackground;
    }

    public void setMerchantCatBackground(String merchantCatBackground) {
        MerchantCatBackground = merchantCatBackground;
    }

    public String getOfferCat() {
        return OfferCat;
    }

    public void setOfferCat(String offerCat) {
        OfferCat = offerCat;
    }

    public String getOfferImage() {
        return OfferImage;
    }

    public void setOfferImage(String offerImage) {
        OfferImage = offerImage;
    }

    public String getOfferPaid() {
        return OfferPaid;
    }

    public void setOfferPaid(String offerPaid) {
        OfferPaid = offerPaid;
    }
}
