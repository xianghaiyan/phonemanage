package com.ht.phoneguard.broadcastreceive;

import android.R.integer;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.database.Cursor;
import android.net.Uri;
import android.os.Bundle;
import android.telephony.SmsMessage;
import android.util.Log;

import com.ht.phoneguard.dao.DbManager;
import com.ht.phoneguard.pojo.DuanXin;

import java.text.SimpleDateFormat;


public class SmsBroadcastReceiver extends BroadcastReceiver {
    @Override
    public void onReceive(Context context, Intent intent) {
        DuanXin duanXin=selectsmsfromBroadcast(intent);
        DbManager.createInstance(context);
    	if (DbManager.getInstance().phonenumberisExist(duanXin.getPhonenumber()) || (DbManager.getInstance().phonenumberisExist("+86" +duanXin.getPhonenumber()))) {
               //把短信加入短信拦截表
               SimpleDateFormat sDateFormat = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
               String time = sDateFormat.format(System.currentTimeMillis());
               duanXin.setTime(time);
               DbManager.getInstance().addDuanXin(duanXin);
               abortBroadcast();
               //deleteSMS(context, duanXin.getPhonenumber());
         }
    }
   public DuanXin selectsmsfromBroadcast(Intent intent)
   {
	   //String action = intent.getAction();
       //接收由sms传递过来的数据
       Bundle extras = intent.getExtras();
       //通过pdus可以获得接收到的所有短信信息
       Object[] array = (Object[]) extras.get("pdus");
       String adress="";
       String content="";
       StringBuilder builder=new StringBuilder();
       //因为可能同时获得多个信息
       for (Object o1 : array) {
           SmsMessage message = SmsMessage.createFromPdu((byte[]) o1);
           //获得接收短信的电话号码
           adress = message.getOriginatingAddress();
           //获得短信的内容
           content = message.getDisplayMessageBody();
           builder.append(content);
           
       }
       DuanXin duanXin=new DuanXin();
       duanXin.setContent(builder.toString());
       duanXin.setPhonenumber(adress);
       return duanXin;
   }
   public void deleteSMS(Context context, String smsaddress) 
   { 
       try 
       { 
    	   String SMS_READ_COLUMN = "read";
    	   String WHERE_CONDITION = SMS_READ_COLUMN + " = 0";
    	   String SORT_ORDER = "date DESC";
    	   int count = 0;
    	   Cursor cursor =  context.getContentResolver().query(Uri.parse("content://sms/inbox"),
    	   new String[] { "_id", "thread_id", "address", "person", "date",
    	   "body" }, null, null, SORT_ORDER);
    	   if (cursor != null) {
    	   try {
    		   count = cursor.getCount();
    		   Log.v("COUNT:", count+"");
    	   if (count > 0) {
	    	   cursor.moveToFirst();
	    	   long threadId = cursor.getLong(1);
	    	   int id=cursor.getInt(cursor.getColumnIndex("_id")); 
	    	   String address=cursor.getString(cursor.getColumnIndex("address")).trim();
	    	   Log.v("adress", cursor.getString(cursor.getColumnIndex("address")).trim()+" :"+threadId);
	    	   if(smsaddress.equals(address));
	    	   {
	    		   //int id = isRead.getInt(isRead.getColumnIndex("_id")); 
	    		   //long id = getThreadId();

	    		   /*Uri mUri=Uri.parse("content://sms/conversations/" + threadId);

	    		  int i= context.getContentResolver().delete(mUri, null, null);*/
	    		/*   Thread.sleep(1000);
                 int i= context.getContentResolver().delete( 
                           Uri.parse("content://sms"), "_id=" + id, null); 
                   Log.v("短信已", "删除"+id);*/
	    		  
	    	   }
    	    }
    	   } finally {
    	    cursor.close();
    	    }
    	   }
       } 
       catch (Exception e) 
       { 
           e.printStackTrace(); 
       } 
   } 
}
