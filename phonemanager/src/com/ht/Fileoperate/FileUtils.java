package com.ht.Fileoperate;

import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.OutputStreamWriter;

import android.content.Context;
import android.os.Environment;
import android.util.Log;

public class FileUtils {
     public void createFile(String filename)
     {
    	 File outputimage=new File(Environment.getExternalStorageDirectory(),filename);
         try {
			if(outputimage.exists())
			{ 
				Log.v("exist", "已删除");
				outputimage.delete();
				
			}
			outputimage.createNewFile();
		} catch (Exception e) {
			// TODO: handle exception
		}
     }
     public String fileReader(Context context,String url)
 	{
 		FileInputStream inputStream=null;
 		BufferedReader reader=null;
 		StringBuilder stringBuilder=new StringBuilder();
 		try
 		{
 			inputStream=context.openFileInput(url);
 			reader=new BufferedReader(new  InputStreamReader(inputStream));
 			String lineString="";
 			while((lineString=reader.readLine())!=null)
 			{
 				stringBuilder.append(lineString);
 			}
 		}
 		catch(Exception ec)
 		{
 			Log.v("读取错误！", ec.toString());
 		}
 		finally{
 			if(reader!=null)
 			{
 				try{
 				reader.close();
 				}
 				catch(IOException exce)
 				{
 					exce.printStackTrace();
 				}
 			}
 		}
 		return stringBuilder.toString();
 	}
    public void WriteFile(Context context ,String url,String inputdata, int mode)
    {
 	   FileOutputStream  out=null;
 	   BufferedWriter writer=null;
 	   try{
 		   //MODE_APPEND 在文件末尾添加
 		   out=context.openFileOutput(url,mode);//重写
 		   writer=new BufferedWriter(new OutputStreamWriter(out));
 		   writer.write(inputdata);
 		   System.out.println("写入文件成功！");
 	   }
 	   catch(Exception e)
 	   {
 		   Log.v("写入文件出错", e.toString());
 	   }
 	   finally
 	   {
 		   try{
 		   if(writer!=null)
 			   writer.close();
 		   }
 		   catch(IOException es)
 		   {
 			   
 		   }
 		   
 	   }
    }
}
