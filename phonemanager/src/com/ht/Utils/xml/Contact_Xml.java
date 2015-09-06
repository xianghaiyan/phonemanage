package com.ht.Utils.xml;

import java.io.File;
import java.io.FileOutputStream;
import java.io.InputStream;
import java.util.ArrayList;
import java.util.List;

import org.xmlpull.v1.XmlPullParser;
import org.xmlpull.v1.XmlSerializer;

import android.content.ContentResolver;
import android.content.Context;
import android.database.Cursor;
import android.os.Environment;
import android.provider.ContactsContract;
import android.util.Log;
import android.util.Xml;
import com.ht.phoneguard.pojo.Info;
public class Contact_Xml {
	 public boolean save (List<Info> Infos,FileOutputStream out) throws Exception {
	        XmlSerializer serializer = Xml.newSerializer();
	        /*File file = new File(Environment.getExternalStorageDirectory(),
					"config.xml");
			FileOutputStream out = new FileOutputStream(file);*/
			//serializer.setOutput(os, "UTF-8");
	        try{
	        	 String enter = System.getProperty("line.separator");//换行  
		        serializer.setOutput(out, "UTF-8");
		        serializer.startDocument("UTF-8", true);
		        serializer.startTag(null, "Contacts");  
		        //changeLine(serializer, enter); 
		        int i=0;
		        for (Info info : Infos)
		        {
		            serializer.startTag(null, "Contact");            
		            serializer.attribute(null, "id",String.valueOf(info.getId()).trim());            
		            serializer.startTag(null, "Name");            
		            serializer.text(info.getName().toString().trim());
		            serializer.endTag(null, "Name");    //changeLine(serializer, enter);          
		            serializer.startTag(null, "Number");            
		            serializer.text(info.getPhonenumber().toString().trim());
		            serializer.endTag(null, "Number"); // changeLine(serializer, enter);            
		            serializer.endTag(null, "Contact"); //changeLine(serializer, enter); 
		            Log.v("序列次数：", ""+i++);
	        }        
	        serializer.endTag(null, "Contacts");// changeLine(serializer, enter); 
	        serializer.endDocument();
	        out.flush();
	        out.close();
	        return true;
	        }
	        catch(Exception e)
	        {
	        	return false;
	        }
	    }
	/*	public  void changeLine(XmlSerializer serializer, String enter){  
		    try{  
		        serializer.text(enter);  
		    }catch(Exception e){  
		        System.out.println(e.getMessage());
		    }     
	 	}*/
	 //使用contentprovider获取通讯录中的所有联系人的姓名和电话
	    public List<Info> getInfos (Context context) {
	        ContentResolver cr = context.getContentResolver();
	        //得到的是所有的联系人信息
	        Cursor cursor = cr.query(ContactsContract.Contacts.CONTENT_URI,
	                null,
	                null,
	                null,
	                null);
	        ArrayList<Info> infoList = new ArrayList<Info>();
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
	                info.setId(Integer.parseInt(contactId));
	                info.setName(name);
	                info.setPhonenumber(strPhoneNumber);
	                infoList.add(info);
	                Log.v("导出成功", infoList.size()+"");
	            }
	            phone.close();
	        }
	        cursor.close();
	        return infoList;
	    }
	    public List<Info> Pull_Xml_getInfo(InputStream is) throws Exception
	    {
	    	ArrayList<Info> list=null;
	    	XmlPullParser parser=Xml.newPullParser();
	    	Info info=null;
	    	parser.setInput(is,"utf-8");
	    	int type= parser.getEventType();//得到事件类型
	    	while (type!=XmlPullParser.END_DOCUMENT) {
	 		//解析   
		 	 switch (type) {
		 		case XmlPullParser.START_TAG:
		 			if("Contacts".equals(parser.getName()))
		 			{
		 				  list=new ArrayList<Info>();
		 			}
		 			else if("Contact".equals(parser.getName()))
		 			{
		 	             info=new Info();
		 	             String id=parser.getAttributeValue(0);
		 	             info.setId(Integer.parseInt(id));
		 			}
		 			else if ("Name".equals(parser.getName())) {
		 				String name=parser.nextText();//得到文本内容<>内容</>
		 				info.setName(name);
		 			}
		 			else if ("Number".equals(parser.getName())) {
		 				String phonenumber=parser.nextText();//得到文本内容<>内容</>
		 				info.setPhonenumber(phonenumber);
		 			}
		 			break;
		        case XmlPullParser.END_TAG:
		           if("Contact".equals(parser.getName()))
		           {
		        	   list.add(info);
		        	   Log.v("解析：id=", info.getId()+info.getName()+info.getPhonenumber());
		         	  info=null;
		           }
		 		default:
		 			break;
		 		}
		 		type=parser.next();
	 	}
	 	   return list;
	    }
}
