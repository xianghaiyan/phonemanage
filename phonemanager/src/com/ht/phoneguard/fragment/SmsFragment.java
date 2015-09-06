package com.ht.phoneguard.fragment;


import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ListView;
import com.ht.phoneguard.R;
import com.ht.phoneguard.adapter.SmsAdapter;
import com.ht.phoneguard.adapter.TongHuaAdapter;
import com.ht.phoneguard.broadcastreceive.SmsBroadcastReceiver;
import com.ht.phoneguard.dao.DbManager;
import com.ht.phoneguard.pojo.DuanXin;
import com.ht.phoneguard.pojo.TongHua;

import java.util.List;


public class SmsFragment extends Fragment {
    private ListView listView;
    private SmsAdapter adapter;
    private List<DuanXin> duanXinList;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        //从数据库中得到拦截短信的集合
        super.onActivityCreated(savedInstanceState);
        View view = inflater.inflate(R.layout.fragment_sms, container, false);
        duanXinList = DbManager.getInstance().getDuanXinList();
        listView = (ListView) view.findViewById(R.id.smslistview);
        adapter = new SmsAdapter(getActivity(), duanXinList);
        listView.setAdapter(adapter);
        registerForContextMenu(listView);
        return view;
    }
}