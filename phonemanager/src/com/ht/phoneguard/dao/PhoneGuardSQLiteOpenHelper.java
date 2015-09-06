package com.ht.phoneguard.dao;

import android.content.Context;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;
import android.util.Log;

/**
 * Intent：SQLite 工具类 
 * Project: utils
 * Author: 王超
 * Email: 
 * Date: 2015/7/13
 */
public class PhoneGuardSQLiteOpenHelper extends SQLiteOpenHelper {
    public PhoneGuardSQLiteOpenHelper(Context context) {
        super(context, "phoneguard", null, 1);
    }

    @Override
    public void onCreate(SQLiteDatabase sqLiteDatabase) {
        sqLiteDatabase.execSQL("CREATE TABLE dark(_id INTEGER PRIMARY KEY ASC AUTOINCREMENT, name TEXT NOT NULL, phonenumber TEXT NOT NULL);");
        sqLiteDatabase.execSQL("CREATE TABLE duanxin(_id INTEGER PRIMARY KEY ASC AUTOINCREMENT, phonenumber TEXT, content TEXT, time TEXT);");
        sqLiteDatabase.execSQL("CREATE TABLE tonghua(_id INTEGER PRIMARY KEY ASC AUTOINCREMENT, phonenumber TEXT, time TEXT);");
    }

    @Override
    public void onUpgrade(SQLiteDatabase sqLiteDatabase, int i, int i1) {
    	/*String sql="alter table duanxin add name varchar(20)";
    	String sql2="alter table tonghua add name varchar(20)";
		Log.v("更新提示", "数据库已更新");
		sqLiteDatabase.execSQL(sql);
		sqLiteDatabase.execSQL(sql2);*/
    }
}