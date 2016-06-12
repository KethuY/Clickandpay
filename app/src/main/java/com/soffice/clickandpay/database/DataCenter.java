package com.soffice.clickandpay.database;

import android.content.ContentResolver;
import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteException;
import android.database.sqlite.SQLiteOpenHelper;
import android.database.sqlite.SQLiteStatement;
import android.os.AsyncTask;
import android.os.Build;
import android.os.Environment;
import android.provider.ContactsContract;

import com.soffice.clickandpay.ClickandPay;
import com.soffice.clickandpay.Utilty.Display;
import com.soffice.clickandpay.Utilty.SessionManager;

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;


public class DataCenter extends SQLiteOpenHelper {

    private static final int DATABASE_VERSION = 1;
    private static final String DB_Path = "/Android/data/com.soffice.clickandpay/databases/";
    private static final String DB_NAME = "click_and_pay.db";
    /***
     * CONTACTS TABLE
     ***/
    private static final String CONTACTS_TABLE = "contacts_table";
    private static final String CONTACTID = "contactid";
    private static final String NAME = "name";
    private static final String PHOTO_URI = "photo_uri";
    private static final String NUMBER = "number";
    private static final String STYLE = "style";
    private static final String FAVORITE = "favorite";
    private static int DB_VERSION = 1;
    public String ValidStr = "^[0-9\\+]*$";
    Context context;
    boolean dbExist;
    /**
     * Copies your database from your local assets-folder to the just created
     * empty database in the system folder, from where it can be accessed and
     * handled. This is done by transferring bytestream.
     */
    InputStream myInput;


    InsertsmsData task;
    SessionManager session;
    Cursor cur = null, pCur = null;
    SQLiteDatabase mYDB;

    public DataCenter(Context _context) {
        super(_context, DB_NAME, null, DATABASE_VERSION);
        context = _context;

        try {
            createDataBase();
        } catch (IOException e) {
            e.printStackTrace();
        }

    }

