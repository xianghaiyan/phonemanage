package com.ht.phoneguard.activity;

import android.app.Activity;
import android.content.ContentResolver;
import android.content.Context;
import android.database.Cursor;
import android.os.Bundle;
import android.provider.ContactsContract;
import android.util.Log;
import android.view.View;
import android.widget.*;
import com.ht.phoneguard.R;
import com.ht.phoneguard.adapter.ContactsAdapter;
import com.ht.phoneguard.dao.DbManager;
import com.ht.phoneguard.pojo.Info;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;


public class AddFromComtactsActivity extends Activity {

    private ListView listView;
    private ContactsAdapter adapter;
    private List<Info> infoList;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_addfromcomtaccts);
        infoList = getInfos(getApplicationContext());
        listView = (ListView) findViewById(R.id.contacts);
        adapter = new ContactsAdapter(this, infoList);
        listView.setAdapter(adapter);
    }


    //使用contentprovider获取通讯录中的所有联系人的姓名和电话
    private List<Info> getInfos(Context context) {
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
            // 取得电话号码(可能存在多个号码),这里我就默认一个人
            while (phone.moveToNext()) {
                String strPhoneNumber = phone.getString(phone.getColumnIndex(ContactsContract.CommonDataKinds.Phone.NUMBER));
                //这里把同一人不同电话分为不同行处理了
               /* Log.v("Data",phone.getString(phone.getColumnIndex(ContactsContract.CommonDataKinds.Phone.DATA)));
                Log.v("Data",phone.getString(phone.getColumnIndex(ContactsContract.CommonDataKinds.Phone.NORMALIZED_NUMBER)));
                Log.v("Data",phone.getString(phone.getColumnIndex(ContactsContract.CommonDataKinds.Phone.DATA2)));*/
                Info info = new Info();
                info.setName(name);
              //  info.setId(Integer.parseInt(contactId));
                info.setPhonenumber(strPhoneNumber);
                //判断这个info是否在黑名单中，如果在就不加入进去
                if (!DbManager.getInstance().phonenumberisExist(strPhoneNumber))
                    infoList.add(info);
            }
            phone.close();
        }
        cursor.close();
        return infoList;
    }

    public void backOnClick(View view) {
        this.finish();
    }

    //取消按钮的点击事件
    public void concelOnClick(View view) {
        // 遍历listview的长度，将已选的按钮设为未选
        for (int i = 0; i < infoList.size(); i++) {

            if ((adapter.getmCBFlag()).get(i)) {
                adapter.getmCBFlag().put(i, false);
            }
            adapter.notifyDataSetChanged();
        }
    }

    //确定按钮的点击事件
    public void ensureOnClick(View view) {
        // 遍历listview的长度，找到选中的按钮，然后把其中的的姓名和电话取出来，存到新建的数据库中
        for (int i = 0; i < infoList.size(); i++) {
            if (adapter.getmCBFlag().get(i)) {
                //取出这条数据，然后存放到数据库中
                Info info = new Info();
                info.setName(infoList.get(i).getName());
                info.setPhonenumber(infoList.get(i).getPhonenumber());
                DbManager.getInstance().addInfo(info);
            }
        }
        this.finish();
    }
}