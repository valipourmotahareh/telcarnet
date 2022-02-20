package com.thimat.sockettelkarnet.activites;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.widget.ListView;

import com.thimat.sockettelkarnet.adapter.RealmAdapterInbox;
import com.thimat.sockettelkarnet.localDb.LocalDB;
import com.thimat.sockettelkarnet.models.MessageModel;
import com.thimat.sockettelkarnet.R;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;
import io.realm.RealmResults;

public class ActInboxActivity extends Activity {
    @BindView(R.id.list)
    ListView listview;
    public String numsWhere;
    public List<String> myinbox = new ArrayList<String>();
    public ListView myList;
    Context context;
    LocalDB localDB;
    ArrayList<HashMap<String, String>> data;
    RealmResults<MessageModel> userList1;
    RealmAdapterInbox adapter;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.inbox);
        ButterKnife.bind(this);
        context=getApplicationContext();
        localDB=new LocalDB(context);
        onRestart();
        SharedPreferences shared = getSharedPreferences("Prefs", MODE_PRIVATE);
        SharedPreferences.Editor editor = shared.edit();
        numsWhere = shared.getString("phonenumber", "");
        String phone = numsWhere.replaceFirst("0", "+98");
        //
        userList1 = localDB.GetMessages();
        // Pass the results into ListViewAdapter.java
        adapter = new RealmAdapterInbox(ActInboxActivity.this, userList1);
        // Set the adapter to the ListView
        listview.setAdapter(adapter);
    }
    //----------------------------------------------------------------------------- onActivityResult
    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);

    }
    //-----------------------------------------------------------------------------onResume
    @Override
    protected void onResume() {
        super.onResume();
        this.onCreate(null);
    }
    //-----------------------------------------------------------------------------onCreateOptionsMenu
    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        MenuInflater inflater = getMenuInflater();
        inflater.inflate(R.menu.main, menu);
        return true;
    }
    //-------------------------------------------------------------------------------When Options Menu is selected
    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()) {
            case R.id.clearinbox:
                localDB.DeleteMessage();
                onResume();
                return true;
            default:
                return true;
        }
    }
}