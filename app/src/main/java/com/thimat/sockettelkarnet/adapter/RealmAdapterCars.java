package com.thimat.sockettelkarnet.adapter;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.constraintlayout.widget.ConstraintLayout;
import androidx.recyclerview.widget.RecyclerView;

import com.thimat.sockettelkarnet.classes.Jalali;
import com.thimat.sockettelkarnet.localDb.LocalDB;
import com.thimat.sockettelkarnet.models.CarModel;
import com.thimat.sockettelkarnet.R;

import java.text.SimpleDateFormat;
import java.util.Date;

import butterknife.BindView;
import butterknife.ButterKnife;
import io.realm.OrderedRealmCollection;

/**
 * Created by Valipour.Motahareh on 5/20/2018.
 */

public class RealmAdapterCars extends RecyclerView.Adapter<RealmAdapterCars.CarHolder> {
    Context context;
    LayoutInflater layoutInflater;
    EventHandler eventHandler;
    EVentRename eVentRename;
    EventDelete eventDelete;
    LocalDB localDB;
    long elapsedDays;
    long elapsedHours;
    long elapsedMinutes;
    long elapsedSeconds;
    OrderedRealmCollection<CarModel> data;
    public RealmAdapterCars(@Nullable OrderedRealmCollection<CarModel> data, boolean autoUpdate, Context context,
                            EventHandler eventHandler, EVentRename eVentRename, EventDelete eventDelete) {
        this.data=data;
        this.context = context;
        layoutInflater = LayoutInflater.from(context);
        this.eventHandler = eventHandler;
        this.eVentRename = eVentRename;
        this.eventDelete = eventDelete;
        localDB = new LocalDB(context);
    }

    @Override
    public int getItemCount() {
        return data.size();
    }

    @NonNull
    @Override
    public CarHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = layoutInflater.inflate(R.layout.ralm_card_item, null, false);
        return new CarHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull CarHolder holder, int position) {
        CarModel currentModel = data.get(position);
        if (currentModel.getCar_name().equals("")) {
            holder.namecar.setText("بی نام");
        } else {
            holder.namecar.setText(String.valueOf(currentModel.getCar_name()));
        }
        //--------------------------------------------------------
        try {
            SimpleDateFormat simpleDateFormat = new SimpleDateFormat("yyyy-MM-dd hh:mm:ss");
            Date currentTime =new Date();
            Date date1 = simpleDateFormat.parse(currentModel.getTstmp());
            Date date2 = simpleDateFormat.parse(Jalali.getEnglishFormattedDate(currentTime));
            printDifference(date1,date2);
            if (elapsedDays==0 & elapsedHours==0) {
                if (elapsedMinutes < 5) {
                    if (currentModel.getSpeed() > 0) {
                        holder.status_car.setText("در حال حرکت(" + currentModel.getSpeed() + "کیلومتر)");
                    } else {
                        holder.status_car.setText("وصل");
                    }
                } else {
                    holder.status_car.setText("آفلاین(" + elapsedMinutes + "دقیقه)");
                }
            }else{
                holder.status_car.setText("آفلاین(" + elapsedDays+"روز"+" "+elapsedHours+"ساعت"+" "+elapsedMinutes+ "دقیقه)");
            }
        } catch (Exception ex) {
            ex.printStackTrace();
        }
    }

    public class CarHolder extends RecyclerView.ViewHolder {
        @BindView(R.id.txt_namecar)
        TextView namecar;
        @BindView(R.id.icon_delete)
        ImageView icon_delete;
        @BindView(R.id.icon_rename)
        ImageView icon_rename;
        @BindView(R.id.status_car)
        TextView status_car;
        @BindView(R.id.linearitem)
        ConstraintLayout linearitem;
        @BindView(R.id.deleteedit)
        LinearLayout deleteedit;

        public CarHolder(View itemView) {
            super(itemView);
            ButterKnife.bind(this, itemView);
//            //----------------------------------------------------------------- linearitem
//            linearitem.setOnClickListener(new View.OnClickListener() {
//                @Override
//                public void onClick(View view) {
//                    if (deleteedit.getVisibility() == View.VISIBLE) {
//                        deleteedit.setVisibility(View.GONE);
//                    } else {
//                        deleteedit.setVisibility(View.VISIBLE);
//                    }
//                }
//            });
            //------------------------------------------------------------------ name car link
            namecar.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    int position = getAdapterPosition();
                    CarModel currentModel =data.get(position);
                    eventHandler.OnItemClick(currentModel.getId(), currentModel.getCar_code());
                }
            });
            //----------------------------------------------------------------- delete icon_delete
            icon_delete.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    int position = getAdapterPosition();
                    CarModel currentModel = data.get(position);
                    eventDelete.OnItemClick(currentModel.getId(), currentModel.getSync());
                }
            });
            //---------------------------------------------------------------- icon_rename
            icon_rename.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    int position = getAdapterPosition();
                    CarModel currentModel =data.get(position);
                    eVentRename.OnItemClick(currentModel.getCar_code(), currentModel.getId(), currentModel.getCar_name(),
                            currentModel.getModel(), currentModel.getPhone());
                }
            });

        }
    }

    //-------------------------------------------- interface for click listview
    public interface EventHandler {
        void OnItemClick(int idcar, String car_code);
    }

    //------------------------------------------- interface rename
    public interface EVentRename {
        void OnItemClick(String car_code, int car_id, String car_name, int model, String phone);
    }

    //------------------------------------------- interface delete
    public interface EventDelete {
        void OnItemClick(int car_id, int sync);
    }
    //----------------------------------------------------------------
    public long printDifference(Date startDate, Date endDate) {
        //milliseconds
        long different = endDate.getTime() - startDate.getTime();

        System.out.println("startDate : " + startDate);
        System.out.println("endDate : "+ endDate);
        System.out.println("different : " + different);

        long secondsInMilli = 1000;
        long minutesInMilli = secondsInMilli * 60;
        long hoursInMilli = minutesInMilli * 60;
        long daysInMilli = hoursInMilli * 24;

        elapsedDays =Math.round(different / daysInMilli);
        different = different % daysInMilli;

        elapsedHours = different / hoursInMilli;
        different = different % hoursInMilli;

        elapsedMinutes = different / minutesInMilli;
        different = different % minutesInMilli;

        elapsedSeconds = different / secondsInMilli;

        System.out.printf(
                "%d days, %d hours, %d minutes, %d seconds%n",
                elapsedDays, elapsedHours, elapsedMinutes, elapsedSeconds);
        return elapsedMinutes;
    }
}
