package com.thimat.sockettelkarnet.adapter;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.recyclerview.widget.RecyclerView;

import com.thimat.sockettelkarnet.models.NewsModel;
import com.thimat.sockettelkarnet.R;

import butterknife.BindView;
import butterknife.ButterKnife;
import io.realm.OrderedRealmCollection;

public class RealmAdapterews extends RecyclerView.Adapter<RealmAdapterews.newsAdapter> {
    Context context;
    LayoutInflater layoutInflater;
    OrderedRealmCollection<NewsModel> data;
    public RealmAdapterews(@Nullable OrderedRealmCollection<NewsModel> data, boolean autoUpdate, Context context) {
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
    public newsAdapter onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view=layoutInflater.inflate(R.layout.realmadapternews,parent,false);
        return new newsAdapter(view);
    }

    @Override
    public void onBindViewHolder(@NonNull newsAdapter holder, int position) {
        NewsModel currentmodel=data.get(position);
        holder.date.setText(currentmodel.getDate());
        holder.title.setText(currentmodel.getTitle());
        holder.text.setText(currentmodel.getText());

    }

    public class newsAdapter extends RecyclerView.ViewHolder  {
        @BindView(R.id.title)
        TextView title;
        @BindView(R.id.date)
        TextView date;
        @BindView(R.id.text)
        TextView text;
        public newsAdapter(View itemView) {
            super(itemView);
            ButterKnife.bind(this,itemView);
        }
    }
}
