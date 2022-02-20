package com.thimat.sockettelkarnet.adapter;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.TextView;

import com.thimat.sockettelkarnet.models.AgentModel;
import com.thimat.sockettelkarnet.R;

import java.util.ArrayList;

import io.realm.RealmResults;

public class RealmAdapterNameCity  extends BaseAdapter {
    private final LayoutInflater myinflater;

    private RealmResults<AgentModel> list = null;
    private final ArrayList<AgentModel> arraylist;
    public Context context;

    public RealmAdapterNameCity(Context context) {
        this.context = context;
        myinflater = LayoutInflater.from(context);
        this.arraylist = new ArrayList<AgentModel>();
    }
    public void setData(RealmResults<AgentModel> list) {
        this.list = list;
        this.arraylist.clear();
        this.arraylist.addAll(list);
    }
    @Override
    public int getCount() {
        // TODO Auto-generated method stub
        return list.size();
    }

    @Override
    public Object getItem(int position) {
        // TODO Auto-generated method stub
        return null;
    }

    @Override
    public long getItemId(int position) {
        // TODO Auto-generated method stub
        return 0;
    }

    @Override
    public View getView(final int position, View convertView,
                        ViewGroup parent) {
        ViewHolder viewholder;
        if (convertView == null) {
            convertView = myinflater.inflate(R.layout.realmadapternamecity,
                    null);
            viewholder = new ViewHolder();
            viewholder.txtnamecity = (TextView) convertView
                    .findViewById(R.id.name);
            viewholder.code = (TextView) convertView
                    .findViewById(R.id.code);
            convertView.setTag(viewholder);
        } else
            viewholder = (ViewHolder) convertView.getTag();
        viewholder.txtnamecity.setText(list.get(position)
                .getCity());
        viewholder.code.setText(String.valueOf(list.get(position).getCod_city()));
        return convertView;
    }

    public class ViewHolder {
        public TextView code;
        public TextView txtnamecity;
    }
}