    public boolean insertContacts(Cursor pCur, SQLiteDatabase myWritableDb) {

        // SQLiteDatabase myWritableDb = null;
        ContentValues values1 = null;
        String phone = null;
        String name = null;
        String image_uri = null;
        int style = 0;
        Cursor pCur1 = null;
        long contact_Id,maxContactid = 0;
        SessionManager session;
        String mainversion;
        try {
            session = ClickandPay.getInstance().getSession();
            if (myWritableDb != null) {
                myWritableDb.beginTransaction();
                values1 = new ContentValues();

                Display.DisplayLogI("ADITYA", "Insert spCur : " + pCur.getCount());


                while (pCur.moveToNext()) {
//                    Display.DisplayLogI("ADITYA", "Insert1 version : " + pCur.getString(pCur.getColumnIndex(ContactsContract.CommonDataKinds.Phone.DATA_VERSION)));
                    Display.DisplayLogI("ADITYA", "NOrmal conatct id : " + pCur.getString(pCur.getColumnIndex(ContactsContract.CommonDataKinds.Phone.CONTACT_ID)));
                    String contactid=pCur.getString(pCur.getColumnIndex(ContactsContract.CommonDataKinds.Phone.CONTACT_ID));
//                    Display.DisplayLogI("ADITYA", "Insert spCur : " + pCur.getString(pCur.getColumnIndex(ContactsContract.CommonDataKinds.Phone.CONTACT_STATUS_TIMESTAMP)));
                    if ((pCur.getString(pCur.getColumnIndex(ContactsContract.CommonDataKinds.Phone.DATA4)) == null)) {
                        phone = pCur.getString(pCur.getColumnIndex(ContactsContract.CommonDataKinds.Phone.DATA1));
                    } else {
                        phone = pCur.getString(pCur.getColumnIndex(ContactsContract.CommonDataKinds.Phone.DATA4));
                    }
                    if (phone != null && !phone.equalsIgnoreCase("")) {
                        phone = phone.trim();
                        phone = phone.replaceAll("\\(", "");
                        phone = phone.replaceAll("\\) ", "");
                        phone = phone.replaceAll("\\)", "");
                        phone = phone.replaceAll("\\-", "");

                        Display.DisplayLogI("ADITYA", "insert phone :: " + phone);

                        if (phone.length() == 10) {
                            phone = "+91" + phone;
                        } else if (phone.length() == 11) {
                            if (phone.substring(0, 1).matches("0")) {
                                phone = phone.replaceFirst("0", "+91");
                            }
                        } else if (phone.length() == 12) {
                            if (phone.substring(0, 2).matches("91")) {
                                phone = phone.replaceFirst("91", "+91");
                            }
                        }
                        if (phone.contains(" ")) {
                            String st[] = phone.split(" ");
                            String ph = "";
                            for (int l = 0; l < st.length; l++) {
                                ph = ph + st[l];
                            }
                            phone = ph;
                        }
                        phone = phone.trim();

                        name = pCur.getString(pCur.getColumnIndex(ContactsContract.CommonDataKinds.Phone.DISPLAY_NAME));
                        Display.DisplayLogI("ADITYA", "Insert CONTACTnew " + contactid);
                        if (name != null && !name.equals("") && !phone.equals("")) {
                            image_uri = null;
                            values1.put(CONTACTID, contactid);
                            values1.put(NAME, name);
                            values1.put(NUMBER, phone.replaceAll(" ", ""));
                            values1.put(STYLE, style);
                            values1.put(FAVORITE, pCur.getString(pCur.getColumnIndex(ContactsContract.CommonDataKinds.Phone.STARRED)));
                            style++;
                            if (style > 7) {
                                style = 0;
                            }
                            image_uri = pCur.getString(pCur.getColumnIndex(ContactsContract.CommonDataKinds.Phone.PHOTO_URI));
                            values1.put(PHOTO_URI, image_uri);
                            myWritableDb.insert(CONTACTS_TABLE, null, values1);
                        }
                    }
                }
                myWritableDb.setTransactionSuccessful();
                myWritableDb.endTransaction();
            }
            return true;
        } catch (Exception e) {
            e.printStackTrace();
            myWritableDb.endTransaction();
            return false;
        } finally {
//            if (pCur != null && !pCur.isClosed()) {
//                pCur.close();
//            }
            values1 = null;
            phone = null;
            name = null;
            image_uri = null;
            style = 0;
        }
    }



