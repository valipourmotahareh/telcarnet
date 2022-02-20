package com.thimat.sockettelkarnet.activites;

import android.content.Context;
import android.os.Bundle;
import android.view.View;
import android.widget.ImageView;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.ItemTouchHelper;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.thimat.sockettelkarnet.adapter.RealmAdapterwarning;
import com.thimat.sockettelkarnet.localDb.LocalDB;
import com.thimat.sockettelkarnet.models.WarningModel;
import com.thimat.sockettelkarnet.R;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;
import io.realm.RealmResults;

public class ActWarning extends AppCompatActivity {
    @BindView(R.id.list_car)
    RecyclerView list_car;
    @BindView(R.id.deleteall)
    ImageView deleteall;
    LocalDB localDB;
    Context context;
    RealmResults<WarningModel> warningModels;
    RealmAdapterwarning realmAdapterwarning;
    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.act_warning);
        context = getApplicationContext();
        ButterKnife.bind(this);
        localDB = new LocalDB(context);
        GetWarning();
        delete();
    }
    //---------------------------------------------------- deleteall
    @OnClick(R.id.deleteall)
    public void deleteall(View view){
        localDB.DeleteAllWarning();
    }
    //---------------------------------------------------- get warning
    public void GetWarning(){
        warningModels=localDB.GetWarning();
        realmAdapterwarning=new RealmAdapterwarning(warningModels,true,context);
        list_car.setLayoutManager(new LinearLayoutManager(this));
        list_car.setAdapter(realmAdapterwarning);
    }
    //------------------------------------------------------- delete
    public void delete() {
        ItemTouchHelper.SimpleCallback simpleItemTouchCallback =new ItemTouchHelper.SimpleCallback(0,
                ItemTouchHelper.LEFT | ItemTouchHelper.RIGHT) {
            @Override
            public boolean onMove(RecyclerView recyclerView, RecyclerView.ViewHolder viewHolder, RecyclerView.ViewHolder target) {
                return false;
            }

            @Override
            public void onSwiped(RecyclerView.ViewHolder viewHolder, int direction) {
                final int position = viewHolder.getAdapterPosition();
                WarningModel current=warningModels.get(position);
                if (direction == ItemTouchHelper.LEFT) {    //if swipe left
                    AlertDialog.Builder builder = new AlertDialog.Builder(ActWarning.this); //alert for confirm to delete
                    builder.setMessage("آیا می خواهید این را از لیست حذف کنید؟");    //set message

                    //not removing items if cancel is done
                    //when click on DELETE
                    builder.setPositiveButton("حذف", (dialog, which) -> {
                            localDB.DeleteWarning(current.getId());
                        realmAdapterwarning.notifyItemRemoved(position);
                        // show_list.removeItemDecorationAt(position);  //then remove item
                        return;
                    }).setNegativeButton("لغو", (dialog, which) -> {
                        realmAdapterwarning.notifyItemRemoved(position + 1);
                        realmAdapterwarning.notifyItemRangeChanged(position, realmAdapterwarning.getItemCount());

                        return;
                    }).show();  //show alert dialog
                }
            }
        };
        ItemTouchHelper itemTouchHelper = new ItemTouchHelper(simpleItemTouchCallback);
        itemTouchHelper.attachToRecyclerView(list_car);
    }
}
