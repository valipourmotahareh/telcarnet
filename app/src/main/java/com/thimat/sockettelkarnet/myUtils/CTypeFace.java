package com.thimat.sockettelkarnet.myUtils;

import android.app.ProgressDialog;
import android.content.Context;
import android.content.res.Configuration;
import android.graphics.Paint;
import android.graphics.Typeface;
import android.util.Log;
import android.view.Display;
import android.view.View;
import android.view.ViewGroup;
import android.view.WindowManager;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;



public class CTypeFace {
    public static final String regularExpression = "/\\w*((\\%27)|(\\'))((\\%6F)|o|(\\%4F))((\\%72)|r|(\\%52))/ix";
    // ---------SyncTime---------------
    public static final int SYNC_DATA_TIME_IN_MIN = 60;
    public static final int SYNC_POSITION_TIME_IN_MIN = 5;
    // ---------For Msg Type---------------
    public static final int MSG_TYPE_RECEIVED = 0;
    public static final int MSG_TYPE_SEND = 1;
    // ------------------------
    public static final int DISPLAY_ORIENTATION_LANDSCAPE = 1;
    public static final int DISPLAY_ORIENTATION_PORTRAIT = 2;
    private static final String TAG = "---CTypeFace---";
    //    private static String fonts = "IRAN Sans Light.ttf";
    private static final String fonts = "BYEKAN.TTF";
    private static Typeface face = null;
    private static final Typeface faceEn = null;
    private static ProgressDialog pDialog;

    public static void setTypeFace(Context context, ViewGroup parent) {
        initialized(context);
        for (int i = 0; i < parent.getChildCount(); i++) {
            View v = parent.getChildAt(i);
            if (v instanceof ViewGroup) {
                setTypeFace(context, (ViewGroup) v);
            } else if (v instanceof TextView) {
                TextView tv = (TextView) v;
                tv.setTypeface(face);
                //For making the font anti-aliased.
                tv.setPaintFlags(tv.getPaintFlags() | Paint.SUBPIXEL_TEXT_FLAG);
            }
        }
    }

    public static int detectOrientation(Context contex) {
        Display display = ((WindowManager) contex
                .getSystemService(Context.WINDOW_SERVICE)).getDefaultDisplay();
        int orientation = display.getOrientation();
        switch (orientation) {
            case Configuration.ORIENTATION_LANDSCAPE:
                orientation = DISPLAY_ORIENTATION_LANDSCAPE;
                break;
            case Configuration.ORIENTATION_PORTRAIT:
                orientation = DISPLAY_ORIENTATION_PORTRAIT;
                break;
        }
        return orientation;
    }

    public static void showProgress(Context contex) {
        pDialog = new ProgressDialog(contex);
        pDialog.setMessage(processOBJ("بارگزاری اطلاعات"));
        pDialog.setIndeterminate(false);
        pDialog.setCancelable(false);
        pDialog.show();
    }

    public static void closeProgress() {
        pDialog.dismiss();
    }

    public static void initialized(Context contex) {
        // Init Fonts
        face = Typeface.createFromAsset(contex.getAssets(), "font/" + fonts
                + "");
    }

    public static void processOBJ(TextView object) {
        object.setTypeface(face);
        // object.setText(Reshape.getString(object.getText().toString()));
//        object.setText(object.getText().toString());
    }

    public static String getCurrentTimeMillis() {
        return String.valueOf(System.currentTimeMillis() / 1000L);
    }

    public static void processOBJ(TextView object, Context contex) {
        initialized(contex);
        object.setTypeface(face);
//        object.setText(object.getText().toString());
//		object.setText(Reshape.getString(object.getText().toString()));
    }

    public static void processOBJ(EditText object, Context contex) {
        initialized(contex);
        object.setTypeface(face);
//        object.setHint(object.getHint().toString());
//        object.setHint(                Reshape.getString(object.getHint().toString()));
    }

    public static void processEditTextOBJ(EditText object, Context contex) {
        initialized(contex);
        object.setTypeface(face);
//        object.setText(Reshape.getString(object.getText().toString()));
//        object.setText(object.getText().toString());
    }

    public static void processOBJ(Button object, Context contex) {
        initialized(contex);
        object.setTypeface(face);
//        object.setText(Reshape.getString(object.getText().toString()));
//        object.setText(object.getText().toString());
    }

    public static void processOBJ(Button object) {
//        initialized(contex);
        object.setTypeface(face);
//        object.setText(Reshape.getString(object.getText().toString()));
//        object.setText(object.getText().toString());
    }


    // ------------------------------------
    public static void toastOBJ(String object, Context contex) {
        // initialized(contex);
//        Toast.makeText(contex, Reshape.getString(object.toString()),
//        Toast.LENGTH_SHORT).show();
        Toast.makeText(contex, object, Toast.LENGTH_SHORT).show();
    }

    public static void logOBJ(String tag, String object) {
        // initialized(contex);
        Log.e(tag, object);
    }

    public static String processOBJ(String object) {
        // initialized(contex);
//        return Reshape.getString(object.toString());
        return object;
    }

}
