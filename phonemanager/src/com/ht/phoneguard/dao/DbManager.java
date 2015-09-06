package com.ht.phoneguard.dao;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import com.ht.phoneguard.pojo.DuanXin;
import com.ht.phoneguard.pojo.Info;
import com.ht.phoneguard.pojo.TongHua;

import java.util.ArrayList;
import java.util.List;


public class DbManager {
    private static DbManager dbManager;
    private static PhoneGuardSQLiteOpenHelper phoneGuardSQLiteOpenHelper;
    private static Context context;

    public static DbManager createInstance(Context ct) {
        if (dbManager == null) {

            dbManager = new DbManager(ct);//这里已经把ct传给构造函数了
        }
        return dbManager;
    }

    public static DbManager getInstance() {
        if (dbManager == null) {
        	createInstance(context);
            throw new IllegalStateException("must call createInstance method before getInstance method");
        }
        return dbManager;
    }

    private DbManager(Context ct) {
        phoneGuardSQLiteOpenHelper =
                new PhoneGuardSQLiteOpenHelper(ct);
        this.context = ct;

    }

    //往数据库中添加一条记录黑名单记录
    public boolean addInfo(Info info) {
        SQLiteDatabase db = phoneGuardSQLiteOpenHelper.getWritableDatabase();
        if (db.isOpen()) {
            db.beginTransaction();
            ContentValues values = new ContentValues();
            values.put("name", info.getName());
            String phonenumber = info.getPhonenumber().replace(" ", "").replace("-", "");
            if (! info.getPhonenumber().contains("+86")) {
                phonenumber = "+86"+phonenumber;
            }
            values.put("phonenumber", phonenumber);
            long rid = db.insert("dark", null, values);
            db.setTransactionSuccessful();
            db.endTransaction();
            db.close();
            return true;
        } else
            return false;
    }

    //往数据库中添加一条记录拦截到的短信记录
    public boolean addDuanXin(DuanXin duanXin) {
        SQLiteDatabase db = phoneGuardSQLiteOpenHelper.getWritableDatabase();
        if (db.isOpen()) {
            db.beginTransaction();
            ContentValues values = new ContentValues();
            values.put("phonenumber", duanXin.getPhonenumber());
            values.put("content", duanXin.getContent());
            values.put("time", duanXin.getTime());
            long rid = db.insert("duanxin", null, values);
            db.setTransactionSuccessful();
            db.endTransaction();
            db.close();
            return true;
        } else
            return false;
    }


    //往数据库中添加一条记录拦截到的通话记录的信息
    public boolean addTongHua(TongHua tongHua) {
        SQLiteDatabase db = phoneGuardSQLiteOpenHelper.getWritableDatabase();
        if (db.isOpen()) {
            db.beginTransaction();
            ContentValues values = new ContentValues();
            values.put("phonenumber", tongHua.getPhonenumber());
            values.put("time", tongHua.getTime());
            long rid = db.insert("tonghua", null, values);
            db.setTransactionSuccessful();
            db.endTransaction();
            db.close();
            return true;
        } else
            return false;
    }

    //删除数据库中的一条黑名单记录
    public Boolean deleteDark(Info info) {
        SQLiteDatabase db = phoneGuardSQLiteOpenHelper.getWritableDatabase();
        if (db.isOpen()) {
            db.beginTransaction();
            long rid = db.delete("dark", "phonenumber = ?", new String[]{info.getPhonenumber()});
            db.setTransactionSuccessful();
            db.endTransaction();
            db.close();
            return true;
        } else
            return false;
    }


    //删除数据库中的一条拦截短信记录
    public Boolean deleteDuanXin(DuanXin duanXin) {
        SQLiteDatabase db = phoneGuardSQLiteOpenHelper.getWritableDatabase();
        if (db.isOpen()) {
            db.beginTransaction();
            long rid = db.delete("duanxin", "_id = ?", new String[]{duanXin.getId() + ""});
            db.setTransactionSuccessful();
            db.endTransaction();
            db.close();
            return true;
        } else
            return false;
    }


