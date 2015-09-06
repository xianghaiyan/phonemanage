package com.ht.phoneguard.activity;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

import com.ht.Utils.xml.Contact_Xml;
import com.ht.Utils.xml.Sms_Xml;
import com.ht.Utils.xml.WriteContct_Sms;
import com.ht.phoneguard.R;
import com.ht.phoneguard.broadcastreceive.SmsBroadcastReceiver;
import com.ht.phoneguard.pojo.DuanXin;
import com.ht.phoneguard.pojo.Info;
import com.ht.phoneguard.service.PhoneService;

import android.app.Activity;
import android.app.AlertDialog;
import android.app.Dialog;
import android.app.Notification;
import android.app.PendingIntent;
import android.app.ProgressDialog;
import android.app.Service;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.IntentFilter;
import android.os.AsyncTask;
import android.os.Bundle;
import android.os.Environment;
import android.os.Handler;
import android.os.IBinder;
import android.os.Message;
import android.util.Log;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.Button;
import android.widget.ImageButton;
import android.widget.TextView;
import android.widget.Toast;

public class IndexActivity extends Activity implements OnClickListener {
	private ImageButton btncontacts,btn_jiexin,btn_sms,btn_smsjiexin;
	private ImageButton btnlanjie,btnabout;
	 private Dialog progressDialog;
    ArrayList<Info> list=new ArrayList<Info>();
    ArrayList<DuanXin> listsms=new ArrayList<DuanXin>();
   Contact_Xml contact;
   Sms_Xml sms;
   @Override
	protected void onCreate(Bundle savedInstanceState) {
		// TODO Auto-generated method stub
		super.onCreate(savedInstanceState);
		setContentView(R.layout.mainindex);
        //开启服务
        Intent intent = new Intent(this, PhoneService.class);
        startService(intent);
        //注册广播 已在清单 
	 /*  IntentFilter intentFilter=new IntentFilter();
	   intentFilter.addAction("android.provider.Telephony.SMS_RECEIVED");
	   	SmsBroadcastReceiver smsBroadcastReceiver=new SmsBroadcastReceiver();
	    registerReceiver(smsBroadcastReceiver, intentFilter);*/
		
		contact=new Contact_Xml();
		sms=new Sms_Xml();
		btnlanjie= (ImageButton) findViewById(R.id.btn_lanjie);
		btnlanjie.setOnClickListener(this);
		btncontacts=(ImageButton) findViewById(R.id.btn_contacts);
		btncontacts.setOnClickListener(this);
		btn_jiexin=(ImageButton) findViewById(R.id.btn_jiexin);
		btn_jiexin.setOnClickListener(this);
		btn_smsjiexin= (ImageButton) findViewById(R.id.btn_smsjiexin);
		btn_smsjiexin.setOnClickListener(this);
		btn_sms=(ImageButton) findViewById(R.id.btn_sms);
		btn_sms.setOnClickListener(this);
		btnabout=(ImageButton) findViewById(R.id.btn_more);
		btnabout.setOnClickListener(this);
	}
   public void showDiaLog(String message)
   {
	   AlertDialog.Builder dialog=new AlertDialog.Builder(IndexActivity.this);
		dialog.setTitle("导出提示");
		dialog.setMessage(message);
	    dialog.setCancelable(false);
	    dialog.setPositiveButton("确定", new DialogInterface.OnClickListener() {
			
			@Override
			public void onClick(DialogInterface dialog, int which) {
				// TODO Auto-generated method stub
				
				dialog.cancel();
			}
		}); 
	    dialog.show();
   }
   public Handler handler=new Handler(){
	   public void handleMessage(Message msg) {
		   switch (msg.what) {
	/*	case 1:
			mProgress.cancel();
			showDiaLog(msg.obj.toString());
			break;
		case 2:
			mProgress.cancel();
			showDiaLog(msg.obj.toString());
			break;
		case 3:
			mProgress.cancel();
			showDiaLog(msg.obj.toString());
			break;
		case 4:
			
			break;*/
		case -1:
			Toast.makeText(getApplicationContext(), "程序异常！", Toast.LENGTH_SHORT).show();
			break;
		default:
			progressDialog.cancel();
			showDiaLog(msg.obj.toString());
			break;
		}
	   };
   };
   
