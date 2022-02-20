package com.thimat.sockettelkarnet.dialogActivity;

import android.content.Context;
import android.content.DialogInterface;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;

import com.thimat.sockettelkarnet.classes.SessionManager;
import com.thimat.sockettelkarnet.classes.ShowDialog;
import com.thimat.sockettelkarnet.R;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;

public class ActDefineLoginPass extends AppCompatActivity {
    @BindView(R.id.edit_pass)
    EditText edit_pass;
    @BindView(R.id.btn_save)
    Button btn_save;
    @BindView(R.id.btn_delete)
    Button btn_delete;
    @BindView(R.id.btn_cancel)
    Button btn_cancel;
    Context context;
    SessionManager sessionManager;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.act_defineloginpass);
        ButterKnife.bind(this);
        context = getApplicationContext();
        sessionManager = new SessionManager(context);
    }

    //---------------------------------------------------- btn_save click
    @OnClick(R.id.btn_save)
    public void btn_save(View view) {
        if (!edit_pass.getText().toString().equals("")){
            sessionManager.setLoginPassword(edit_pass.getText().toString());
            sessionManager.setLoginPass(true);
            finish();
        }else {
            Toast.makeText(ActDefineLoginPass.this,"لطفا پسورد ورود به نرم افزار را وارد کنید!",Toast.LENGTH_LONG).show();
        }
    }
    //---------------------------------------------------- btn_delete click
    @OnClick(R.id.btn_delete)
    public void btn_delete(View view){
        ShowDialog showDialog=new ShowDialog(ActDefineLoginPass.this);
        if (sessionManager.getLoginPass()){
            Showdialog(context.getString(R.string.dialog_txt));
        }else {
            showDialog.ShowDialog("نرم افزار پسورد نداره");
        }

    }
    //------------------------------------------------------------------------------
    public void Showdialog(String text) {
        final AlertDialog.Builder builder =
                new AlertDialog.Builder(ActDefineLoginPass.this);
        builder.setMessage(text);
        builder.setPositiveButton("تایید", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialogInterface, int i) {
                ShowDialog showDialog=new ShowDialog(ActDefineLoginPass.this);
                    sessionManager.setLoginPass(false);
                    showDialog.ShowDialogsuccess("پسورد حذف شد");
            }
        });
        builder.setNegativeButton("منصرف شدم", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
            }
        });
        builder.show();
    }
    //---------------------------------------------------- btn_cancelclick
    @OnClick(R.id.btn_cancel)
    public void btn_cancel(View view){
        finish();
    }
}
