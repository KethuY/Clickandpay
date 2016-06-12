package com.soffice.clickandpay.NetWork;

/**
 * Created by aditya on 11/2/16.
 */
public class Urls {

    /* Home URLS */
    public  static final String homeUrl = "https://clknpay.com/cnpmobile/api/";
    //public static final String homeUrl="http://52.77.228.209/cnpmobile/api/";

    /* SignUp URLS */
    public static final String userSignUp_methodName = "userSignUp";
    public static final String userSignUp = homeUrl+"userSignUp";

    /* Signin URLS*/
    public static final String userSignIn_methodName = "userLogin";
    public static final String userSignIn = homeUrl+"userLogin";

    /* Verification URLS*/
    public static final String otpVerification_methodName = "otpVerification";
    public static final String otpVerification = homeUrl+userSignUp_methodName+"/otpVerification";

    /* Set Passcode URLS*/
    public static final String passcode_methodName = "passcode";
    public static final String passcode = homeUrl+"passcode";

    /* Chech Passcode URLS */
    public static final String check_passcode_methodName = "checkPasscode";
    public static final String check_passcode = homeUrl+passcode_methodName+"/checkPasscode";

    /* Marchant URLS */
    public static final String merchant_methodName = "merchant";
    public static final String merchant = homeUrl+"merchant";

    /* Payment URLS */
    public static final String payment_methodName = "payment";
    public static final String payment = homeUrl+"payment";

    /* Feedback URLS */
    public static final String feedback_methodName = "feedback";
    public static final String feedback = homeUrl+"feedback";

    /* Send Money URLS */
    public static final String sendMoney_methodName = "sendMoney";
    public static final String sendMoney = homeUrl+"sendMoney";
    public static final String BufferSendMoney=homeUrl+"sendBufferMoney";

    /* Request Money URLS */
    public static final String requestMoney_methodName = "requestMoney";
    public static final String requestMoney = homeUrl+"requestMoney";

    /* Forgot Password URLS */
    public static final String forgotPassword_methodName = "forgotPasswordMethod";
    public static final String forgotPassword = homeUrl+"forgotPassword";

    /* Forgot Passcode URLS */
    public static final String forgotPasscode_methodName = "forgotPasscode";
    public static final String forgotPasscode = homeUrl+"forgotPasscode";

    /* Change Password URLS */
    public static final String changePassword_methodName = "changePassword";
    public static final String changePassword = homeUrl+"changePassword";

    /* Change Passcode URLS */
    public static final String changePasscode_methodName = "changePasscode";
    public static final String changePasscode = homeUrl+"changePasscode";

    /* Get Profile URLS */
    public static final String getProfile_methodName = "Profile";
    public static final String getProfile = homeUrl+"Profile";

    /* Update Profile URLS */
    public static final String updateProfile_methodName = getProfile_methodName+"update";
    public static final String updateProfile = homeUrl+getProfile_methodName+"/update";

    /* Get Transactions URLS */
    public static final String getTransactions_methodName = "Transactions";
    public static final String getTransactions = homeUrl+"Transactions";

    /*Get Transactions Details URLS*/
    public static final String getTransactions_details_methodName="Transactions_Details";
    public static final String getTransactions_Details=homeUrl+"Transactions/transactionDetails";

    /*Add Money URLS */
    public static final String checkAddMoney_methodName="AddMoney";
    public static final String checkAddMoney=homeUrl+"AddMoney";

    /*Payment Options */
    public static final String getPayment_Opts="https://secure.ccavenue.com/transaction/transaction.do";
    public static final String getPayment_Opts_Method="getPaymentOpts";


    /*Get RSA Key*/
    public static final String getrsakey_url=homeUrl+"GetRSA";
    public static final String testgetrsakey_url="http://122.182.6.216/merchant/GetRSA.jsp";
    public static final String trans_url="https://secure.ccavenue.com/transaction/initTrans";
    public static final String redirect_url=homeUrl+"CcavResponseHandler";

    /*Offers*/
    public static final String getoffers_url=homeUrl+"Offers";
    public static final String offers_method="offers_method";

    /*Offer Details */
    public static final String getoffer_details_url=homeUrl+"Offers/getOfferDetails";
    public static final String getoffer_details_method="offer_details";

    /*Push Notification*/
    public static final String push_store_url=homeUrl+"PushUser/android";
    public static final String push_store_method="push_store_method";
    public static final String push_notifications_list="PushUser/userNotifications";

    /*Forgot Password/Passcode*/
    public static final String forgot_password_url=homeUrl+"forgotPassword";
    public static final String forgot_passcode_url=homeUrl+"forgotPasscode";
    public static final String forgot_password_method="forgotPasswordMethod";
    public static final String forgot_passcode_method="forgotPasscodeMethod";
    public static final String reset_password_url=homeUrl+"resetPassword";
    public static final String reset_passcode_url=homeUrl+"resetPasscode";
    public static final String reset_passcode_method="reset_passcode_method";
    public static final String reset_password_method="reset_password_method";

    /*NearByStores*/
    public static final String near_by_stores_url=homeUrl+"Geolocation";
    public static final String near_by_stores_method="near_by_stores_method";
    public static final String near_me_search_store_url=homeUrl+"Geolocation/StoreSearchByName";
    public static final String near_me_search_method="near_me_search_method";

    /*Last Transaction*/
    public static final String Last_transaction_url=homeUrl+"Transactions/lastTransaction";
    public static final String Last_transaction_method="last_transaction_method";

    /*Resend OTP*/
    public static final String resend_OTP=homeUrl+"otp/resendotp";
    public static final String resend_OTP_Method="resend_otp_method";

    /*Suggest a store*/
    public static final String suggest_a_store_method = "suggestastore";
    public static final String suggest_a_store_url = homeUrl+"SuggestStore";

    /*Support*/
    public static final String support_method = "support_method";
    public static final String support_url = homeUrl+"Support";

    /*Help us to improve*/
    public static final String help_us_improve_url=homeUrl+"help";
    public static final String help_us_improve_method="help_us_improve_method";

}