    public boolean insertContactsNew(Cursor pCur, SQLiteDatabase myWritableDb) {

        // SQLiteDatabase myWritableDb = null;
        ContentValues values1 = null;
        String phone = null;
        String name = null;
        String image_uri = null;
        int style = 0;
        Cursor pCur1 = null;
        long contact_Id,maxContactid = 0;
        SessionManager session;
        String mainversion;
        try {
            session = ClickandPay.getInstance().getSession();
            if (myWritableDb != null) {
                myWritableDb.beginTransaction();
                values1 = new ContentValues();

                Display.DisplayLogI("ADITYA", "Insert spCur : " + pCur.getCount());
                ContentResolver cr = context.getContentResolver();
                while (pCur.moveToNext()) {
//                    Display.DisplayLogI("ADITYA", "Insert1 version : " + pCur.getString(pCur.getColumnIndex(ContactsContract.CommonDataKinds.Phone.DATA_VERSION)));
//                    Display.DisplayLogI("ADITYA", "NOrmal conatct id : " + pCur.getString(pCur.getColumnIndex(ContactsContract.CommonDataKinds.Phone.CONTACT_ID)));
                    Display.DisplayLogI("ADITYA", "Insert spCur : " + pCur.getString(pCur.getColumnIndex(ContactsContract.CommonDataKinds.Phone.CONTACT_STATUS_TIMESTAMP)));
                    String contact_id=pCur.getString(pCur.getColumnIndex(ContactsContract.CommonDataKinds.Phone.CONTACT_ID));
                    if ((pCur.getString(pCur.getColumnIndex(ContactsContract.CommonDataKinds.Phone.DATA4)) == null)) {
                        phone = pCur.getString(pCur.getColumnIndex(ContactsContract.CommonDataKinds.Phone.DATA1));
                    } else {
                        phone = pCur.getString(pCur.getColumnIndex(ContactsContract.CommonDataKinds.Phone.DATA4));
                    }
                    if (phone != null && !phone.equalsIgnoreCase("")) {
                        phone = phone.trim();
                        phone = phone.replaceAll("\\(", "");
                        phone = phone.replaceAll("\\) ", "");
                        phone = phone.replaceAll("\\)", "");
                        phone = phone.replaceAll("\\-", "");
                        Display.DisplayLogI("ADITYA", "Insert CONTACTnew " + contact_id);
                        Display.DisplayLogI("ADITYA", "insert phone :: " + phone);

                        if (phone.length() == 10) {
                            phone = "+91" + phone;
                        } else if (phone.length() == 11) {
                            if (phone.substring(0, 1).matches("0")) {
                                phone = phone.replaceFirst("0", "+91");
                            }
                        } else if (phone.length() == 12) {
                            if (phone.substring(0, 2).matches("91")) {
                                phone = phone.replaceFirst("91", "+91");
                            }
                        }
                        if (phone.contains(" ")) {
                            String st[] = phone.split(" ");
                            String ph = "";
                            for (int l = 0; l < st.length; l++) {
                                ph = ph + st[l];
                            }
                            phone = ph;
                        }
                        phone = phone.trim();

                        name = pCur.getString(pCur.getColumnIndex(ContactsContract.CommonDataKinds.Phone.DISPLAY_NAME));

                        if (name != null && !name.equals("") && !phone.equals("")) {
                            image_uri = null;
                            values1.put(CONTACTID, contact_id);
                            values1.put(NAME, name);
                            values1.put(NUMBER, phone.replaceAll(" ", ""));
                            values1.put(STYLE, style);
                            values1.put(FAVORITE, pCur.getString(pCur.getColumnIndex(ContactsContract.CommonDataKinds.Phone.STARRED)));
                            style++;
                            if (style > 7) {
                                style = 0;
                            }
                            image_uri = pCur.getString(pCur.getColumnIndex(ContactsContract.CommonDataKinds.Phone.PHOTO_URI));
                            values1.put(PHOTO_URI, image_uri);
                            myWritableDb.insert(CONTACTS_TABLE, null, values1);
                        }
                    }
                }
                myWritableDb.setTransactionSuccessful();
                myWritableDb.endTransaction();
            }
            return true;
        } catch (Exception e) {
            e.printStackTrace();
            myWritableDb.endTransaction();
            return false;
        } finally {
//            if (pCur != null && !pCur.isClosed()) {
//                pCur.close();
//            }
            values1 = null;
            phone = null;
            name = null;
            image_uri = null;
            style = 0;
        }
    }

    public Cursor getContacts(SQLiteDatabase myWritableDb1) {
        String statment = null;
        // SQLiteDatabase myWritableDb1 = null;
        Cursor cur = null;
        try {
                statment = "SELECT  favorite as _id,number,name,style,contactid,photo_uri  FROM contacts_table group by number order by lower(name) ";
                // myWritableDb1 = SQLiteDatabase.openDatabase(getDatabasePath(),
                // null, SQLiteDatabase.OPEN_READWRITE);
                // myWritableDb1=dbManager.getDb();
                // if(DataBaseManager.getInstance()==null){
                // }
                // myWritableDb1 = DataBaseManager.getInstance().openDatabase();
                try {
                    cur = myWritableDb1.rawQuery(statment, null);
                } catch (Exception e) {
                    Display.DisplayLogI("ADITYA", "LOCAL DB CONTACTS DataBaseManager.getInstance() :: " + DataBaseManager.getInstance());
                    if (DataBaseManager.getInstance() != null) {
                        myWritableDb1 = DataBaseManager.getInstance().openDatabase();
                    } else {
                        DataBaseManager.initializeInstance(this);
                        myWritableDb1 = DataBaseManager.getInstance().openDatabase();
                    }

                    if(myWritableDb1 == null){
                        DataBaseManager.initializeInstance(this);
                        myWritableDb1 = DataBaseManager.getInstance().openDatabase();
                    }
                    Display.DisplayLogI("ADITYA", "LOCAL DB CONTACTS myWritableDb1 :: " + myWritableDb1);
                    cur = myWritableDb1.rawQuery(statment, null);
                }
            Display.DisplayLogI("ADITYA", "LOCAL DB CONTACTS COUNT :: " + cur.getCount());
            return cur;
        } catch (Exception e) {
            e.printStackTrace();
            return null;
        } finally {
            // dbManager.close();
            // DataBaseManager.getInstance().closeDatabase();
            // statment = null;
            // myWritableDb1 = null;
            // cur = null;
        }
    }

