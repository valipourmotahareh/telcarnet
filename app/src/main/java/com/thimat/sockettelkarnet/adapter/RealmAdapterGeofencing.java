package com.thimat.sockettelkarnet.adapter;

import android.app.AlertDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.CompoundButton;
import android.widget.ImageView;
import android.widget.Switch;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.recyclerview.widget.RecyclerView;

import com.thimat.sockettelkarnet.localDb.LocalDB;
import com.thimat.sockettelkarnet.models.GeofenceModel;
import com.thimat.sockettelkarnet.R;

import butterknife.BindView;
import butterknife.ButterKnife;
import io.realm.OrderedRealmCollection;

public class RealmAdapterGeofencing extends RecyclerView.Adapter<RealmAdapterGeofencing.GeoHolder> {
    LayoutInflater layoutInflater;
    Context context;
    LocalDB localDB;
    OrderedRealmCollection<GeofenceModel>  data;

    public RealmAdapterGeofencing(@Nullable OrderedRealmCollection<GeofenceModel>
                                          data, boolean autoUpdate, Context context) {
        this.data=data;
        this.context = context;
        layoutInflater = LayoutInflater.from(context);
        localDB = new LocalDB(context);
    }

    @NonNull
    @Override
    public GeoHolder onCreateViewHolder(@NonNull ViewGroup viewGroup, int i) {
        View view = layoutInflater.inflate(R.layout.relam_adapter_geofencing, null, false);
        return new GeoHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull GeoHolder geoHolder, int i) {
        GeofenceModel geofenceModel = data.get(i);
        geoHolder.txt_namegeo.setText(geofenceModel.getNamegeo());
        geoHolder.active_deactive_geo.setChecked(geofenceModel.getActdeac());
        geoHolder.txt_namecar.setText(localDB.GetCarName(geofenceModel.getCar_code()));
        geoHolder.active_deactive_geo.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            final int count = 0;
            @Override
            public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
                try {
                    localDB.Updateactdeac(geofenceModel.getId(), isChecked);

                }catch (Exception ex){
                    ex.printStackTrace();
                }

            }
        });

    }

    @Override
    public int getItemCount() {
        return data.size();
    }

    //--------------------------
    public class GeoHolder extends RecyclerView.ViewHolder {
        @BindView(R.id.txt_namegeo)
        TextView txt_namegeo;
        @BindView(R.id.delete_geo)
        ImageView delete_geo;
        @BindView(R.id.active_deactive_geo)
        Switch active_deactive_geo;
        @BindView(R.id.txt_namecar)
        TextView txt_namecar;

        public GeoHolder(@NonNull View itemView) {
            super(itemView);
            ButterKnife.bind(this, itemView);
            //---------------------------------------------- delete_geo
            delete_geo.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    new AlertDialog.Builder(context)
                            .setMessage(context.getString(R.string.reminder_removal_alert))
                            .setPositiveButton(context.getString(R.string.reminder_removal_alert_positive),
                                    new DialogInterface.OnClickListener() {
                                        @Override
                                        public void onClick(DialogInterface dialog,
                                                            int which) {
                                            //-------------------------- delete geofencing
                                            int position = getAdapterPosition();
                                            GeofenceModel geofenceModel = data.get(position);
                                            localDB.DeleteGeo(geofenceModel.getId());
                                        }
                                    })
                            .setNegativeButton(context.getString(R.string.reminder_removal_alert_negative),
                                    new DialogInterface.OnClickListener() {

                                        @Override
                                        public void onClick(DialogInterface dialog,
                                                            int which) {
                                            return;
                                        }

                                    }).setCancelable(true).show();
                }
            });
            //----------------------------------------------------active_deactive_geo

        }
    }

}
