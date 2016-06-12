package com.soffice.clickandpay.Utilty;

import android.content.Context;
import android.content.SharedPreferences;
import android.content.SharedPreferences.Editor;


public class SessionManager {
    private static final String PREF_NAME = "ClickAndPay";
    public static final String DEVICE_ID = "DEVICE_ID";
    public static final String AUTH_KEY = "AUTH_KEY";
    public static final String WALLET_BAL = "WALLET_BAL";
    public static final String MONTH_TRANS="MONTH_TRANS";
    public static final String USER_NAME = "USER_NAME";
    public static final String EMAIL_ID = "EMAIL_ID";
    public static final String MOBILE_NUM = "MOBILE_NUM";
    public static final String DOB="dob";
    public static final String REFERRAL_CODE="referral_code";
    public static final String IS_LOGGEDIN = "IS_LOGGEDIN";
    public static final String PROFILE = "PROFILE";
    public static final String Contacts = "Contacts";
    public static final String Versions = "Versions";
    public static final String MaxContacts = "MaxContacts";
    public static final String GCM_TOKEN="gcm_token";
    public static final String IS_GCM_REGISTERED="is_gcm_registered";
    public static final String DISPLAYED_WALKTHROUGH="displayed_walkthrough";
    private static final String IS_VERIFIED_USER="verified_user";
    private static final String CUSTOMER_IDENTIFIER="customer_identifier";
    SharedPreferences pref;
    Editor editor;
    Context _context;
    int PRIVATE_MODE = 0;

    // Constructor
    public SessionManager(Context context) {
        this._context = context;
        pref = _context.getSharedPreferences(PREF_NAME, PRIVATE_MODE);
        editor = pref.edit();
    }

    public void setDeviceId(String deviceId) {
        editor.putString(DEVICE_ID, deviceId);
        editor.commit();
    }

    public String getDeviceId() {
        return pref.getString(DEVICE_ID, null);
    }

    public void setAuthKey(String authKey) {
        editor.putString(AUTH_KEY, authKey);
        editor.commit();
    }

    public String getAuthKey() {
        return pref.getString(AUTH_KEY, null);
    }

    public void setWalletBal(String bal) {
        editor.putString(WALLET_BAL, bal);
        editor.apply();
    }



    public String getWalletBal() {
        return pref.getString(WALLET_BAL, null);
    }


    public void setEmailId(String email) {
        editor.putString(EMAIL_ID, email);
        editor.commit();
    }

    public String getEmailId() {
        return pref.getString(EMAIL_ID, null);
    }


    public void setMobileNum(String mobile) {
        editor.putString(MOBILE_NUM, mobile);
        editor.commit();
    }

    public String getMobileNum() {
        return pref.getString(MOBILE_NUM, null);
    }

    public void setIsLoggedIn(boolean loggedIn) {
        editor.putBoolean(IS_LOGGEDIN, loggedIn);
        editor.commit();
    }

    public Boolean getIsLoggedIn() {
        return pref.getBoolean(IS_LOGGEDIN, false);
    }

    public void setUserName(String loggedIn) {
        editor.putString(USER_NAME, loggedIn);
        editor.commit();
    }

    public void setDOB(String dob)
    {
        editor.putString(DOB,dob);
        editor.apply();
    }

    public String getDOB()
    {
        return pref.getString(DOB,"");
    }

    public void setReferralCode(String referralCode)
    {
        editor.putString(REFERRAL_CODE,referralCode);
        editor.apply();
    }

    public String getReferralCode()
    {
        return pref.getString(REFERRAL_CODE,"");
    }



    public String getUserName() {
        return pref.getString(USER_NAME, "");
    }


    public void setProfile(String loggedIn) {
        editor.putString(PROFILE, loggedIn);
        editor.commit();
    }

    public String getProfile() {
        return pref.getString(PROFILE, "");
    }


    public String getStoredContactlist() {
        return pref.getString(Contacts, "");
    }

    public void setStoredContactlist(String b) {
        editor.putString(Contacts, b);
        editor.commit();
    }

    public String getStoredVersionsList() {
        return pref.getString(Versions, "");
    }

    public void setStoredVersionsList(String b) {
        editor.putString(Versions, b);

        editor.commit();

    }

    public String getMaxContact() {
        return pref.getString(MaxContacts, "");
    }

    public void setMaxContact(String b) {
        editor.putString(MaxContacts, b);
        editor.commit();
    }

    public void setMonthTrans(String b)
    {
        editor.putString(MONTH_TRANS,b);
        editor.apply();
    }

    public String getMonthTrans()
    {
        return pref.getString(MONTH_TRANS,"");
    }

    public void setGcmToken(String token)
    {
        editor.putString(GCM_TOKEN,token);
        editor.apply();
    }

    public String getGcmToken()
    {
        return pref.getString(GCM_TOKEN,"");
    }

    public void setIsGcmRegistered(boolean IsRegistered)
    {
        editor.putBoolean(IS_GCM_REGISTERED,IsRegistered);
        editor.apply();
    }

    public void setDisplayedWalkthrough(boolean mbool)
    {
        editor.putBoolean(DISPLAYED_WALKTHROUGH,mbool);
        editor.apply();
    }

    public boolean getDisplayedWalkthrough()
    {
        return pref.getBoolean(DISPLAYED_WALKTHROUGH,false);
    }

    public void setIsVerifiedUser(boolean mbool)
    {
        editor.putBoolean(IS_VERIFIED_USER,mbool);
        editor.apply();
    }

    public boolean getIsVerfiedUser()
    {
        return pref.getBoolean(IS_VERIFIED_USER,false);
    }

    public boolean getIsGcmRegistered()
    {
        return  pref.getBoolean(IS_GCM_REGISTERED,false);
    }


    public void setCustomerIdentifier(String identifier)
    {
        editor.putString(CUSTOMER_IDENTIFIER,identifier);
        editor.apply();
    }

    public String getCustomerIdentifier()
    {
        return pref.getString(CUSTOMER_IDENTIFIER,"");
    }

    public void ClearAll()
    {
        Editor edit=pref.edit();
        edit.clear();
        edit.apply();}

}