	@Override
	public void onClick(View v) {
		// TODO Auto-generated method stub
		switch (v.getId()) {
		case R.id.btn_more:
			 Intent intentabout=new Intent(this,Aboutus.class);
			 startActivity(intentabout);
			break;
		case R.id.btn_lanjie:
			 Intent intent=new Intent(this,MainActivity.class);
			 startActivity(intent);
			break;
        case R.id.btn_contacts:
        	progressDialog("联系人备份中...");
        	  new Thread(new Runnable() {
				@Override
				public void run() {
					try {
					// TODO Auto-generated method stub
					 list=(ArrayList<Info>) contact.getInfos(getApplicationContext());
		        		File file = new File(Environment.getExternalStorageDirectory(),
		    					"联系人备份.xml");
		    		  if(file.exists())
		    		    { 
		    		    	Log.v("exist", "已删除"+list.size()+"");
		    		    	file.delete();
		    		    }
		    		    file.createNewFile();
		    		     FileOutputStream out = new FileOutputStream(file);
						Boolean b=contact.save(list,out);
						 Thread.sleep(1000);
						if(b==true)
						{
							Message msg=new Message();
							msg.what=1;//表示联系人备份完成
							msg.obj="您的备份保存位置：\n"+file.getPath();
							handler.sendMessage(msg);	
						}
						if(b==false)
						{
							Message msg=new Message();
							msg.what=-1;//表示异常
							handler.sendMessage(msg);	
						}
					} catch (Exception e) {
						// TODO Auto-generated catch block
						e.printStackTrace();
					}
				}
			}).start();
        	   
          break;
        case R.id.btn_jiexin:
        progressDialog("联系人导入中...");
      	  new Thread(new Runnable() {
				@Override
				public void run() {
					try {
						File file = new File(Environment.getExternalStorageDirectory(),
								"联系人备份.xml");
			        	FileInputStream is;
						try {
							is = new FileInputStream(file);
							List<Info> listinfoInfos=contact.Pull_Xml_getInfo(is);
							Log.v("开始导入！", listinfoInfos.size()+"");
							boolean b=true;
							for(Info info:listinfoInfos)
							{
								WriteContct_Sms writesms=new WriteContct_Sms();
								writesms.inserttoContact(getApplicationContext(), info.getName(),info.getPhonenumber(), "1034262557@qq.com");
							}
							 Thread.sleep(1000);
							if(b==true)
							{
								Message msg=new Message();
								msg.what=2;//表示联系人导入完成
								msg.obj="联系人导入成功";
								handler.sendMessage(msg);	
							}
							if(b==false)
							{
								Message msg=new Message();
								msg.what=-1;//表示异常
								handler.sendMessage(msg);	
							}
							
						} catch (Exception e) {
							// TODO Auto-generated catch block
							e.printStackTrace();
						}
						
						
					} catch (Exception e) {
						// TODO Auto-generated catch block
						e.printStackTrace();
					}
				}
			}).start();
        	
			break;
        case R.id.btn_sms:
          	progressDialog("短信备份中...");
      	  new Thread(new Runnable() {
				@Override
				public void run() {
					try {
					// TODO Auto-generated method stub
						listsms= (ArrayList<DuanXin>) sms.getDuanXins(getApplicationContext());
	               	    Log.v("ss",""+listsms.size());
	               		File files = new File(Environment.getExternalStorageDirectory(),
	           					"短信备份.xml");
	           		    if(files.exists())
	           		    { 
	           		    				Log.v("exist", "已删除"+listsms.size()+"");
	           		    				files.delete();
	           		    }
						files.createNewFile();
					    FileOutputStream outS = new FileOutputStream(files);
						Boolean b=sms.save(listsms, outS);
						Thread.sleep(1000);
						if(b==true)
						{
							Message msg=new Message();
							msg.what=3;//表示短信备份完成
							msg.obj="您的备份保存位置：\n"+files.getPath();
							handler.sendMessage(msg);	
						}
						if(b==false)
						{
							Message msg=new Message();
							msg.what=-1;//表示异常
							handler.sendMessage(msg);	
						}
					} catch (Exception e) {
						// TODO Auto-generated catch block
						e.printStackTrace();
					}
				}
			}).start();
       break;
        case R.id.btn_smsjiexin:
        	 progressDialog("联系人导入中...");
         	  new Thread(new Runnable() {
   				@Override
   				public void run() {
   					try {
   						File filesms = new File(Environment.getExternalStorageDirectory(),
   								"短信备份.xml");
   			        	FileInputStream issms;
   						try {
   							issms = new FileInputStream(filesms);
   						List<DuanXin> duanxinlist= sms.Pull_Xml_getDuanXin(issms);
   						duanxinlist.size();
   						Boolean b=true;
   						for(DuanXin xin:duanxinlist)
   						{
   							WriteContct_Sms writesms=new WriteContct_Sms();
   							b=writesms.insertTosms(getApplicationContext(), xin.getPhonenumber(),xin.getContent(), xin.getTime(), 1,Integer.parseInt( xin.getType()));
   						}
   					  Thread.sleep(1000);
   						if(b==true)
   						{
   								Message msg=new Message();
   								msg.what=4;//表示短信导入完成
   								msg.obj="短信导入成功";
   								handler.sendMessage(msg);	
   						}
   						if(b==false)
   						{
   								Message msg=new Message();
   								msg.what=-1;//表示异常
   								handler.sendMessage(msg);	
   						}
   						} catch (Exception e) {
   							// TODO Auto-generated catch block
   							e.printStackTrace();
   						}
   						
   						
   					} catch (Exception e) {
   						// TODO Auto-generated catch block
   						e.printStackTrace();
   					}
   				}
   			}).start();
			break;
		default:
			break;
		}
	}
	//自定义加载对话框
	private void progressDialog(String title){  
		 progressDialog = new Dialog(IndexActivity.this,R.style.progress_dialog);
         progressDialog.setContentView(R.layout.dialog);
         progressDialog.setCancelable(true);
         progressDialog.getWindow().setBackgroundDrawableResource(android.R.color.transparent);
         TextView msg = (TextView) progressDialog.findViewById(R.id.id_tv_loadingmsg);
         msg.setText(title);
         progressDialog.show();
	}

}
