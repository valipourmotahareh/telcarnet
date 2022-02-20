package com.thimat.sockettelkarnet.activites;

import android.app.Activity;
import android.app.ProgressDialog;
import android.content.Context;
import android.content.Intent;
import android.graphics.drawable.BitmapDrawable;
import android.graphics.drawable.Drawable;
import android.os.Bundle;
import android.view.Display;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.Window;
import android.view.WindowManager;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.PopupWindow;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.mohamadamin.persianmaterialdatetimepicker.date.DatePickerDialog;
import com.mohamadamin.persianmaterialdatetimepicker.time.RadialPickerLayout;
import com.mohamadamin.persianmaterialdatetimepicker.time.TimePickerDialog;
import com.mohamadamin.persianmaterialdatetimepicker.utils.PersianCalendar;
import com.thimat.sockettelkarnet.adapter.RealmShowListRengeDate;
import com.thimat.sockettelkarnet.classes.Jalali;
import com.thimat.sockettelkarnet.classes.JalaliCalendar;
import com.thimat.sockettelkarnet.localDb.LocalDB;
import com.thimat.sockettelkarnet.models.offline;
import com.thimat.sockettelkarnet.R;
import com.thimat.sockettelkarnet.rest.ClientConfigs;
import com.thimat.sockettelkarnet.rest.OperationService;
import com.thimat.sockettelkarnet.rest.ServiceProvider;

import org.json.JSONException;
import org.json.JSONObject;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

/**
 * Created by Valipour.Motahareh on 5/21/2018.
 */

