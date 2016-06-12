package com.soffice.clickandpay.Pojo;


import com.soffice.clickandpay.Utilty.Display;

public class smsfetch implements item {

    boolean checked;

    public boolean isChecked() {
        return checked;
    }

    public void setChecked(boolean checked) {
        this.checked = checked;
    }

    private int sno;

    public int getSno() {
        return sno;
    }

    public void setSno(int sno) {
        this.sno = sno;
    }

    private String _id;
    private int _unread;

    public String get_id() {
        return _id;
    }

    public void set_id(String _id) {
        this._id = _id;
    }

    public int get_unread() {
        return _unread;
    }

    public void set_unread(int _unread) {
        this._unread = _unread;
    }

    public String get_address() {
        return _address;
    }

    public void set_address(String _address) {
        this._address = _address;
    }

    public String get_msg() {
        return _msg;
    }

    public void set_msg(String _msg) {
        this._msg = _msg;
    }

    public String get_readState() {
        return _readState;
    }

    public void set_readState(String _readState) {
        this._readState = _readState;
    }

    public String get_time() {
        return _time;
    }

    public void set_time(String _time) {
        this._time = _time;
    }

    public String get_folderName() {
        return _folderName;
    }

    public void set_folderName(String _folderName) {
        this._folderName = _folderName;
    }

    private String _address;
    private String _person;
    private String _msg;
    private String _readState; //"0" for have not read sms and "1" for have read sms
    private String _time;
    private String _folderName; //type
    private String _protocal;
    private String _SMStype;

    public String get_subject() {
        return _subject;
    }

    public void set_subject(String _subject) {
        this._subject = _subject;
    }

    private String _subject;
    private String _DelStatus;

    public String get_SMStype() {
        return _SMStype;
    }

    public String getId() {
        return _id;
    }

    public String getMsg() {
        return _msg;
    }

    public String getReadState() {
        return _readState;
    }

    public String getTime() {
        return _time;
    }

    public String getFolderName() {
        return _folderName;
    }

    public String get_person() {
        return _person;
    }

    public String get_protocal() {
        return _protocal;
    }

    public String get_DelStatus() {
        return _DelStatus;
    }

    public void set_DelStatus(String _DelStatus) {
        this._DelStatus = _DelStatus;
    }

    public void set_protocal(String _protocal) {
        this._protocal = _protocal;
    }

    public void setId(String id) {
        _id = id;
    }

    public void setMsg(String msg) {
        _msg = msg;
    }

    public void setReadState(String readState) {
        _readState = readState;
    }

    public void setTime(String time) {
        _time = time;
    }

    public void setFolderName(String folderName) {
        _folderName = folderName;
    }

    public void set_person(String _person) {
        this._person = _person;
    }


    //    364---+919676462929---vasu---hi how r u---1---null---1419060944442---1----1
//                       363---     9676462929---   vasu---hi how r u---        1---            null--- 1419060941469---        2---            0
//    public smsfetch(String _id, int Unread, String _address, String name, String _msg, String _readstate, String protocals, String _time, String foldername
//            , String DelStatus, String Subject) {
//        this._id = _id;
//        this._unread = Unread;
//        this._address = _address;
//        this._msg = _msg;
//        this._readState = _readstate;
//        this._protocal = protocals;
//        this._time = _time;
//        _folderName = foldername;
//        this._DelStatus = DelStatus;
//        this._person = name;
//        this._subject = Subject;
////        _SMStype = SMStype;
//
//    }

    public smsfetch() {

    }

    public smsfetch(String name, String address, String message, String Date) {
        this._person = name;
        this._address = address;
        this._msg = message;
        this._time = Date;
    }

    public smsfetch(int sno, int id, String date, String address, String body,
                    int read, int protocal, int type, String subject, int status, String smsType) {
        this.sno = sno;
        this._id = id + "";
        this._address = address;
        this._msg = body;
        this._readState = read + "";
        this._protocal = protocal + "";
        this._time = date;
        this._folderName = type + "";
        this._DelStatus = status + "";
        this._subject = subject;
        this._SMStype = smsType;
        Display.DisplayLogI("SWETHA", _SMStype + "SMStype>>" + smsType);
    }

    @Override
    public boolean isSection() {
        return false;
    }

}