    //删除数据库中的一条拦截的通话记录
    public Boolean deleteTongHua(TongHua tongHua) {
        SQLiteDatabase db = phoneGuardSQLiteOpenHelper.getWritableDatabase();
        if (db.isOpen()) {
            db.beginTransaction();
            long rid = db.delete("tonghua", "_id = ?", new String[]{tongHua.getId() + ""});
            db.setTransactionSuccessful();
            db.endTransaction();
            db.close();
            return true;
        } else
            return false;
    }


    //得到所有的黑名单的集合
    public List<Info> getDarkList() {
        SQLiteDatabase db = phoneGuardSQLiteOpenHelper.getWritableDatabase();
        List<Info> list = new ArrayList<Info>();
        Cursor cursor = db.query("dark", null, null, null, null, null, null);
        if (cursor != null) {
            while (cursor.moveToNext()) {
                Info info = new Info();
                String name = cursor.getString(cursor.getColumnIndex("name"));
                String phonenumber = cursor.getString(cursor.getColumnIndex("phonenumber"));
                info.setName(name);
                info.setPhonenumber(phonenumber);
                list.add(info);
            }
            cursor.close();
            db.close();
        }
        return list;
    }


    //根据一个info查询他是否在黑名单中
    public boolean phonenumberisExist(String phonenumber) {
        boolean flag = false;
        SQLiteDatabase db = phoneGuardSQLiteOpenHelper.getWritableDatabase();
        Cursor cursor = db.query("dark", null, "phonenumber=?", new String[]{phonenumber}, null, null, null);
        if (cursor.getCount() == 0)
            flag = flag;
        else
            flag = true;
        cursor.close();
        db.close();
        return flag;
    }
    //根据一个info查询单条记录黑名单中
    public String  Selectphonenumber(String phonenumber) {
        SQLiteDatabase db = phoneGuardSQLiteOpenHelper.getWritableDatabase();
        String name="";
        Cursor cursor = db.query("dark", null, "phonenumber=?", new String[]{phonenumber}, null, null, null);
        if (cursor != null) {
            while (cursor.moveToNext()) {
                name=cursor.getString(cursor.getColumnIndex("name"));
            }
        }
            cursor.close();
            db.close();
        return name;
    }

    //得到所有的拦截到短信的集合
    public List<DuanXin> getDuanXinList() {
        SQLiteDatabase db = phoneGuardSQLiteOpenHelper.getWritableDatabase();
        List<DuanXin> list = new ArrayList<DuanXin>();
        Cursor cursor = db.query("duanxin", null, null, null, null, null, "time desc");
        if (cursor != null) {
            while (cursor.moveToNext()) {
                DuanXin duanXin = new DuanXin();
                int id = cursor.getInt(0);
                String phonenumber = cursor.getString(1);
                String content = cursor.getString(2);
                String time = cursor.getString(3);
                duanXin.setPhonenumber(phonenumber);
                duanXin.setContent(content);
                duanXin.setId(id);
                duanXin.setTime(time);
                list.add(duanXin);
            }
            cursor.close();
            db.close();
        }
        return list;
    }


    //得到所有的拦截到短信的集合
    public List<TongHua> getTongHuaList() {
        SQLiteDatabase db = phoneGuardSQLiteOpenHelper.getWritableDatabase();
        List<TongHua> list = new ArrayList<TongHua>();
        Cursor cursor = db.query("tonghua", null, null, null, null, null, "time desc");
        if (cursor != null) {
            while (cursor.moveToNext()) {
                TongHua tongHua = new TongHua();
                int id = cursor.getInt(0);
                String phonenumber = cursor.getString(1);
                String time = cursor.getString(2);
                tongHua.setId(id);
                tongHua.setPhonenumber(phonenumber);
                tongHua.setTime(time);
                list.add(tongHua);
            }
            cursor.close();
            db.close();
        }
        return list;
    }

}