public class ActSelectDate extends AppCompatActivity {
    @BindView(R.id.today)
    ImageView today;
    @BindView(R.id.yesterday)
    ImageView yesterday;
    @BindView(R.id.lastweek)
    ImageView lastweek;
    @BindView(R.id.lastmonth)
    ImageView lastmonth;
    @BindView(R.id.selectdate)
    ImageView selectdate;
    @BindView(R.id.getdata)
    Button Getdata;
    @BindView(R.id.linear_select_date_time)
    LinearLayout linear_select_date_time;
    @BindView(R.id.start_date)
    LinearLayout start_date;
    @BindView(R.id.end_date)
    LinearLayout end_date;
    @BindView(R.id.date_start)
    TextView date_start;
    @BindView(R.id.time_start)
    TextView time_start;
    @BindView(R.id.date_end)
    TextView date_end;
    @BindView(R.id.time_end)
    TextView time_end;
    @BindView(R.id.txt_select_date)
    TextView txt_select_date;
    DatePickerDialog.OnDateSetListener from_dateListener, to_dateListener;
    TimePickerDialog.OnTimeSetListener fromTimeSetListener, toTimeSetListener;
    Context context;
    private String thisfromDate;
    private String thistoDate;
    Drawable drawableselet;
    Drawable Drawable;
    String startdate = "";
    String enddate = "";
    String starttime = "00:00:00";
    String endtime = "23:59:00";
    private OperationService mOService;
    LocalDB localDB;
    private ProgressDialog progressDialog;
    int id;
    RealmShowListRengeDate stringArrayAdapter;
    List<String> listdate;
    int travel;
    int showdate=1;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.act_selectdate);
        context = getApplicationContext();
        ButterKnife.bind(this);
        localDB = new LocalDB(context);
        Calendar cal = Calendar.getInstance();
        date_start.setText(Jalali.getFarsiDate(cal.getTime()));
        time_start.setText("00:00:00");
        startdate=Jalali.getFarsiDate(cal.getTime());
        date_end.setText(Jalali.getFarsiDate(cal.getTime()));
        time_end.setText("23:59:00");
        enddate=Jalali.getFarsiDate(cal.getTime());
        travel = 0;
        showdate=0;
        startdate = Jalali.getEnglishparseDate(cal.getTime());
        enddate = startdate;
        //---------------------------------------------------
        ServiceProvider mSProvider = new ServiceProvider(ServiceProvider.WEBSERVICE_GENERAL, ClientConfigs.timeout);
        mOService = mSProvider.getOpService();
        //--------------------------------------------------
        id = getIntent().getIntExtra("id", 0);
        drawableselet = getResources().getDrawable(R.drawable.informationicon_select);
        Drawable = getResources().getDrawable(R.drawable.informationicon);

        //--------------------------------------------------------from_dateListener------------------
        from_dateListener = new DatePickerDialog.OnDateSetListener() {
            @Override
            public void onDateSet(DatePickerDialog view, int year, int monthOfYear, int dayOfMonth) {
                String smonth = (monthOfYear + 1) + "";
                String sday = dayOfMonth + "";
                if (monthOfYear + 1 < 10) {
                    smonth = "0" + smonth;
                }
                if (dayOfMonth < 10) {
                    sday = "0" + dayOfMonth;
                }
                thisfromDate = year + "/" + smonth + "/" + sday;
                date_start.setText(thisfromDate);
//                sessionManager.SetFromDate_salepayment(thisfromDate);
                Date fromdate = JalaliCalendar.getGregorianDate(thisfromDate);
                startdate = Jalali.getEnglishparseDate(fromdate);
                //----------------------------------------------------------
                PersianCalendar now = new PersianCalendar();
                TimePickerDialog tpd = TimePickerDialog.newInstance(
                        fromTimeSetListener,
                        now.get(PersianCalendar.HOUR_OF_DAY),
                        now.get(PersianCalendar.MINUTE),
                        false);
                tpd.show(getFragmentManager(), "TIMESRAERPICKER");

            }
        };
        //--------------------------------------------------------to_dateListener-------------------
        to_dateListener = new DatePickerDialog.OnDateSetListener() {
            @Override
            public void onDateSet(DatePickerDialog view, int year, int monthOfYear, int dayOfMonth) {
                String smonth = (monthOfYear + 1) + "";
                String sday = dayOfMonth + "";
                if (monthOfYear + 1 < 10) {
                    smonth = "0" + smonth;
                }
                if (dayOfMonth < 10) {
                    sday = "0" + dayOfMonth;
                }
                thistoDate = year + "/" + smonth + "/" + sday;
                date_end.setText(thistoDate);
//                sessionManager.SetToDate_salepayment(thistoDate);
                Date todate = JalaliCalendar.getGregorianDate(thistoDate);
                enddate = Jalali.getEnglishparseDate(todate);
                //----------------------------------------------------------
                PersianCalendar now = new PersianCalendar();
                TimePickerDialog tpd = TimePickerDialog.newInstance(
                        toTimeSetListener,
                        now.get(PersianCalendar.HOUR_OF_DAY),
                        now.get(PersianCalendar.MINUTE),
                        false);
                tpd.show(getFragmentManager(), "TIMEENDPICKER");
            }
        };
        //------------------------------------------------fromTimeSetListener
        fromTimeSetListener = new TimePickerDialog.OnTimeSetListener() {
            @Override
            public void onTimeSet(RadialPickerLayout view, int hourOfDay, int minute) {
                int mHour = hourOfDay;
                int mMinute = minute;
                String min = minute + "";
                if (minute < 10) {
                    min = "0" + minute;
                }
                String hou = hourOfDay + "";
                if (hourOfDay < 10) {
                    hou = "0" + hourOfDay;
                }
                String mTime = hou + ":" + min + ":00";
                time_start.setText(mTime);
                starttime = mTime;
            }
        };
        //------------------------------------------------toTimeSetListener
        toTimeSetListener = new TimePickerDialog.OnTimeSetListener() {
            @Override
            public void onTimeSet(RadialPickerLayout view, int hourOfDay, int minute) {
                int mHour = hourOfDay;
                int mMinute = minute;
                String min = minute + "";
                if (minute < 10) {
                    min = "0" + minute;
                }
                String hou = hourOfDay + "";
                if (hourOfDay < 10) {
                    hou = "0" + hourOfDay;
                }
                String mTime = hou + ":" + min + ":00";
                time_end.setText(mTime);
                endtime = mTime;
            }
        };


    }


    //------------------------------------------------------- today ImageView
    @OnClick(R.id.today)
    public void Methodtoday(View view) {
        //---------------------------------------------- change imageview
        today.setImageDrawable(drawableselet);
        selectdate.setImageDrawable(Drawable);
        lastmonth.setImageDrawable(Drawable);
        lastweek.setImageDrawable(Drawable);
        yesterday.setImageDrawable(Drawable);
        //------------------------------------------------
        Calendar cal = Calendar.getInstance();
        startdate = Jalali.getEnglishparseDate(cal.getTime());
        enddate = startdate;

    }

    //-------------------------------------------------------  yesterday ImageView
    @OnClick(R.id.yesterday)
    public void Methodyesterday() {
        SimpleDateFormat simpleDateFormat = new SimpleDateFormat("yyyy-MM-dd");
        //------------------------------------------------ change imageview
        yesterday.setImageDrawable(drawableselet);
        today.setImageDrawable(Drawable);
        selectdate.setImageDrawable(Drawable);
        lastmonth.setImageDrawable(Drawable);
        lastweek.setImageDrawable(Drawable);
        //----------------------------------------------
        Calendar cal = Calendar.getInstance();
        cal.add(Calendar.DATE, -1);
        startdate = Jalali.getEnglishparseDate(cal.getTime());
        enddate = startdate;
    }

    //------------------------------------------------------  lastweek ImageView
    @OnClick(R.id.lastweek)
    public void Methodlastweek(View View) {
        //----------------------------------------- change icon
        lastweek.setImageDrawable(drawableselet);
        today.setImageDrawable(Drawable);
        selectdate.setImageDrawable(Drawable);
        lastmonth.setImageDrawable(Drawable);
        yesterday.setImageDrawable(Drawable);
        //---------------------------------------------
        Calendar dateto = Calendar.getInstance();
        dateto.add(Calendar.WEEK_OF_MONTH, -1);
        dateto.set(Calendar.DAY_OF_YEAR, -7);
        Date todate = dateto.getTime();
        startdate = Jalali.getEnglishparseDate(todate);
        Calendar date = Calendar.getInstance();
        Date fromdate = date.getTime();
        enddate = Jalali.getEnglishparseDate(fromdate);
    }

    //------------------------------------------------------  lastmonth ImageView
    @OnClick(R.id.lastmonth)
    public void Methodlastmonth(View view) {
        //----------------------------------------------------- change icon
        lastmonth.setImageDrawable(drawableselet);
        today.setImageDrawable(Drawable);
        selectdate.setImageDrawable(Drawable);
        lastweek.setImageDrawable(Drawable);
        yesterday.setImageDrawable(Drawable);
        //------------------------------------------------------ get date
        Calendar dateto = Calendar.getInstance();
        dateto.add(Calendar.WEEK_OF_MONTH, -1);
        dateto.set(Calendar.DAY_OF_YEAR, -30);
        Date todate = dateto.getTime();
        startdate = Jalali.getEnglishparseDate(todate);
        Calendar date = Calendar.getInstance();
        Date fromdate = date.getTime();
        enddate = Jalali.getEnglishparseDate(fromdate);

    }

    //-----------------------------------------------------
    @OnClick(R.id.selectdate)
    public void Methodselectdate(View view) {
        selectdate.setImageDrawable(drawableselet);
        today.setImageDrawable(Drawable);
        lastmonth.setImageDrawable(Drawable);
        lastweek.setImageDrawable(Drawable);
        yesterday.setImageDrawable(Drawable);

    }

    //------------------------------------------------------- getdata click
    @OnClick(R.id.getdata)
    public void GetData(View view) {
        displayProgress(getString(R.string.toast_msg_get_data));
        Call<offline> call = mOService.GetOffline(CreateJson());
        call.enqueue(new Callback<offline>() {
            @Override
            public void onResponse(Call<offline> call, Response<offline> response) {
                if (response.isSuccessful()) {
                    successProgress();
                    localDB.SaveCarOffline(response.body().getCar_offlineModels());
                    if(response.body().getCar_offlineModels()!=null) {
                        if (response.body().getCar_offlineModels().size() > 0) {
                            Intent returnIntent = new Intent();
                            setResult(Activity.RESULT_CANCELED, returnIntent);
                            finish();
                        } else {
                            Toast.makeText(getApplicationContext(), "در این بازه اطلاعاتی وجود ندارد!", Toast.LENGTH_LONG).show();
                        }
                    }else {
                        Toast.makeText(getApplicationContext(), "در این بازه اطلاعاتی وجود ندارد!", Toast.LENGTH_LONG).show();
                    }

                }
            }

            @Override
            public void onFailure(Call<offline> call, Throwable t) {
                errorProgress();
                Toast.makeText(ActSelectDate.this, getText(R.string.error_fail_server).toString(), Toast.LENGTH_LONG).show();
                t.printStackTrace();
            }
        });
    }

    //------------------------------------------------------- create json
    private String CreateJson() {
        JSONObject jsonObj = new JSONObject();
        try {
            jsonObj.put("id", id);
            jsonObj.put("df", startdate + " " + starttime);
            jsonObj.put("dt", enddate + " " + endtime);
            jsonObj.put("travel", travel);
        } catch (JSONException e) {
            e.printStackTrace();
        }
        return jsonObj.toString();
    }

    //--------------------------------------------------------todateClick-----------------------
    @OnClick(R.id.end_date)
    public void todateClick(View view) {
        if (showdate!=0){
            PersianCalendar persianCalendar = new PersianCalendar();
            DatePickerDialog datePickerDialog = DatePickerDialog.newInstance(
                    to_dateListener,
                    persianCalendar.getPersianYear(),
                    persianCalendar.getPersianMonth(),
                    persianCalendar.getPersianDay()
            );
            datePickerDialog.show(getFragmentManager(), "Datepickerdialog");
        }else {
            //----------------------------------------------------------
            PersianCalendar now = new PersianCalendar();
            TimePickerDialog tpd = TimePickerDialog.newInstance(
                    toTimeSetListener,
                    now.get(PersianCalendar.HOUR_OF_DAY),
                    now.get(PersianCalendar.MINUTE),
                    false);
            tpd.show(getFragmentManager(), "TIMEENDPICKER");
        }

    }

    //-------------------------------------------- fromdate button
    @OnClick(R.id.start_date)
    public void fromClick(View view) {
        if (showdate!=0){
            PersianCalendar persianCalendar = new PersianCalendar();
            DatePickerDialog datePickerDialog = DatePickerDialog.newInstance(
                    from_dateListener,
                    persianCalendar.getPersianYear(),
                    persianCalendar.getPersianMonth(),
                    persianCalendar.getPersianDay()
            );
            datePickerDialog.show(getFragmentManager(), "Datepickerdialog");
        }else {
            //----------------------------------------------------------
            PersianCalendar now = new PersianCalendar();
            TimePickerDialog tpd = TimePickerDialog.newInstance(
                    fromTimeSetListener,
                    now.get(PersianCalendar.HOUR_OF_DAY),
                    now.get(PersianCalendar.MINUTE),
                    false);
            tpd.show(getFragmentManager(), "TIMESRAERPICKER");
        }

    }

    //---------progress---------------
    private void displayProgress(String text) {
        try {
            if (progressDialog == null) {
                progressDialog = new ProgressDialog(this);
                progressDialog.getWindow().addFlags(Window.FEATURE_NO_TITLE);
                progressDialog.setCancelable(false);
                progressDialog.setMessage(text);
            }
            if (!progressDialog.isShowing()) {
                try {
                    progressDialog.show();
                } catch (Exception e) {
                    e.printStackTrace();
                }
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    //-----------------------------------------successProgress------------
    private void successProgress() {
        if (progressDialog != null && progressDialog.isShowing()) {
            progressDialog.dismiss();
        }
    }

    //-----------------------------------------errorProgress-----------
    private void errorProgress() {
        if (progressDialog != null && progressDialog.isShowing()) {
            progressDialog.dismiss();
        }

    }

    //----------------------------------------------- linear_select_date_time
    @OnClick(R.id.linear_select_date_time)
    public void Select_Renge(View view) {
        DisplayPopUpwindow(view);
    }

    //----------------------------------------------- display popup select renge date
    private void DisplayPopUpwindow(View anchorview) {
        PopupWindow popupWindow = new PopupWindow(context);
        LayoutInflater layoutInflater = (LayoutInflater) getSystemService(Context.LAYOUT_INFLATER_SERVICE);
        View layout = layoutInflater.inflate(R.layout.dialog_selectdatetime,null);
        popupWindow.setContentView(layout);
        // Set content width and heightfa
        popupWindow.setHeight(WindowManager.LayoutParams.WRAP_CONTENT);
        popupWindow.setWidth(WindowManager.LayoutParams.WRAP_CONTENT);
        // Closes the popup window when touch outside of it - when looses focus
        //  popup.setOutsideTouchable(true);
        popupWindow.setFocusable(true);
        popupWindow.setBackgroundDrawable(new BitmapDrawable());
        // Show anchored to button
        Display display = getWindowManager().getDefaultDisplay();
        popupWindow.showAtLocation(anchorview, Gravity.BOTTOM, 0,
                display.getHeight() - anchorview.getBottom() + 300);
        popupWindow.showAsDropDown(anchorview);
        listdate = new ArrayList<>();
        listdate.add(getString(R.string.txt_today));
        listdate.add(getString(R.string.txt_Current_trip));
        listdate.add(getString(R.string.txt_Last_trip));
        listdate.add(getString(R.string.txt_yesterday));
        listdate.add(getString(R.string.txt_two_days_ago));
        listdate.add(getString(R.string.txt_three_days_ago));
        listdate.add(getString(R.string.txt_selectdatetime));
        localDB.InsertRengeDate(listdate);
        RecyclerView recyclerView = layout.findViewById(R.id.select_datetime);

       //--------------------------------------------
        stringArrayAdapter = new RealmShowListRengeDate(localDB.GetRengeDate(), true, context,
                new RealmShowListRengeDate.EventHandler() {
                    @Override
                    public void OnItemClick(int id, String title) {
                        String farsistartdate="";
                        String farsienddate="";
                        if (id==6){
                            start_date.setVisibility(View.VISIBLE);
                            end_date.setVisibility(View.VISIBLE);
                            travel=0;
                            showdate=1;

                            Calendar cal = Calendar.getInstance();
                            startdate = Jalali.getEnglishparseDate(cal.getTime());
                            enddate =startdate;
                            farsistartdate = Jalali.getFarsiDate(cal.getTime());
                            farsienddate = farsistartdate;
                        }
                        if (id == 1) {
                            travel = 1;
                            start_date.setVisibility(View.GONE);
                            end_date.setVisibility(View.GONE);
                        } else if (id == 2) {
                            travel = 2;
                            start_date.setVisibility(View.GONE);
                            end_date.setVisibility(View.GONE);
                        } else if (id == 0) {
                            start_date.setVisibility(View.VISIBLE);
                            end_date.setVisibility(View.VISIBLE);
                            travel = 0;
                            showdate=0;
                            Calendar cal = Calendar.getInstance();
                            startdate = Jalali.getEnglishparseDate(cal.getTime());
                            enddate = startdate;
                            farsistartdate=Jalali.getFarsiDate(cal.getTime());
                            farsienddate=farsistartdate;

                        } else if (id == 3) {
                            start_date.setVisibility(View.VISIBLE);
                            end_date.setVisibility(View.VISIBLE);
                            travel = 0;
                            showdate=0;
                            //----------------------------------------------
                            Calendar cal = Calendar.getInstance();
                            cal.add(Calendar.DATE, -1);
                            startdate = Jalali.getEnglishparseDate(cal.getTime());
                            enddate = startdate;
                            farsistartdate=Jalali.getFarsiDate(cal.getTime());
                            farsienddate=farsistartdate;
                        } else if (id == 4) {
                            start_date.setVisibility(View.VISIBLE);
                            end_date.setVisibility(View.VISIBLE);
                            travel = 0;
                            showdate=0;
                            //---------------------------------------------
                            Calendar cal = Calendar.getInstance();
                            cal.add(Calendar.DATE, -2);
                            startdate = Jalali.getEnglishparseDate(cal.getTime());
                            enddate = startdate;
                            farsistartdate=Jalali.getFarsiDate(cal.getTime());
                            farsienddate=farsistartdate;
                        } else if (id == 5) {
                            start_date.setVisibility(View.VISIBLE);
                            end_date.setVisibility(View.VISIBLE);
                            travel = 0;
                            //---------------------------------------------
                            Calendar cal = Calendar.getInstance();
                            cal.add(Calendar.DATE, -3);
                            startdate = Jalali.getEnglishparseDate(cal.getTime());
                            enddate =startdate;
                            farsistartdate=Jalali.getFarsiDate(cal.getTime());
                            farsienddate=farsistartdate;
                        }
                        txt_select_date.setText(title);
                        popupWindow.dismiss();
                        date_start.setText(farsistartdate);
                        date_end.setText(farsienddate);
                    }
                });
        recyclerView.setLayoutManager(new LinearLayoutManager(context));
        recyclerView.setAdapter(stringArrayAdapter);


    }
}
