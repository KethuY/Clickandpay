package com.soffice.clickandpay.Utilty;

import com.soffice.clickandpay.Pojo.MerchantPojo;

/**
 * Created by aditya on 11/2/16.
 */
public class Constants {

    public static String App_Version_Identifier = "V ";
    public static String App_Version = "1.0";
    public static final String SMS_REFRESH = "sms-refresh";
    public static final String CONTACTS_REFRESH = "contacts-refresh";
    public static final String SMS_COMPOSEREFRESH = "sms-composerefresh";
    public static final String TAB_FILTER = "Custom.DB.Refresh";
    public static final String UPDATE_SMILEY = "uPDATE.DB.Refresh";
    public static final String COMMAND = "COMMAND";
    public static MerchantPojo marchant;
    public static final String PARAMETER_SEP = "&";
    public static final String PARAMETER_EQUALS = "=";
    public static final String TRANS_URL = "https://secure.ccavenue.com/transaction/initTrans";
    public static final String KEY_URL="https://test.ccavenue.com/transaction/getRSAKey";
    public static final String SOFFICESERVICE="soffice.service";
    public static final double MIN_ADDMONEY=10.00;
    public static final double MAX_ADDMONEY=9999.00;
    public static final double MIN_SEND_REQ_MONEY=1.00;
    public static final double MAX_SEND_REQ_MONEY=5000.00;


}
