package com.soffice.clickandpay.NetWork;

/**
 * Created by Aditya Sharma Malladi on 12/9/15.
 */
public class UrlDisplay {

    //    Request Parameters:  JSON Object
//    {"firstName":"Ajay","emailId":"ajay.chada@yahoo.com","mobileNumber":"9949056744","password":"123456”}
//        Response Format:
//        {"code":100,"message":"Registration Successful","status":"SUCCESS"}


    public static final String success_code = "100";

    /*
    *   REGISTER SERVICE IN SIGNUP ACTIVITY
    */
    public static final String Registration_firstName = "firstName";
    public static final String Registration_emailId = "emailId";
    public static final String Registration_mobileNumber = "mobileNumber";
    public static final String Registration_password = "password";
    public static final String Registration_gender = "gender";
    public static final String Registration_methodName = "register";
    public static final String Registration = "http://103.5.16.98:9595/kalakar/register";


    /*
    *   EMAILID AVAILABLE SERVICE IN SIGNUP ACTIVITY
    */
    public static final String Email_ID_Avalible_methodName = "verifyEmailIdIsAvailable";
    public static final String Email_Id_Avalible = "http://103.5.16.98:9595/kalakar/verifyEmailIdIsAvailable?emailId=";


    /*
    *   EMAILID CONFORMATION SERVICE IN SIGNUP ACTIVITY
    */
    public static final String Email_id_Confirmation_emailId = "emailId";
    public static final String Email_id_Confirmation_methodName = "confirmEmailId";
    public static final String Email_id_Confirmation = "http://103.5.16.98:9595/kalakar/confirmEmailId?emailId=";


    /*
    *   LOGIN SERVICE IN SIGNUP ACTIVITY
    */
    public static final String Login_emailId = "emailId";
    public static final String Login_password = "password";
    public static final String Login_methodName = "login";
    public static final String Login = "http://103.5.16.98:9595/kalakar/login?";


    /*
    *   OTP SERVICE IN SIGNUP ACTIVITY
    */
    public static final String Otp_methodName = "sendVerificationCodeToMobile";
    public static final String Otp = "http://103.5.16.98:9595/kalakar/sendVerificationCodeToMobile?mobileNumber=";//?mobileNumber=9949056744


    /*
    *   OTP VERIFICATION SERVICE IN SIGNUP ACTIVITY
    */
    public static final String OtpVerification_verificationCode = "verificationCode";
    public static final String OtpVerification_mobileNumber = "mobileNumber";
    public static final String OtpVerification_methodName = "verifyVerificationCodeToMobile";
    public static final String OtpVerification = "http://103.5.16.98:9595/kalakar/verifyVerificationCodeToMobile?";//?verificationCode=609945&mobileNumber=9949056744


    /*
    *   SPECIALIZATIONS SERVICE IN SIGNUP ACTIVITY
    */
    public static final String Specilaztion_methodName = "specializations";
    public static final String Specilaztion = "http://103.5.16.98:9595/kalakar/specializations"; //N/A JsonArray


    /*
    *   OCCASIONS SERVICE IN SIGNUP ACTIVITY
    */
    public static final String Occasions_methodName = "eventTypes";
    public static final String Occasions = "http://103.5.16.98:9595/kalakar/eventTypes";// N/A JsonArray


    /*
    *   FORGOTPASSWORD SERVICE IN SIGNUP ACTIVITY
    */
    public static final String ForgotPassword_methodName = "forgotCustomerPassword";
    public static final String ForgotPassword = "http://103.5.16.98:9595/jilmore/forgotCustomerPassword?emailId=";// EmailId JsonObject


    /*
    *   CHANGE PASSWORD SERVICE IN SIGNUP ACTIVITY
    */
    public static final String changePassword_methodName = "changePassword";
    public static final String ChangePassword = " http://103.5.16.98:9595/kalakar/changePassword";// emailId, newPassword and  oldPassword


    /*
    *   GET COMPLETE PROFILE SERVICE IN SIGNUP ACTIVITY
    */
    public static final String GetCompleteProfile_methodName = "getCompleteUserSessionInfo";
    public static final String GetCompleteProfile = "http://103.5.16.98:9595/jilmore/getCompleteUserSessionInfo?kalakarEmailId=";// kalakarEmailId


    /*
    *   BOOLEN RESPONSE STRINGS
    */
    public static final String true_string = "true";
    public static final String false_string = "false";


}