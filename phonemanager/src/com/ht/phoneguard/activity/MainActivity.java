package com.ht.phoneguard.activity;

import android.content.Intent;
import android.support.v4.app.Fragment;
import android.os.Bundle;
import android.support.v4.app.FragmentActivity;
import android.support.v4.view.ViewPager;
import android.view.MenuItem;
import android.view.View;
import android.widget.PopupMenu;
import android.widget.RadioButton;
import android.widget.RadioGroup;
import com.ht.phoneguard.R;
import com.ht.phoneguard.dao.DbManager;
import com.ht.phoneguard.fragment.SmsFragment;
import com.ht.phoneguard.fragment.PhoneFragment;
import com.ht.phoneguard.fragment.DarkFragment;
import com.ht.phoneguard.adapter.MainFragmentPagerAdapter;
import com.ht.phoneguard.service.PhoneService;

import java.util.ArrayList;
import java.util.List;

public class MainActivity extends FragmentActivity implements DarkFragment.Callbacks{



    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.main);
        DbManager.createInstance(getApplicationContext());
        final ViewPager viewPager = (ViewPager) findViewById(R.id.main_view_pager);
        List<Fragment> fragments = new ArrayList<Fragment>();
        fragments.add(new DarkFragment());
        fragments.add(new SmsFragment());
        fragments.add(new PhoneFragment());
        MainFragmentPagerAdapter adapter = new MainFragmentPagerAdapter(
                getSupportFragmentManager(),
                fragments
        );
        viewPager.setAdapter(adapter);

        final RadioGroup radioGroup = (RadioGroup) findViewById(R.id.radiogroup);
        radioGroup.setOnCheckedChangeListener(new RadioGroup.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(RadioGroup group, int checkedId) {
                int count = group.getChildCount();
                int checkPos = 0;
                for (int i = 0; i < count; i++) {
                    View view = group.getChildAt(i);
                    if (view instanceof RadioButton) {
                        RadioButton rb = (RadioButton) view;
                        boolean checked = rb.isChecked();
                        if (checked) {
                            checkPos = i;
                            break;
                        }
                    }
                }
                viewPager.setCurrentItem(checkPos, false);
            }
        });


        viewPager.setOnPageChangeListener(new ViewPager.OnPageChangeListener() {
            @Override
            public void onPageScrolled(int i, float v, int i1) {

            }

            @Override
            public void onPageSelected(int i) {
                View view = radioGroup.getChildAt(i);
                if (view != null) {
                    if (view instanceof RadioButton) {
                        RadioButton rb = (RadioButton) view;
                        rb.setChecked(true);
                    }
                }


            }

            @Override
            public void onPageScrollStateChanged(int i) {

            }
        });

        //初始的时候在黑名单界面
        RadioButton button = (RadioButton) radioGroup.getChildAt(0);
        button.setChecked(true);
    }

    //实现darkfragment中的接口方法
    @Override
    public void addOnClick(View view) {
        PopupMenu popupMenu = new PopupMenu(this, view);
        popupMenu.inflate(R.menu.add_menu);
        popupMenu.show();
        popupMenu.setOnMenuItemClickListener(new PopupMenu.OnMenuItemClickListener() {
            @Override
            public boolean onMenuItemClick(MenuItem menuItem) {
                int itemId = menuItem.getItemId();
                switch (itemId) {
                    //点击了从联系人添加的响应事件
                    case R.id.addfromcontacts:
                        Intent intent = new Intent(MainActivity.this, AddFromComtactsActivity.class);
                        startActivity(intent);
                        break;
                    //点击了手动添加的响应事件
                    case R.id.addshoudong:
                        Intent intent2 = new Intent(MainActivity.this, AddShoudongActivity.class);
                        startActivity(intent2);
                        break;
                }
                return true;
            }
        });
    }
}
