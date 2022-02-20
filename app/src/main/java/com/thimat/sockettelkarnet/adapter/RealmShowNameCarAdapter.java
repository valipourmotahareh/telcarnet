package com.thimat.sockettelkarnet.adapter;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.recyclerview.widget.RecyclerView;

import com.thimat.sockettelkarnet.models.CarModel;
import com.thimat.sockettelkarnet.R;

import butterknife.BindView;
import butterknife.ButterKnife;
import io.realm.OrderedRealmCollection;

public class RealmShowNameCarAdapter extends RecyclerView.Adapter<RealmShowNameCarAdapter.NameCarHolder> {
    Context context;
    LayoutInflater layoutInflater;
    EventHandler eventHandler;
    OrderedRealmCollection<CarModel> data;

    public RealmShowNameCarAdapter(@Nullable OrderedRealmCollection<CarModel> data, boolean autoUpdate, Context context,
                                   EventHandler eventHandler) {
        this.data=data;
        this.context = context;
        layoutInflater = LayoutInflater.from(context);
        this.eventHandler = eventHandler;
    }

    @Override
    public int getItemCount() {
        return data.size();
    }

    @NonNull
    @Override
    public NameCarHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = layoutInflater.inflate(R.layout.realm_show_name_car, null, false);
        return new NameCarHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull NameCarHolder holder, int position) {
        CarModel currentModel = data.get(position);
        if (currentModel.getCar_name().equals("")) {
            holder.namecar.setText("بی نام");
        } else {
            holder.namecar.setText(String.valueOf(currentModel.getCar_name()));
        }
    }

    public class NameCarHolder extends RecyclerView.ViewHolder {
        @BindView(R.id.txt_namecar)
        TextView namecar;

        public NameCarHolder(View itemView) {
            super(itemView);
            ButterKnife.bind(this, itemView);
            //--------------------------------------------namecar
            namecar.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    try {
                        int position = getAdapterPosition();
                        CarModel currentModel = data.get(position);
                        eventHandler.OnItemClick(currentModel.getId(), currentModel.getCar_code(),currentModel.getPhone());
                    } catch (Exception ex) {
                        ex.printStackTrace();
                    }
                }
            });

        }
    }

    //-------------------------------------------- interface for click listview
    public interface EventHandler {
        void OnItemClick(int idcar, String car_code,String phone);
    }

}
