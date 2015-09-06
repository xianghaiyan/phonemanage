package com.ht.phoneguard.activity;

import android.app.Activity;
import android.os.Bundle;
import android.text.TextUtils;
import android.view.View;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;
import com.ht.phoneguard.R;
import com.ht.phoneguard.dao.DbManager;
import com.ht.phoneguard.pojo.Info;


public class AddShoudongActivity extends Activity {

    private EditText editText1;
    private EditText editText2;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_addshoudong);
        editText1 = (EditText) findViewById(R.id.shoudongname);
        editText2 = (EditText) findViewById(R.id.shoudongnumber);
    }

    public void backOnClick(View view) {
        this.finish();
    }

    public void concelOnClick(View view) {
        editText1.setText("");
        editText2.setText("");

    }

    public void ensureOnClick(View view) {
        String name = editText1.getText().toString();
        String number = editText2.getText().toString();
        if (TextUtils.isEmpty(name)) {
            Toast.makeText(this, "名字不能为空", Toast.LENGTH_LONG).show();
        } else if (TextUtils.isEmpty(number)) {
            Toast.makeText(this, "电话号码不能为空", Toast.LENGTH_LONG).show();
        } else {
            Info info = new Info();
            info.setName(name);
            info.setPhonenumber(number);
            DbManager.getInstance().addInfo(info);
            Toast.makeText(this, "黑名单添加成功", Toast.LENGTH_LONG).show();
        }
    }
}