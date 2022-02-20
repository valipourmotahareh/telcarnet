package com.thimat.sockettelkarnet.adapter;

import android.content.Context;
import android.content.Intent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.LinearLayout;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.recyclerview.widget.RecyclerView;

import com.thimat.sockettelkarnet.activites.ActAgentDetail;
import com.thimat.sockettelkarnet.models.AgentModel;
import com.thimat.sockettelkarnet.R;

import butterknife.BindView;
import butterknife.ButterKnife;
import io.realm.OrderedRealmCollection;

public class RealmAdapterAgent extends RecyclerView.Adapter<RealmAdapterAgent.AgentAdapter> {
    Context context;
    LayoutInflater layoutInflater;
    OrderedRealmCollection<AgentModel> data;

    public RealmAdapterAgent(@Nullable OrderedRealmCollection<AgentModel> data, boolean autoUpdate, Context context) {
        this.data=data;
        this.context = context;
        layoutInflater = LayoutInflater.from(context);
    }

    @Override
    public int getItemCount() {
        return data.size();
    }

    @NonNull
    @Override
    public RealmAdapterAgent.AgentAdapter onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = layoutInflater.inflate(R.layout.realmadapteragent, null, false);
        return new AgentAdapter(view);
    }

    @Override
    public void onBindViewHolder(@NonNull AgentAdapter holder, int position) {
        AgentModel currentmodel = data.get(position);
        holder.radif.setText(String.valueOf(position + 1));
        holder.name_Agent.setText(currentmodel.getName());
        holder.agent_address.setText(currentmodel.getAddress());
        holder.agent_cellphone.setText(currentmodel.getMobil());
        holder.agent_city.setText(currentmodel.getCity());
    }

    public class AgentAdapter extends RecyclerView.ViewHolder {
        @BindView(R.id.radif)
        TextView radif;
        @BindView(R.id.name_Agent)
        TextView name_Agent;
        @BindView(R.id.agent_address)
        TextView agent_address;
        @BindView(R.id.agent_cellphone)
        TextView agent_cellphone;
        @BindView(R.id.linear)
        LinearLayout linear;
        @BindView(R.id.agent_city)
        TextView agent_city;

        public AgentAdapter(View itemView) {
            super(itemView);
            ButterKnife.bind(this, itemView);
            //------------------------------------------------------------ show detail
            linear.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    int position = getAdapterPosition();
                    AgentModel current = data.get(position);
                    Intent intent = new Intent(context, ActAgentDetail.class);
                    intent.putExtra("ID", current.getId());
                    intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
                    context.startActivity(intent);
                }
            });
        }
    }
}
