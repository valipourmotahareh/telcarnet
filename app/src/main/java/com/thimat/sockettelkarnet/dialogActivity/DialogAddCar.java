package com.thimat.sockettelkarnet.dialogActivity;

import android.annotation.SuppressLint;
import android.content.Context;
import android.content.Intent;
import android.database.Cursor;
import android.net.Uri;
import android.os.Bundle;
import android.provider.ContactsContract;
import android.util.Log;
import android.view.MotionEvent;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Spinner;
import android.widget.Toast;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;

import com.thimat.sockettelkarnet.classes.SessionManager;
import com.thimat.sockettelkarnet.localDb.LocalDB;
import com.thimat.sockettelkarnet.R;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;

public class DialogAddCar extends AppCompatActivity {
    @BindView(R.id.edt_namecar)
    EditText edt_namecar;
    @BindView(R.id.edt_phonenumber)
    EditText edt_phonenumber;
    @BindView(R.id.spinnermodel)
    Spinner spinnermodel;
    @BindView(R.id.btn_save)
    Button btn_save;
    @BindView(R.id.btn_cancel)
    Button btn_cancel;
    Context context;
    LocalDB localDB;
    boolean getTcn_v200=true;
    boolean getTcn_v300=false;
    SessionManager sessionManager;
    int modelcar=1;
    int count = 0;
    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.add_car);
        context = getApplicationContext();
        ButterKnife.bind(this);
        localDB = new LocalDB(context);
        sessionManager=new SessionManager(context);
        String[] arraySpinner = new String[]{
                "ردیاب", "دزدگیرـ ردیاب"
        };
        ArrayAdapter<String> adapter = new ArrayAdapter<String>(this,
                android.R.layout.simple_spinner_item, arraySpinner);
        spinnermodel.setAdapter(adapter);
        spinnermodel.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                if (position==0){
                    getTcn_v200=true;
                    getTcn_v300=false;
                    modelcar=1;
                }else if (position==1){
                    getTcn_v200=false;
                    getTcn_v300=true;
                    modelcar=2;
                }
            }

            @Override
            public void onNothingSelected(AdapterView<?> parent) {

            }
        });
        //---------------------------------------------- edt_phonenumber click
        edt_phonenumber.setOnTouchListener(new View.OnTouchListener() {
            @SuppressLint("ClickableViewAccessibility")
            final int DRAWABLE_LEFT = 0;
            final int DRAWABLE_TOP = 1;
            final int DRAWABLE_RIGHT = 2;
            final int DRAWABLE_BOTTOM = 3;

            @Override
            public boolean onTouch(View view, MotionEvent motionEvent) {
                if (motionEvent.getRawX() >= (edt_phonenumber.getRight() - edt_phonenumber.getCompoundDrawables()[DRAWABLE_RIGHT].getBounds().width())) {
                    if (count == 0) {
                        Intent intent = new Intent(Intent.ACTION_PICK, ContactsContract.Contacts.CONTENT_URI);
                        intent.setType(ContactsContract.CommonDataKinds.Phone.CONTENT_TYPE);
                        startActivityForResult(intent, 1);
                    }
                    count++;
                    return true;
                }
                count = 0;
                return false;
            }
        });
    }
    //------------------------------------------------------------ btn_cancel
    @OnClick(R.id.btn_cancel)
    public void btn_cancel(View view){
        finish();
    }
    //------------------------------------------------------------ btn_save
    @OnClick(R.id.btn_save)
    public void btn_save(View view) {
        //-------------------------------------------------------- insert car to local
        String phonenumber=edt_phonenumber.getText().toString().trim().replace(" ","");
        localDB.SaveLocalCar(edt_namecar.getText().toString(),phonenumber,
                modelcar,0,sessionManager.getPerson_Id());
    }
    //------------------------------------------------------------------onActivityResult
    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        // TODO Auto-generated method stub
        super.onActivityResult(requestCode, resultCode, data);
        if (resultCode == RESULT_OK) {
            if (resultCode == RESULT_OK) {
                    Cursor cursor = null;
                    Uri uri = data.getData();
                    cursor = getContentResolver().query(uri, new String[]{ContactsContract.CommonDataKinds.Phone.NUMBER}, null, null, null);
                    if (cursor != null && cursor.moveToNext()) {
                        String phone = cursor.getString(0).trim().replace(" ","");
                        if(phone.startsWith("+98")) phone=phone.replace("+98","0");
                        Log.e("addCar","phone=="+phone);
                        if (phone.length()<0 || phone.length()>11)  Toast.makeText(context,"شماره تلفن باید 11 رقم باشد" ,Toast.LENGTH_LONG).show();
                        else   edt_phonenumber.setText(phone);
                        count = 0;
                        // Do something with phone
                    }

            }
        }
    }
}
