package com.ht.phoneguard.fragment;


import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ListView;
import com.ht.phoneguard.R;
import com.ht.phoneguard.adapter.DarkAdapter;
import com.ht.phoneguard.adapter.TongHuaAdapter;
import com.ht.phoneguard.dao.DbManager;
import com.ht.phoneguard.pojo.Info;
import com.ht.phoneguard.pojo.TongHua;

import java.util.List;


public class PhoneFragment extends Fragment {
    private ListView listView;
    private TongHuaAdapter adapter;
    private List<TongHua> tongHuaList;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        //从数据库中得到拦截电话的集合
        super.onActivityCreated(savedInstanceState);
        View view = inflater.inflate(R.layout.fragment_phone, container, false);
        tongHuaList = DbManager.getInstance().getTongHuaList();
        listView = (ListView) view.findViewById(R.id.tonghuaListView);
        adapter = new TongHuaAdapter(getActivity(), tongHuaList);
        listView.setAdapter(adapter);
        registerForContextMenu(listView);
        return view;
    }
}