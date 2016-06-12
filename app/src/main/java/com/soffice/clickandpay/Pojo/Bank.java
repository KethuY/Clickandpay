package com.soffice.clickandpay.Pojo;

import java.io.Serializable;

/**
 * Created by Surya on 12-04-2016.
 */
public class Bank implements Serializable
{
    String BankName;
    String BankLogo;

    public String getBankName() {
        return BankName;
    }

    public void setBankName(String bankName) {
        BankName = bankName;
    }

    public String getBankLogo() {
        return BankLogo;
    }

    public void setBankLogo(String bankLogo) {
        BankLogo = bankLogo;
    }
}
