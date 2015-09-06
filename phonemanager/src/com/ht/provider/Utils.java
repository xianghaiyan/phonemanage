package com.ht.provider;

import java.util.ArrayList;
import java.util.List;

import android.content.ContentResolver;
import android.content.Context;
import android.database.Cursor;
import android.provider.ContactsContract;
import android.util.Log;

import com.ht.phoneguard.dao.DbManager;
import com.ht.phoneguard.pojo.Info;

public class Utils {

	 //使用contentprovider获取通讯录中的所有联系人的姓名和电话
    private static List<Info> getInfos(Context context) {
        ContentResolver cr = context.getContentResolver();
        //得到的是所有的联系人信息
        Cursor cursor = cr.query(ContactsContract.Contacts.CONTENT_URI,
                null,
                null,
                null,
                null);
        List<Info> infoList = new ArrayList<Info>();
        while (cursor.moveToNext()) {
            //Info info = new Info();
            // 取得联系人名字
            int nameFieldColumnIndex = cursor.getColumnIndex(ContactsContract.Contacts.DISPLAY_NAME);
            String name = cursor.getString(nameFieldColumnIndex);

            // 取得联系人ID
            String contactId = cursor.getString(cursor.getColumnIndex(ContactsContract.Contacts._ID));
            //这里的URI是一个字段
            Cursor phone = cr.query(ContactsContract.CommonDataKinds.Phone.CONTENT_URI, null, ContactsContract.CommonDataKinds.Phone.CONTACT_ID + " = "
                    + contactId, null, null);

            //List<String> list = new ArrayList<>();
            // 取得电话号码(可能存在多个号码),分开处理
            while (phone.moveToNext()) {
                String strPhoneNumber = phone.getString(phone.getColumnIndex(ContactsContract.CommonDataKinds.Phone.NUMBER));
                
                //这里把同一人不同电话分为不同行处理了
                Info info = new Info();
                info.setName(name);
                info.setPhonenumber(strPhoneNumber);
            }
            phone.close();
        }
        cursor.close();
        return infoList;
    }
}
