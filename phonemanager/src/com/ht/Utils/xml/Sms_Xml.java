package com.ht.Utils.xml;


import java.io.File;
import java.io.FileOutputStream;
import java.io.InputStream;
import java.util.ArrayList;
import java.util.List;

import org.apache.http.impl.conn.SingleClientConnManager;
import org.xmlpull.v1.XmlPullParser;
import org.xmlpull.v1.XmlSerializer;

import android.content.ContentResolver;
import android.content.Context;
import android.database.Cursor;
import android.net.Uri;
import android.os.Environment;
import android.provider.ContactsContract;
import android.util.Log;
import android.util.Xml;

import com.ht.phoneguard.dao.DbManager;
import com.ht.phoneguard.pojo.DuanXin;
public class Sms_Xml {
	 public boolean save (List<DuanXin> DuanXins,FileOutputStream out) throws Exception {
	        XmlSerializer serializer = Xml.newSerializer();
	        /*File file = new File(Environment.getExternalStorageDirectory(),
					"config.xml");
			FileOutputStream out = new FileOutputStream(file);*/
			//serializer.setOutput(os, "UTF-8");
	        try{
			        serializer.setOutput(out, "UTF-8");
			        serializer.startDocument("UTF-8", true);
			       // String enter = System.getProperty("line.separator");//换行  
			        serializer.startTag(null, "SMSs");
			        int i=0;
			        for (DuanXin Xin : DuanXins)
			        {
			            serializer.startTag(null, "SMS");            
			            serializer.attribute(null, "id",String.valueOf(Xin.getId()).trim());            
			            serializer.startTag(null, "Type");            
			            serializer.text(Xin.getType());
			            serializer.endTag(null, "Type");            
			            serializer.startTag(null, "Number");            
			            serializer.text(Xin.getPhonenumber().toString().trim());
			            serializer.endTag(null, "Number"); 
			            serializer.startTag(null, "Body");            
			            serializer.text(Xin.getContent().toString().trim());
			            serializer.endTag(null, "Body");   
			            serializer.startTag(null, "Time");            
			            serializer.text(Xin.getTime().toString().trim());
			            serializer.endTag(null, "Time");
			            serializer.endTag(null, "SMS");
			            Log.v("此次：", DuanXins.size()+"");
			            Log.v("序列次数：", Xin.getContent()+":"+i++);
		        }        
		        serializer.endTag(null, "SMSs");
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
	 	public  void changeLine(XmlSerializer serializer, String enter){  
		    try{  
		        serializer.text(enter);  
		    }catch(Exception e){  
		        System.out.println(e.getMessage());
		    }     
	 	}
	 //使用contentprovider获取短信
	    public List<DuanXin> getDuanXins (Context context) {
	    	 Uri uri=Uri.parse("content://sms/");
	    	 ArrayList<DuanXin> list=new  ArrayList<DuanXin>();
	    	 ContentResolver resolver=context.getContentResolver();
	    	 Cursor cursor= resolver.query(uri,new String[]{"address","date","type","body"}, null, null, null);
	    	 int i=1;
	    	 while(cursor.moveToNext())
	    	 {
	    		 String address=cursor.getString(cursor.getColumnIndex("address"));
	    		 String data=cursor.getString(cursor.getColumnIndex("date"));
	    		 String type=cursor.getString(cursor.getColumnIndex("type"));
	    		 String body=cursor.getString(cursor.getColumnIndex("body"));
	    		 Log.v(i+"", "address="+address+" data="+data+"  type="+type+" body="+body);
	    		 DuanXin xin=new DuanXin();
	    		 xin.setId(i);
	    		 xin.setType(type);
	    		 xin.setContent(body);
	    		 xin.setPhonenumber(address);
	    		 xin.setTime(data);
	    		 list.add(xin);
	    		 i++;
	    	 }
			return list;
	    }
	    public List<DuanXin> Pull_Xml_getDuanXin(InputStream is) throws Exception
	    {
	    	ArrayList<DuanXin> list=null;
	    	XmlPullParser parser=Xml.newPullParser();
	    	DuanXin Xin=null;
	    	parser.setInput(is,"utf-8");
	    	int type= parser.getEventType();//得到事件类型
	    	while (type!=XmlPullParser.END_DOCUMENT) {//解析   
		 	 switch (type) {
		 		case XmlPullParser.START_TAG:
		 			if("SMSs".equals(parser.getName()))
		 			{
		 				  list=new ArrayList<DuanXin>();
		 			}
		 			else if("SMS".equals(parser.getName()))
		 			{
		 	             Xin=new DuanXin();
		 	             String id=parser.getAttributeValue(0);
		 	             Xin.setId(Integer.parseInt(id));
		 			}
		 			else if ("Type".equals(parser.getName())) {
		 				String types=parser.nextText();//得到文本内容<>内容</>
		 				Xin.setType(types);
		 			}
		 			else if ("Number".equals(parser.getName())) {
		 				String phonenumber=parser.nextText();//得到文本内容<>内容</>
		 				Xin.setPhonenumber(phonenumber);
		 			}
		 			else if ("Body".equals(parser.getName())) {
		 				String body=parser.nextText();//得到文本内容<>内容</>
		 				Xin.setContent(body);
		 			}
		 			else if ("Time".equals(parser.getName())) {
		 				String time=parser.nextText();//得到文本内容<>内容</>
		 				Xin.setTime(time);
		 			}
		 			break;
		        case XmlPullParser.END_TAG:
		           if("SMS".equals(parser.getName()))
		           {
		        	   list.add(Xin);
		        	   Log.v("解析短信：id=", Xin.getId()+Xin.getContent());
		         	   Xin=null;
		           }
		 		default:
		 			break;
		 		}
		 		type=parser.next();
	 	}
	 	   return list;
	    }
}
