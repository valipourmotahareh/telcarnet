package com.thimat.sockettelkarnet.adapter;

import android.content.Context;
import android.content.Intent;
import android.graphics.Color;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.TextView;

import com.thimat.sockettelkarnet.activites.ActDetailMessage;
import com.thimat.sockettelkarnet.localDb.LocalDB;
import com.thimat.sockettelkarnet.models.MessageModel;
import com.thimat.sockettelkarnet.R;

import io.realm.RealmResults;

public class RealmAdapterInbox extends BaseAdapter {

    // Declare Variables
    Context context;
    LayoutInflater inflater;
    RealmResults<MessageModel> data;
    String   path;
    LocalDB localDB;
    public RealmAdapterInbox(Context context,RealmResults<MessageModel> arraylist) {
        this.context = context;
        data = arraylist;
        localDB = new LocalDB(context);
    }

    @Override
    public int getCount() {
        return data.size();
    }

    @Override
    public Object getItem(int position) {
        return null;
    }

    @Override
    public long getItemId(int position) {
        return 0;
    }

    public View getView(final int position, View convertView, ViewGroup parent) {
        // Declare Variables
        TextView name;
        TextView des;
        ImageView image;
        TextView name_car;

        inflater = (LayoutInflater) context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);

        View itemView = inflater.inflate(R.layout.list_item_inbox, parent, false);
        // Get the position
        MessageModel currentmodel = data.get(position);

        // Locate the TextViews in listview_item.xml
        name = (TextView) itemView.findViewById(R.id.inbox_textView);
        name_car=(TextView) itemView.findViewById(R.id.name_car);
        name.setText(currentmodel.getType());

        if (!currentmodel.isRead())
        {
            name.setTextColor(Color.parseColor("#ffd90d0d"));
        }else
        {
            name.setTextColor(Color.parseColor("#fffaf5f6"));
        }
        name_car.setText(localDB.CarName(currentmodel.getSendernum()));
        itemView.setOnClickListener(new View.OnClickListener() {

            @Override
            public void onClick(View arg0) {
                // Get the position
                MessageModel current = data.get(position);
                LocalDB controller = new LocalDB(context);
                controller.UpdateMessage((current.getId()));
                Intent intent=new Intent(context, ActDetailMessage.class);
                intent.putExtra("type",current.getType());
                intent.putExtra("message",current.getMessage());
                intent.putExtra("from",current.getSendernum());
                intent.putExtra("date", current.getDate());
                intent.putExtra("time",current.getTime());
                context.startActivity(intent);
            }
        });

        return itemView;
    }


}