    public boolean dropandInsertContacts(SQLiteDatabase myWritableDb) {
        Display.DisplayLogI("ADITYA", "dropandInsertContacts");
        String phone = null;
        String name = null;
        String image_uri = null;
        int style = 0;
        Cursor pCur = null, pCur1 = null;
        try {
            if (myWritableDb != null) {
                try {
                    myWritableDb.beginTransaction();
                } catch (Exception e) {
                    if (DataBaseManager.getInstance() != null) {
                        myWritableDb = DataBaseManager.getInstance().openDatabase();
                    } else {
                        DataBaseManager.initializeInstance(this);
                        myWritableDb = DataBaseManager.getInstance().openDatabase();
                    }
                    try {
                        myWritableDb.beginTransaction();
                    } catch (Exception e1) {
                        myWritableDb = DataBaseManager.getInstance().openDatabase();
                        myWritableDb.beginTransaction();
                    }
                }
                myWritableDb.delete(CONTACTS_TABLE, null, null);
                ContentResolver cr = context.getContentResolver();
                pCur = cr.query(ContactsContract.CommonDataKinds.Phone.CONTENT_URI,
                        null, ContactsContract.CommonDataKinds.Phone.CONTACT_ID,
                        null, ContactsContract.CommonDataKinds.Phone.DISPLAY_NAME + " COLLATE NOCASE");
                ContentValues values1 = new ContentValues();
                assert pCur != null;
                String sql = "INSERT INTO " + CONTACTS_TABLE + "  (contactid,name,photo_uri,number,style,favorite) VALUES (?,?,?,?,?,?);";
                SQLiteStatement statement = myWritableDb.compileStatement(sql);

                /*pCur1 = cr.query(ContactsContract.RawContacts.CONTENT_URI, new String[]{ContactsContract.RawContacts.CONTACT_ID, ContactsContract.RawContacts.VERSION, ContactsContract.RawContacts.DELETED}, null, null, ContactsContract.RawContacts.CONTACT_ID + " DESC");
                int i = 1;
                while (pCur1.moveToNext()) {

                    Display.DisplayLogI("ADITYA", "Raw position : " + i);
                    Display.DisplayLogI("ADITYA", "Raw version : " + pCur1.getString(pCur1.getColumnIndex(ContactsContract.RawContacts.VERSION)));
                    Display.DisplayLogI("ADITYA", "Raw contact : " + pCur1.getString(pCur1.getColumnIndex(ContactsContract.RawContacts.CONTACT_ID)));
                    Display.DisplayLogI("ADITYA", "Raw Isdeleted : " + pCur1.getString(pCur1.getColumnIndex(ContactsContract.RawContacts.DELETED)));
                    i++;
                }*/
//                Display.DisplayLogI("ADITYA", "CUROr size spCur11111 : " + pCur1.getCount() + "-------------------- " + pCur.getCount());
                while (pCur.moveToNext()) {
//                    pCur1.moveToNext();
//
////                    Display.DisplayLogI("ADITYA", "Normal version : " + pCur.getString(pCur.getColumnIndex(ContactsContract.CommonDataKinds.Phone.DATA_VERSION)));
//                    Display.DisplayLogI("ADITYA", "Normal conatct id : " + pCur.getString(pCur.getColumnIndex(ContactsContract.CommonDataKinds.Phone.CONTACT_ID)));

                    if ((pCur.getString(pCur.getColumnIndex(ContactsContract.CommonDataKinds.Phone.DATA4)) == null)) {
                        phone = pCur.getString(pCur.getColumnIndex(ContactsContract.CommonDataKinds.Phone.DATA1));
                    } else {
                        phone = pCur.getString(pCur.getColumnIndex(ContactsContract.CommonDataKinds.Phone.DATA4));
                    }
                    Display.DisplayLogI("ADITYA", "Insert1 spCurnormal : " + phone);
                    if (phone != null && !phone.equalsIgnoreCase("")) {
                        Display.DisplayLogI("ADITYA", "drop phone : " + phone);
                        phone = phone.trim();
                        phone = phone.replaceAll("\\(", "");
                        phone = phone.replaceAll("\\) ", "");
                        phone = phone.replaceAll("\\)", "");
                        phone = phone.replaceAll("\\-", "");

                        if (phone.length() == 10) {
                            phone = "+91" + phone;
                        } else if (phone.length() == 11) {
                            if (phone.substring(0, 1).matches("0")) {
                                phone = phone.replaceFirst("0", "+91");
                            }
                        } else if (phone.length() == 12) {
                            if (phone.substring(0, 2).matches("91")) {
                                phone = phone.replaceFirst("91", "+91");
                            }
                        }

                        if (phone.contains(" ")) {
                            String st[] = phone.split(" ");
                            String ph = "";
                            for (int l = 0; l < st.length; l++) {
                                ph = ph + st[l];
                            }
                            phone = ph;
                        }

                        phone = phone.trim();
                        name = pCur.getString(pCur.getColumnIndex(ContactsContract.CommonDataKinds.Phone.DISPLAY_NAME));
                        if (name != null && !name.equals("") && phone != null
                                && !phone.equals("")) {

                            image_uri = pCur.getString(pCur.getColumnIndex(ContactsContract.CommonDataKinds.Phone.PHOTO_URI));
                            statement.clearBindings();
                            statement.bindLong(1, pCur.getInt(pCur.getColumnIndex(ContactsContract.CommonDataKinds.Phone.CONTACT_ID)));
                            statement.bindString(2, name);
                            if (image_uri == null) {
                                statement.bindString(3, "");
                            } else {
                                statement.bindString(3, image_uri);
                            }
                            statement.bindString(4, phone.replaceAll(" ", ""));
                            statement.bindLong(5, style);
                            statement.bindLong(6, pCur.getInt(pCur.getColumnIndex(ContactsContract.CommonDataKinds.Phone.STARRED)));
//                            SQLiteStatement statement=new SQLiteStatem;
//                            values1.put(CONTACTID, pCur.getString(pCur.getColumnIndex(ContactsContract.CommonDataKinds.Phone.CONTACT_ID)));
//                            values1.put(NAME, name);
//                            values1.put(FAVORITE, pCur.getString(pCur.getColumnIndex(ContactsContract.CommonDataKinds.Phone.STARRED)));
//                            values1.put(NUMBER, phone.replaceAll(" ", ""));
//                            values1.put(STYLE, style);
                            style++;
                            if (style > 7) {
                                style = 0;
                            }


                            try {
                                statement.executeInsert();
//                                myWritableDb.insert(CONTACTS_TABLE, null, values1);
                            } catch (Exception e) {
                                if (DataBaseManager.getInstance() != null) {
                                    DataBaseManager.getInstance().openDatabase();
                                } else {
                                    DataBaseManager.initializeInstance(this);
                                    DataBaseManager.getInstance().openDatabase();
                                }
                                statement.executeInsert();
//                                myWritableDb.insert(CONTACTS_TABLE, null, values1);
                            }
                        }
                    }
                }

                myWritableDb.setTransactionSuccessful();
//                myWritableDb.endTransaction();
            }
            return true;
        } catch (Exception e) {
            e.printStackTrace();
            return false;
        } finally {
            if (myWritableDb != null) {
                myWritableDb.endTransaction();
            }
            if (pCur != null && !pCur.isClosed()) {
                pCur.close();
                pCur = null;
            }
            phone = null;
            name = null;
            image_uri = null;
        }
    }

