package com.thimat.sockettelkarnet.activites;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;

import com.thimat.sockettelkarnet.classes.SessionManager;
import com.thimat.sockettelkarnet.R;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;

public class ActLoginPasswordApp extends AppCompatActivity {
    @BindView(R.id.btn_login)
    Button btn_login;
    @BindView(R.id.edit_pass)
    EditText edit_pass;
    Context context;
    SessionManager sessionManager;
    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.act_loginpasswordapp);
        ButterKnife.bind(this);
        context=getApplicationContext();
        sessionManager=new SessionManager(context);
    }
    //------------------------------------------------- btn_login
    @OnClick(R.id.btn_login)
    public void btn_login(View view){
        if (!edit_pass.getText().toString().equals("")){
            if (edit_pass.getText().toString().equals(sessionManager.getLoginPassword())){
                Intent intent = new Intent(context, ActMainActivity.class);
                startActivity(intent);
                finish();
            }else {
                Toast.makeText(ActLoginPasswordApp.this,"پسورد وارد شده اشتباه است!",Toast.LENGTH_LONG).show();
            }
        }
    }
}
