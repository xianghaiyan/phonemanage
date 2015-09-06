package com.ht.phoneguard.adapter;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.TextView;
import com.ht.phoneguard.R;
import com.ht.phoneguard.dao.DbManager;
import com.ht.phoneguard.pojo.TongHua;

import java.util.List;


public class TongHuaAdapter extends BaseAdapter {
    private Context context;
    private List<TongHua> list;

    public TongHuaAdapter(Context context, List<TongHua> list) {
        this.context = context;
        this.list = list;
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
    public View getView(int i, View view, ViewGroup viewGroup) {
        ViewHolder viewHolder = null;
        if (view == null) {
            view = LayoutInflater.from(context).inflate(R.layout.tonghua_item, null);
            viewHolder = new ViewHolder();
            viewHolder.phone = (TextView) view.findViewById(R.id.phonenumber);
            viewHolder.time = (TextView) view.findViewById(R.id.phonetime);
            view.setTag(viewHolder);
        } else
            viewHolder = (ViewHolder) view.getTag();
        viewHolder.time.setText(list.get(i).getTime());
        String name= DbManager.getInstance().Selectphonenumber(list.get(i).getPhonenumber());
        viewHolder.phone.setText(name+" "+list.get(i).getPhonenumber());
        return view;
    }

    private class ViewHolder {
        private TextView phone;
        private TextView time;
    }
}