    // ///////////////////// CREATE DB FROM ASSETS ////////////////////////


    public void createDataBase() throws IOException {

        File file = new File(getDatabasePath());
        if (file.exists() && !file.isDirectory()) {
            dbExist = databaseExists();
            Display.DisplayLogI("ADITYA", "DB EXISTTTTTTTTTTTTT");
        }

        if (dbExist) {
            // do nothing , database already exists
            SQLiteDatabase db = null;
            try {

                Display.DisplayLogI("ADITYA", "DB EXISTTTTTTTTTTTTT IFFFFFF");
                db = SQLiteDatabase.openDatabase(getDatabasePath(), null, SQLiteDatabase.OPEN_READWRITE);
            } catch (Exception e) {

                Display.DisplayLogI("ADITYA", "DB EXISTTTTTTTTTTTTT IFFFFFF catchhh");
                e.printStackTrace();
//                file.delete();
//                PrintMsg.SetToast(context, "Permissions are not given to app. PLease enable app permissions in settings..", -1, 0, 0);
//                createDataBase();
//                System.exit(0);

                Display.DisplayLogI("ADITYA", "DB EXISTTTTTTTTTTTTT 2nd Time" + db);
                /*try{
                    db = SQLiteDatabase.openDatabase(getDatabasePath(), null, SQLiteDatabase.OPEN_READWRITE);


                    Display.DisplayLogI("ADITYA", "DB EXISTTTTTTTTTTTTT IFFFFFF 1111");

                    if (db.getVersion() == 1 || db.getVersion() == 2 || db.getVersion() == 3) {
                        db.execSQL(CREATE_TABLE);
                        db.execSQL(Create_Way2NewsTable);
//                    db.execSQL("drop table if exists Bookmarks_table");
                        db.execSQL(CREATE_BOOKMARKS_TABLE);
                        Display.DisplayLogI("ADITYA", "DB CREATE");
                        db.execSQL(Create_Waynewslang_Table);
                        db.execSQL(Create_WaynewsData_Table);
                        db.execSQL("drop table if exists notificationtable");
                        db.setVersion(DB_VERSION);
                    }
                }catch (Exception err){
                    err.printStackTrace();
                }*/


            } finally {

                if (db != null) {
                    db.close();
                    db = null;
                }
                // DataBaseManager.getInstance().closeDatabase();
            }

        } else {

            /**
             * By calling this method and empty database will be created into
             * the default system path of your application so we are gonna be
             * able to overwrite that database with our database.
             */
            // this.getReadableDatabase();
            // this.close();

            try {
                Display.DisplayLogI("ADITYA", "DB EXISTTTTTTTTTTTTT ELSEEEEE ");
                copyDataBase();
            } catch (IOException e) {

                e.printStackTrace();
               /* Intent intent = new Intent();
                intent.setAction(Settings.ACTION_APPLICATION_DETAILS_SETTINGS);
                Uri uri = Uri.fromParts("package", context.getPackageName(), null);
                intent.setData(uri);
                intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
                context.startActivity(intent);*/
            }
        }

    }

