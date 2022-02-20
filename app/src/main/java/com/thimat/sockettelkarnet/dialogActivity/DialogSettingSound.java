package com.thimat.sockettelkarnet.dialogActivity;

import android.content.Context;
import android.os.Bundle;
import android.widget.CompoundButton;
import android.widget.Switch;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;

import com.thimat.sockettelkarnet.classes.SessionManager;
import com.thimat.sockettelkarnet.R;

import butterknife.BindView;
import butterknife.ButterKnife;

public class DialogSettingSound extends AppCompatActivity {
    @BindView(R.id.setting_sound_botton)
    Switch setting_sound_botton;
    @BindView(R.id.setting_sound_alarm)
    Switch setting_sound_alarm;
    Context context;
    SessionManager sessionManager;
    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.dialog_settingsound);
        ButterKnife.bind(this);
        context=getApplicationContext();
        sessionManager=new SessionManager(context);
        //------------------------------------------
        setting_sound_botton.setChecked(sessionManager.GetSoundButton());
        //------------------------------------------
        setting_sound_alarm.setChecked(sessionManager.GetSoundAlarm());
        //----------------------------------------------------
        setting_sound_botton.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            final int count=0;
            @Override
            public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {

                sessionManager.SetSoundButton(isChecked);

            }
        });
        //---------------------------------------------------------
        setting_sound_alarm.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            final int count=0;
            @Override
            public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
                sessionManager.SetSoundAlarm(isChecked);
            }
        });
    }
}
