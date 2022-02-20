package com.thimat.sockettelkarnet.adapter;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.recyclerview.widget.RecyclerView;

import com.thimat.sockettelkarnet.localDb.LocalDB;
import com.thimat.sockettelkarnet.models.WarningModel;
import com.thimat.sockettelkarnet.R;

import butterknife.BindView;
import butterknife.ButterKnife;
import io.realm.OrderedRealmCollection;

public class RealmAdapterwarning extends RecyclerView.Adapter<RealmAdapterwarning.warning> {
    Context context;
    LayoutInflater layoutInflater;
    LocalDB localDB;
    OrderedRealmCollection<WarningModel> data;
    public RealmAdapterwarning(@Nullable OrderedRealmCollection<WarningModel> data, boolean autoUpdate,
                               Context context) {
        this.data=data;
        this.context = context;
        layoutInflater = LayoutInflater.from(context);
        localDB=new LocalDB(context);
    }

    @Override
    public int getItemCount() {
        return data.size();
    }

    @NonNull
    @Override
    public warning onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = layoutInflater.inflate(R.layout.realm_warning_item, null, false);
        return new warning(view);
    }

    @Override
    public void onBindViewHolder(@NonNull warning holder, int position) {
        WarningModel current=data.get(position);
        holder.name_car.setText(localDB.GetCarName(current.getIdcar()));
        holder.txt_warning.setText(current.getTxtwarning());
        holder.date_warning.setText(current.getDate());
        //----------------------------------
        if (current.getTxtwarning().equals(context.getString(R.string.warning_switch_on))){
            holder.img_warning.setBackgroundResource(R.drawable.switch_on);
        }else if (current.getTxtwarning().equals(context.getString(R.string.warning_switch_off))){
            holder.img_warning.setBackgroundResource(R.drawable.switch_off);
        }else if (current.getTxtwarning().equals(context.getString(R.string.warning_Ultra_sensor))){
            holder.img_warning.setBackgroundResource(R.drawable.ultrasensor);
        }else if (current.getTxtwarning().equals(context.getString(R.string.warning_getShock_sensor))){
            holder.img_warning.setBackgroundResource(R.drawable.shocksensor);
        }else if (current.getTxtwarning().equals(context.getString(R.string.warning_disconnect_battery))){
            holder.img_warning.setBackgroundResource(R.drawable.batoffline);
        }else if (current.getTxtwarning().equals(context.getString(R.string.warning_connect_battery))){
            holder.img_warning.setBackgroundResource(R.drawable.baton);
        }else if (current.getTxtwarning().equals(context.getString(R.string.warning_open_door))){
            holder.img_warning.setBackgroundResource(R.drawable.opendoor);
        }else if (current.getTxtwarning().equals(context.getString(R.string.warning_close_door))){
            holder.img_warning.setBackgroundResource(R.drawable.closedoor);
        }else if (current.getTxtwarning().equals(context.getString(R.string.warning_Enter))){
            holder.img_warning.setBackgroundResource(R.drawable.geoenter);
        }else if (current.getTxtwarning().equals(context.getString(R.string.warning_exit))){
            holder.img_warning.setBackgroundResource(R.drawable.geoexit);
        }
    }

    public class warning extends RecyclerView.ViewHolder{
        @BindView(R.id.name_car)
        TextView name_car;
        @BindView(R.id.txt_warning)
        TextView txt_warning;
        @BindView(R.id.date_warning)
        TextView date_warning;
        @BindView(R.id.img_warning)
        ImageView img_warning;
        public warning(View itemView) {
            super(itemView);
            ButterKnife.bind(this, itemView);
        }
    }
}
