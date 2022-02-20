package com.thimat.sockettelkarnet.adapter;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.constraintlayout.widget.ConstraintLayout;
import androidx.recyclerview.widget.RecyclerView;

import com.thimat.sockettelkarnet.models.RengeDateModel;
import com.thimat.sockettelkarnet.R;

import butterknife.BindView;
import butterknife.ButterKnife;
import io.realm.OrderedRealmCollection;

public class RealmShowListRengeDate  extends RecyclerView.Adapter<RealmShowListRengeDate.RengeDateHolder> {
    Context context;
    LayoutInflater layoutInflater;
    EventHandler eventHandler;
    OrderedRealmCollection<RengeDateModel> data;

    public RealmShowListRengeDate(@Nullable OrderedRealmCollection<RengeDateModel> data, boolean autoUpdate, Context context,
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
    public RengeDateHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = layoutInflater.inflate(R.layout.realm_show_listrenge, null, false);
        return new RengeDateHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull RengeDateHolder holder, int position) {
        RengeDateModel currentModel = data.get(position);
            holder.title.setText(String.valueOf(currentModel.getTitle()));
    }

    public class RengeDateHolder extends RecyclerView.ViewHolder {
        @BindView(R.id.title)
        TextView title;
        @BindView(R.id.select_title)
        ConstraintLayout select_title;

        public RengeDateHolder(View itemView) {
            super(itemView);
            ButterKnife.bind(this, itemView);
            //--------------------------------------------namecar
            select_title.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    try {
                        int position = getAdapterPosition();
                        RengeDateModel currentModel = data.get(position);
                        eventHandler.OnItemClick(currentModel.getID(), currentModel.getTitle());
                    } catch (Exception ex) {
                        ex.printStackTrace();
                    }
                }
            });

        }
    }

    //-------------------------------------------- interface for click listview
    public interface EventHandler {
        void OnItemClick(int id, String title);
    }

}