    /**
     * Check if the database already exist to avoid re-copying the file each
     * +
     * time you open the application.
     *
     * @return true if it exists, false if it doesn't
     */
    private boolean databaseExists() {

        SQLiteDatabase checkDB = null;

        try {
            String myPath = getDatabasePath();
            checkDB = SQLiteDatabase.openDatabase(myPath, null,
                    SQLiteDatabase.NO_LOCALIZED_COLLATORS
                            | SQLiteDatabase.OPEN_READONLY);

        } catch (SQLiteException e) {
        } finally {
            if (checkDB != null) {
                checkDB.close();

            }
        }

        return checkDB != null ? true : false;

    }

    public boolean deleteDB() {
        try {
            boolean b = databaseExists();
            if (b) {
                File ft = new File(Environment.getExternalStorageDirectory() + DB_Path
                        + DB_NAME);
                if (ft.exists()) {
                    ft.delete();
                    return true;
                } else {
                    return true;
                }
            } else {
                return true;
            }
        } catch (Exception e) {
            e.printStackTrace();
            return false;
        }
    }

    private void copyDataBase() throws IOException {

        Display.DisplayLogI("ADITYA", "DB copyDataBase 11111 ");
        File myDirectory = new File(Environment.getExternalStorageDirectory(),
                DB_Path);


        Display.DisplayLogI("ADITYA", "DB copyDataBase 222222 ");
        //
        if (!myDirectory.exists()) {
            Display.DisplayLogI("ADITYA", "DB copyDataBase 222222-1111111");
            myDirectory.mkdirs();
        }
        Display.DisplayLogI("ADITYA", "DB copyDataBase 222222-222222");
        File ft = new File(Environment.getExternalStorageDirectory() + DB_Path
                + DB_NAME);
        if (!ft.exists()) {
            Display.DisplayLogI("ADITYA", "DB copyDataBase 222222-333333");
            ft.createNewFile();
        }

        Display.DisplayLogI("ADITYA", "DB copyDataBase 3333333 ");
        myInput = context.getAssets().open(DB_NAME);

        Display.DisplayLogI("ADITYA", "DB copyDataBase 444444 ");
        // Path to the just created empty db
        String outFileName = getDatabasePath();

        Display.DisplayLogI("ADITYA", "DB copyDataBase 555555 ");
        OutputStream myOutput = new FileOutputStream(outFileName);

        Display.DisplayLogI("ADITYA", "DB copyDataBase 666666 ");
        // transfer bytes from the inputfile to the outputfile
        byte[] buffer = new byte[1024];

        Display.DisplayLogI("ADITYA", "DB copyDataBase 77777 ");
        int length;
        while ((length = myInput.read(buffer)) > 0) {
            myOutput.write(buffer, 0, length);
            Display.DisplayLogI("ADITYA", "DB copyDataBase 888888 ");
        }

        Display.DisplayLogI("ADITYA", "DB copyDataBase 999999 ");
        SQLiteDatabase checkDB = null; // get a reference to the db.

        try {

            Display.DisplayLogI("ADITYA", "DB copyDataBase 444444 trytrytrytry");
            // checkDB = DataBaseManager.getInstance().openDatabase();
            //noinspection PointlessBitwiseExpression
            checkDB = SQLiteDatabase.openDatabase(getDatabasePath(), null, SQLiteDatabase.NO_LOCALIZED_COLLATORS | SQLiteDatabase.OPEN_READWRITE);


            Display.DisplayLogI("ADITYA", "DB copyDataBase 5555555 trytrytrytry");

            // once the db has been copied, set the new version..
            checkDB.setVersion(DATABASE_VERSION);
            Display.DisplayLogI("ADITYA", "DB copyDataBase 6666666 trytrytrytry");
            //
            // checkDB.execSQL(CREATE_TABLE);
            // checkDB.execSQL(Create_Way2NewsTable);

        } catch (SQLiteException e) {
            Display.DisplayLogI("ADITYA", "DB copyDataBase 6666666 catch chatch");

            if (ft.exists()) {
                ft.delete();
                Display.DisplayToast(context, "Permissions are not given to app. PLease enable app permissions in settings..");
                createDataBase();
            }else{
                createDataBase();
            }

            DataBaseManager.initializeInstance(this);

            new InsertsmsData().execute();

            // database does?t exist yet.
            e.printStackTrace();
        } finally {
            // DataBaseManager.getInstance().closeDatabase();
            if (checkDB != null) {
                checkDB.close();
                checkDB = null;
            }
        }

        // Close the streams
        myOutput.flush();
        myOutput.close();
        myInput.close();

    }


