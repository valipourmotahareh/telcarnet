package com.thimat.sockettelkarnet.activites;

import android.content.Context;
import android.content.Intent;
import android.media.MediaPlayer;
import android.os.Bundle;
import android.view.View;
import android.widget.LinearLayout;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;

import com.thimat.sockettelkarnet.classes.SessionManager;
import com.thimat.sockettelkarnet.R;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;

public class ActBtnTelkarnet extends AppCompatActivity {
    @BindView(R.id.agent)
    LinearLayout agent;
    @BindView(R.id.news)
    LinearLayout news;
    @BindView(R.id.guide)
    LinearLayout guide;
    @BindView(R.id.contactus)
    LinearLayout contactus;
    Context context;
    MediaPlayer mp;
    SessionManager sessionManager;
    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.btn_karnet);
        ButterKnife.bind(this);
        context = getApplicationContext();
        sessionManager=new SessionManager(context);
        mp = MediaPlayer.create(this, R.raw.click);

    }
    //----------------------------------------------- btn contactus
    @OnClick(R.id.contactus)
    public void btn_contactus(View view){
        if (sessionManager.GetSoundButton()){
            mp.start();
        }
        Intent intent = new Intent(context, ActContactUs.class);
        startActivity(intent);
    }
    //------------------------------------------------ btn click
    @OnClick(R.id.agent)
    public void btn_agent(View view) {
        if (sessionManager.GetSoundButton()){
            mp.start();
        }
        Intent intent = new Intent(context, ActShowAgent.class);
        startActivity(intent);
    }

    //------------------------------------------------------- btn news
    @OnClick(R.id.news)
    public void btn_news(View view) {
        if (sessionManager.GetSoundButton()){
            mp.start();
        }
        Intent intent = new Intent(context, ActShowNews.class);
        startActivity(intent);
    }

    //------------------------------------------------------ btn guide
    @OnClick(R.id.guide)
    public void btn_guide(View view) {
        if (sessionManager.GetSoundButton()){
            mp.start();
        }
        Intent intent = new Intent(context, ActShowGuide.class);
        startActivity(intent);
    }

}
