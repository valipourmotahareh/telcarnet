package com.thimat.sockettelkarnet.adapter;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.recyclerview.widget.RecyclerView;

import com.thimat.sockettelkarnet.models.GuideModel;
import com.thimat.sockettelkarnet.R;

import butterknife.BindView;
import butterknife.ButterKnife;
import io.realm.OrderedRealmCollection;

public class RealmAdapterGuide extends RecyclerView.Adapter<RealmAdapterGuide.GuideAdapter> {
    Context context;
    LayoutInflater layoutInflater;
    OrderedRealmCollection<GuideModel> data;
    public RealmAdapterGuide(@Nullable OrderedRealmCollection<GuideModel> data, boolean autoUpdate, Context context) {
        this.data=data;
        this.context=context;
        layoutInflater=LayoutInflater.from(context);
    }
    @Override
    public int getItemCount() {
        return data.size();
    }
    @NonNull
    @Override
    public GuideAdapter onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view=layoutInflater.inflate(R.layout.realmadapterguide,null,false);
        return new GuideAdapter(view);
    }

    @Override
    public void onBindViewHolder(@NonNull GuideAdapter holder, int position) {
        GuideModel currentmodel=data.get(position);
        holder.model.setText(currentmodel.getModel());
        holder.title.setText(currentmodel.getTitle());
    }

    public class GuideAdapter extends RecyclerView.ViewHolder  {
        @BindView(R.id.model)
        TextView model;
        @BindView(R.id.title)
        TextView title;
        public GuideAdapter(View itemView) {
            super(itemView);
            ButterKnife.bind(this,itemView);
        }
    }
}