    public Cursor getlikecontactdata(String inputtext,
                                     SQLiteDatabase myWritableDb) {

        // SQLiteDatabase myWritableDb = null;
        String statement = null;
        Cursor cur1 = null;

        try {
            // myWritableDb3 = SQLiteDatabase.openDatabase(getDatabasePath(),
            // null, SQLiteDatabase.OPEN_READONLY);

            // myWritableDb = dbManager.getDb();
            if (myWritableDb != null) {
                if (inputtext != null) {
                    inputtext = inputtext.replaceAll("'", "''");

                    statement = "select distinct number,name,favorite as _id,style,photo_uri from contacts_table where (name like '%"
                            + inputtext
                            + "%'  or number like '%"
                            + inputtext
                            + "%') group by number order by lower(name)";
                    try {
                        cur1 = myWritableDb.rawQuery(statement, null);
                    } catch (Exception e) {
                        if (DataBaseManager.getInstance() != null) {
                            myWritableDb = DataBaseManager.getInstance().openDatabase();
                        } else {
                            DataBaseManager.initializeInstance(this);
                            myWritableDb = DataBaseManager.getInstance().openDatabase();
                        }
                        cur1 = myWritableDb.rawQuery(statement, null);
                    }
                }
            }
            return cur1;
        } catch (Exception e) {
            e.printStackTrace();
            return null;
        } finally {
            // if (!cur1.isClosed()) {
            // cur1.close();
            // }
            // dbManager.close();
            // myWritableDb3 = null;
            statement = null;
            // cur1 = null;
        }
    }

