package com.ht.phoneguard.adapter;

import android.content.Context;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.*;
import com.ht.phoneguard.R;
import com.ht.phoneguard.pojo.Info;

import java.util.HashMap;
import java.util.List;
import java.util.Map;


public class ContactsAdapter extends BaseAdapter {
    private Context context;
    private List<Info> list;
    public  Map<Integer,Boolean> mCBFlag = null;

    public ContactsAdapter(Context context, List<Info> list) {
        this.context = context;
        this.list = list;
        mCBFlag = new HashMap<Integer, Boolean>();
        init();
    }

    //初始化CheckBox状态
    void init(){
        for (int i = 0; i < list.size(); i++) {
            mCBFlag.put(i, false);
        }
    }

    @Override
    public int getCount() {
        return list.size();
    }

    @Override
    public Object getItem(int i) {
        return list.get(i);
    }

    @Override
    public long getItemId(int i) {
        return i;
    }

    @Override
    public View getView(final int i, View view, ViewGroup viewGroup) {
        ViewHolder viewHolder = null;
        if (view == null) {
            view = LayoutInflater.from(context).inflate(R.layout.contacts_item, null);
            viewHolder = new ViewHolder();
            viewHolder.name = (TextView) view.findViewById(R.id.name);
            viewHolder.number = (TextView) view.findViewById(R.id.number);
            viewHolder.check = (CheckBox) view.findViewById(R.id.check);
            view.setTag(viewHolder);
        }
        else
            viewHolder = (ViewHolder) view.getTag();
        viewHolder.name.setText(list.get(i).getName());
        viewHolder.number.setText(list.get(i).getPhonenumber());
        viewHolder.check.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton compoundButton, boolean isChecked) {
                if (isChecked) {
                    mCBFlag.put(i, true);
                } else {
                    mCBFlag.put(i, false);
                }
            }
        });
        /*CheckBox监听事件必须放在setChecked之前，否则后果自负*/
        viewHolder.check.setChecked(mCBFlag.get(i));
        Log.d("position:", "i=" + i + ",view=" + view);
        return view;
    }

    private class ViewHolder {
        private TextView name;
        private TextView number;
        private CheckBox check;
    }

    public Map<Integer, Boolean> getmCBFlag() {
        return mCBFlag;
    }

    public void setmCBFlag(Map<Integer, Boolean> mCBFlag) {
        this.mCBFlag = mCBFlag;
    }
}

