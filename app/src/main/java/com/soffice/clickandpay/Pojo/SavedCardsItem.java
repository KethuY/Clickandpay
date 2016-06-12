package com.soffice.clickandpay.Pojo;

/**
 * Created by Malladi-Bhai on 3/11/2016.
 */
public class SavedCardsItem {

    String bank_name;
    String card_type;
    String num_part1;
    String num_part4;
    String customer_name;

    public String getBank_name() {
        return bank_name;
    }

    public void setBank_name(String bank_name) {
        this.bank_name = bank_name;
    }

    public String getCard_type() {
        return card_type;
    }

    public void setCard_type(String card_type) {
        this.card_type = card_type;
    }

    public String getNum_part1() {
        return num_part1;
    }

    public void setNum_part1(String num_part1) {
        this.num_part1 = num_part1;
    }

    public String getNum_part4() {
        return num_part4;
    }

    public void setNum_part4(String num_part4) {
        this.num_part4 = num_part4;
    }

    public String getCustomer_name() {
        return customer_name;
    }

    public void setCustomer_name(String customer_name) {
        this.customer_name = customer_name;
    }
}
