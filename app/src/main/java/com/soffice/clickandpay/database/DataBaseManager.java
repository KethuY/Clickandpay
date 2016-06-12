package com.soffice.clickandpay.database;

import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;
import android.os.Environment;

import java.util.concurrent.atomic.AtomicInteger;

public class DataBaseManager {

	private AtomicInteger mOpenCounter = new AtomicInteger();

	private static DataBaseManager instance;
	private static SQLiteOpenHelper mDatabaseHelper;
	private static SQLiteDatabase mDatabase;

	public static synchronized void initializeInstance(SQLiteOpenHelper helper) {
		if (instance == null) {
			try {
				instance = new DataBaseManager();
				mDatabaseHelper = helper;
			}catch (Exception e){
				e.printStackTrace();
			}
		}else{
			instance = null;
			instance = new DataBaseManager();
			mDatabaseHelper = helper;
		}
	}

	public static synchronized DataBaseManager getInstance() {
//		System.out.println("getInstance "+instance);
		if (instance == null) {

			new DataBaseManager();
//			throw new IllegalStateException(
//					DataBaseManager.class.getSimpleName()
//							+ " is not initialized, call initializeInstance(..) method first.");
		}

		return instance;
	}

	public synchronized SQLiteDatabase openDatabase() {
		if (mOpenCounter.incrementAndGet() == 1) {
			try {
//				System.out.println("mOpenCounter.countttttt "+mOpenCounter.intValue());
				mDatabase = SQLiteDatabase.openDatabase(getDatabasePath(), null,
						SQLiteDatabase.OPEN_READWRITE);
			}catch (Exception e){
				e.printStackTrace();
			}
		}else{
//			System.out.println("mOpenCounter.countttttt else "+mOpenCounter.intValue());
		}

		return mDatabase;
	}

	public synchronized void closeDatabase() {
		try {
			if (mOpenCounter != null && mDatabase != null && mOpenCounter.decrementAndGet() == 0) {
				// Closing database

				mDatabase.close();
				mOpenCounter=new AtomicInteger();

			}
		}catch (Exception e){}
	}

	private static final String DB_Path = "/Android/data/com.soffice.clickandpay/databases/";
	private static final String DB_NAME = "click_and_pay.db";

	private String getDatabasePath() {
		// The Android's default system path of your application database.
		// /data/data/<package name>/databases/<databasename>
		return Environment.getExternalStorageDirectory() + DB_Path + DB_NAME;
	}
}