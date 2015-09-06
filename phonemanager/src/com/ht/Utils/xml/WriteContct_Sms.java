package com.ht.Utils.xml;

import android.R.integer;
import android.content.ContentResolver;
import android.content.ContentUris;
import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.net.Uri;
import android.provider.ContactsContract;
import android.provider.ContactsContract.CommonDataKinds.Email;
import android.provider.ContactsContract.CommonDataKinds.Phone;
import android.provider.ContactsContract.CommonDataKinds.StructuredName;
import android.provider.ContactsContract.Contacts.Data;
import android.provider.ContactsContract.RawContacts;

public class WriteContct_Sms {
	public boolean inserttoContact(Context context,String name, String number, String email) {  
        try {  
            ContentValues values = new ContentValues();  
  
            // 下面的操作会根据RawContacts表中已有的rawContactId使用情况自动生成新联系人的rawContactId  
            Uri rawContactUri = context.getContentResolver().insert(  
                    RawContacts.CONTENT_URI, values);  
            long rawContactId = ContentUris.parseId(rawContactUri);  
  
            // 向data表插入姓名数据  
            if (name != "") {  
                values.clear();  
                values.put(Data.RAW_CONTACT_ID, rawContactId);  
                values.put(Data.MIMETYPE, StructuredName.CONTENT_ITEM_TYPE);  
                values.put(StructuredName.GIVEN_NAME, name);  
                context.getContentResolver().insert(ContactsContract.Data.CONTENT_URI,  
                        values);  
            }  
  
            // 向data表插入电话数据  
            if (number != "") {  
                values.clear();  
                values.put(Data.RAW_CONTACT_ID, rawContactId);  
                values.put(Data.MIMETYPE, Phone.CONTENT_ITEM_TYPE);  
                values.put(Phone.NUMBER, number);  
                values.put(Phone.TYPE, Phone.TYPE_MOBILE);  
                context.getContentResolver().insert(ContactsContract.Data.CONTENT_URI,  
                        values);  
            }  
  
            // 向data表插入Email数据  
            if (email != "") {  
                values.clear();  
                values.put(Data.RAW_CONTACT_ID, rawContactId);  
                values.put(Data.MIMETYPE, Email.CONTENT_ITEM_TYPE);  
                values.put(Email.DATA, email);  
                values.put(Email.TYPE, Email.TYPE_WORK);  
                context.getContentResolver().insert(ContactsContract.Data.CONTENT_URI,  
                        values);  
            }  
  
           /* // 向data表插入QQ数据  
            if (im_qq != "") {  
                values.clear();  
                values.put(Data.RAW_CONTACT_ID, rawContactId);  
                values.put(Data.MIMETYPE, Im.CONTENT_ITEM_TYPE);  
                values.put(Im.DATA, im_qq);  
                values.put(Im.PROTOCOL, Im.PROTOCOL_QQ);  
                getContentResolver().insert(ContactsContract.Data.CONTENT_URI,  
                        values);  
            }  
            // 向data表插入头像数据  
            Bitmap sourceBitmap = BitmapFactory.decodeResource(getResources(),  
                    R.drawable.icon);  
            final ByteArrayOutputStream os = new ByteArrayOutputStream();  
            // 将Bitmap压缩成PNG编码，质量为100%存储  
            sourceBitmap.compress(Bitmap.CompressFormat.PNG, 100, os);  
            byte[] avatar = os.toByteArray();  
            values.put(Data.RAW_CONTACT_ID, rawContactId);  
            values.put(Data.MIMETYPE, Photo.CONTENT_ITEM_TYPE);  
            values.put(Photo.PHOTO, avatar);  
            getContentResolver().insert(ContactsContract.Data.CONTENT_URI,  
                    values);  */
        }  
  
        catch (Exception e) {  
            return false;  
        }  
        return true;  
    }  
	public static Uri mSmsUri = Uri.parse("content://sms/inbox");
	 
	public Boolean insertTosms(Context context,String address,String body,String  date,int read,int type)
	{
	   
	        ContentValues values = new ContentValues();
	        values.put("address", address);
	        values.put("body", body);
	        values.put("date", date);     
	        values.put("read", read);
	        values.put("type", type);//1表示收件箱 2表示发件箱
	     
	        context.getContentResolver().insert(mSmsUri, values);
	        return true;
	}
	/** 
     * 从通讯录获取联系人 
     * @throws Throwable 
     */  
    public void testGetContacts(Context context) throws Throwable  
    {  
        Uri uri=Uri.parse("content://com.android.contacts/contacts");  
        ContentResolver resolver=context.getContentResolver();  
        Cursor cursor=resolver.query(uri, null, null,null,null);  
        while(cursor.moveToNext())  
        {  
            StringBuilder sb=new StringBuilder();  
            int contactid=cursor.getInt(cursor.getColumnIndex("_id"));  
            String name=cursor.getString(cursor.getColumnIndex("display_name"));  
            sb.append("id=").append(contactid).append(",name=").append(name);  
              
            uri=Uri.parse("content://com.android.contacts/data/phones");  
            Cursor phoneCursor=resolver.query(uri, null, "contact_id=?",new String[]{String.valueOf(contactid)},null);  
            while(phoneCursor.moveToNext())  
            {  
                String phone=phoneCursor.getString(phoneCursor.getColumnIndex("data1"));  
                sb.append(",phone=").append(phone);  
            }  
            uri=Uri.parse("content://com.android.contacts/data/emails");  
            Cursor emailCursor=resolver.query(uri, null, "contact_id=?",new String[]{String.valueOf(contactid)},null);  
            while(emailCursor.moveToNext())  
            {  
                String email=emailCursor.getString(emailCursor.getColumnIndex("data1"));  
                sb.append(",email=").append(email);  
            }  
            //Log.i(TAG,sb.toString());  
        }  
    }  
      
}
