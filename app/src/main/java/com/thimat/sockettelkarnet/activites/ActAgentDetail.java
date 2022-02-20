package com.thimat.sockettelkarnet.activites;

import android.content.Context;
import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.text.SpannableString;
import android.text.style.UnderlineSpan;
import android.view.View;
import android.widget.TextView;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;

import com.thimat.sockettelkarnet.localDb.LocalDB;
import com.thimat.sockettelkarnet.models.AgentModel;
import com.thimat.sockettelkarnet.R;

import butterknife.BindView;
import butterknife.ButterKnife;

public class ActAgentDetail extends AppCompatActivity {
    @BindView(R.id.code_agent)
    TextView code_agent;
    @BindView(R.id.name_Agent)
    TextView name_Agent;
    @BindView(R.id.agent_address)
    TextView agent_address;
    @BindView(R.id.agent_tel)
    TextView agent_tel;
    @BindView(R.id.agent_cellphone)
    TextView agent_cellphone;
    @BindView(R.id.agent_location)
    TextView agent_location;
    @BindView(R.id.agent_city)
            TextView agent_city;
    LocalDB localDB;
    Context context;
    AgentModel currentmodel;
    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.act_agentdetail);
        ButterKnife.bind(this);
        context=getApplicationContext();
        localDB=new LocalDB(context);
       int agentid=getIntent().getIntExtra("ID",0);
        currentmodel=localDB.GetAgentFilter(agentid);
        code_agent.setText(currentmodel.getCod());
        name_Agent.setText(currentmodel.getName());
        agent_address.setText(currentmodel.getAddress());
        agent_tel.setText(currentmodel.getTel());
        agent_cellphone.setText(currentmodel.getMobil());
        SpannableString content = new SpannableString("موقعیت");
        content.setSpan(new UnderlineSpan(), 0, content.length(), 0);
        agent_location.setText(content);
        agent_city.setText(currentmodel.getCity());
        //-----------------------------------------------------------agent_location
        agent_location.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent i = new Intent(Intent.ACTION_VIEW);
                i.setData(Uri.parse(currentmodel.getLocation()));
                i.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
                context.startActivity(i);
            }
        });

    }
}