    public void DeleteContact(String address, SQLiteDatabase myWritableDb) {
            // SQLiteDatabase myWritableDb = null;
            int rows = 0;
            Cursor cc = null;
            try {
                // myWritableDb = SQLiteDatabase.openDatabase(getDatabasePath(),
                // null,
                // SQLiteDatabase.OPEN_READWRITE);

                // myWritableDb = dbManager.getDb();

                if (myWritableDb != null) {
                    rows = myWritableDb.delete(CONTACTS_TABLE, CONTACTID + " = ? ",
                            new String[]{address});


                }
            } catch (Exception e) {

                e.printStackTrace();
            } finally {
                if (cc != null && !cc.isClosed()) {
                    cc.close();
                    cc = null;
                }
                // dbManager.close();
                // myWritableDb = null;
                rows = 0;
            }
    }


    /************
     * INSERT SMS AND CONTACTS INTO DATABASE
     ************/

    class InsertsmsData extends AsyncTask<Void, Integer, Void> {

        @Override
        protected Void doInBackground(Void... params) {
            ContentResolver cr = context.getContentResolver();
            try {
                mYDB = DataBaseManager.getInstance().openDatabase();
                pCur = cr.query(ContactsContract.CommonDataKinds.Phone.CONTENT_URI, null,
                        ContactsContract.CommonDataKinds.Phone.CONTACT_ID, null,
                        ContactsContract.CommonDataKinds.Phone.DISPLAY_NAME + " COLLATE NOCASE");
                if (Build.VERSION.SDK_INT < Build.VERSION_CODES.JELLY_BEAN) {
                        if (insertContacts(pCur, mYDB)) {
                            Display.DisplayLogI("ADITYA", "Inserting Data Lower");
                        }
                } else {
                        if (insertContacts(pCur, mYDB)) {
                            Display.DisplayLogI("ADITYA", "Inserting Data");
                        }
                }

            } catch (Exception e) {
                e.printStackTrace();
            } finally {
                if (cur != null) {
                    cur.close();
                }
                if (pCur != null) {
                    pCur.close();
                }
            }
            return null;
        }



        @Override
        protected void onProgressUpdate(Integer... values) {
            super.onProgressUpdate(values);
        }

        @Override
        protected void onPreExecute() {
            super.onPreExecute();
            try {
                Display.DisplayLogI("ADITYA", "SMS AND CONTACTS DUMP STARTED");
                DataBaseManager.getInstance().openDatabase();
            } catch (Exception e) {
                e.printStackTrace();
            }
        }

        @Override
        protected void onPostExecute(Void result) {
            try {

                DataBaseManager.getInstance().closeDatabase();
            } catch (Exception e) {
                e.printStackTrace();
            }
//			Intent i = new Intent(Lan.this, DashBoard.class);
//			i.putExtra("AppInstall", "install");
//			startActivity(i);
//			finish();
            super.onPostExecute(result);
            Display.DisplayLogI("ADITYA", "SMS AND CONTACTS DUMP DONE");
        }

    }

    /**
     * Get absolute path to database file. The Android's default system path of
     * your application database is /data/data/&ltpackage
     * name&gt/databases/&ltdatabase name&gt
     *
     * @return path to database file
     */
    private String getDatabasePath() {
        // The Android's default system path of your application database.
        // /data/data/<package name>/databases/<databasename>
        return Environment.getExternalStorageDirectory() + DB_Path + DB_NAME;
    }
    // ////////////// CREATE DB FROM ASSETS END //////////////////////////

    @Override
    public void onCreate(SQLiteDatabase db) {

    }

    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {

    }
}