package com.ht.phoneguard.fragment;

import android.app.Activity;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.*;
import android.widget.*;
import com.ht.phoneguard.R;
import com.ht.phoneguard.adapter.ContactsAdapter;
import com.ht.phoneguard.adapter.DarkAdapter;
import com.ht.phoneguard.dao.DbManager;
import com.ht.phoneguard.pojo.Info;

import java.util.List;


public class DarkFragment extends Fragment {
    private Callbacks mCallbacks;
    private ListView listView;
    private DarkAdapter adapter;
    private List<Info> infoList;
    int position;

    public interface Callbacks {
        public void addOnClick(View view);
    }

    @Override
    public void onAttach(Activity activity) {
        super.onAttach(activity);
        if (!(activity instanceof Callbacks)) {
            throw new IllegalStateException("DarkFragment所在的activity必须实现Callbacks接口");
        }
        mCallbacks = (Callbacks) activity;
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        //从数据库中得到黑名单的集合
        super.onActivityCreated(savedInstanceState);
        View view = inflater.inflate(R.layout.fragment_dark, container, false);
        infoList = DbManager.getInstance().getDarkList();
        listView = (ListView) view.findViewById(R.id.dark);
        adapter = new DarkAdapter(getActivity(), infoList);
        listView.setAdapter(adapter);
        registerForContextMenu(listView);
        return view;
    }

    @Override
    public void onResume() {
        super.onResume();
        infoList.clear();
        infoList.clear();
        infoList.addAll(DbManager.getInstance().getDarkList());
        adapter.notifyDataSetChanged();
    }


    @Override
    public void onCreateContextMenu(ContextMenu menu, View v, ContextMenu.ContextMenuInfo menuInfo) {
        super.onCreateContextMenu(menu, v, menuInfo);
        position = 0;
        if (menuInfo instanceof AdapterView.AdapterContextMenuInfo) {
            AdapterView.AdapterContextMenuInfo info = (AdapterView.AdapterContextMenuInfo) menuInfo;
            position = info.position;
        }
        menu.add("从黑名单中删除：" + infoList.get(position).getPhonenumber());

    }

    @Override
    public boolean onContextItemSelected(MenuItem item) {

        Info info = infoList.get(position);
        DbManager.getInstance().deleteDark(info);
        infoList.clear();
        infoList.addAll(DbManager.getInstance().getDarkList());
        adapter.notifyDataSetChanged();
        return super.onContextItemSelected(item);
    }
}