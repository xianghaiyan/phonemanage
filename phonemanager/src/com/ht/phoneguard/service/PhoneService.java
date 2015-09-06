package com.ht.phoneguard.service;

import android.R;
import android.R.integer;
import android.app.Notification;
import android.app.PendingIntent;
import android.app.Service;
import android.content.Intent;
import android.os.IBinder;
import android.telephony.PhoneStateListener;
import android.telephony.TelephonyManager;
import android.util.Log;
import com.android.internal.telephony.ITelephony;
import com.ht.phoneguard.activity.MainActivity;
import com.ht.phoneguard.dao.DbManager;
import com.ht.phoneguard.pojo.TongHua;

import java.lang.reflect.Method;
import java.text.SimpleDateFormat;

public class PhoneService extends Service {

    TelephonyManager tManager;
    CustomPhone custonPhne;

    public class CustomPhone extends PhoneStateListener {
        @Override
        public void onCallStateChanged(int state, String number) {
            switch (state) {
                case TelephonyManager.CALL_STATE_IDLE:
                    break;
                case TelephonyManager.CALL_STATE_OFFHOOK:
                    break;
                case TelephonyManager.CALL_STATE_RINGING:
                    if (DbManager.getInstance().phonenumberisExist("+86"+number)) {
                        try {
                            Method method = Class.forName("android.os.ServiceManager").getMethod("getService", String.class);
                            IBinder binder = (IBinder) method.invoke(null, new Object[]{TELEPHONY_SERVICE});
                            ITelephony telephony = ITelephony.Stub.asInterface(binder);
                            // 挂断电话
                            Log.d("PhoneService", "挂断");
                            telephony.endCall();
                            //把来电记录存入拦截记录表
	                            TongHua tongHua = new TongHua();
	                            SimpleDateFormat sDateFormat = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
	                            String time = sDateFormat.format(System.currentTimeMillis());
	                            tongHua.setTime(time);
	                            tongHua.setPhonenumber("+86"+number);
	                            DbManager.getInstance().addTongHua(tongHua);
                         
                        } catch (Exception e) {
                            e.printStackTrace();
                        }
                    }
                    break;
            }

        }
    }
   @SuppressWarnings("deprecation")
@Override
	public void onCreate() {
		// TODO Auto-generated method stub
		super.onCreate();
		Log.v("服务启动", "Oncreate");
		
/*		 Notification.Builder builder = new Notification.Builder(this);  
	        PendingIntent contentIntent = PendingIntent.getActivity(this, 0,    
	                new Intent(this, MainActivity.class), 0);    
	        builder.setContentIntent(contentIntent);  
	        builder.setSmallIcon(R.drawable.ic_dialog_alert);  
	        builder.setTicker("Foreground Service Start");  
	        builder.setContentTitle("Foreground Service");  
	        builder.setContentText("Make this service run in the foreground.");  
	        Notification notification = builder.build();
	          
	        startForeground(1, notification); */   
		
		Notification notification=new Notification(R.drawable.ic_dialog_alert,"通信管家已启动！",System.currentTimeMillis());
		Intent notifactionintent=new Intent(this,MainActivity.class);
		PendingIntent pendingIntent=PendingIntent.getActivity(this, 0, notifactionintent, 0);
		notification.setLatestEventInfo(getApplicationContext(),"通信管家","为您实时拦截骚扰" , pendingIntent);
		startForeground(1,notification);                                                                                                                                                                                                                                                                                                                                                                                                                                            
		 //拦截电话
        tManager = (TelephonyManager) getSystemService(TELEPHONY_SERVICE);
        custonPhne = new CustomPhone();
        tManager.listen(custonPhne, PhoneStateListener.LISTEN_CALL_STATE);
       // return START_STICKY;
	}
    @Override
    public IBinder onBind(Intent intent) {
        return null;
    }
}
