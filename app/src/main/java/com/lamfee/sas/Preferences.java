package com.lamfee.sas;

import android.content.Context;
import android.content.SharedPreferences;
import android.widget.Toast;
class Preferences {
    static void saveInfo(Context context, String key, String value) {
        SharedPreferences sharedPref = context.getSharedPreferences("userPref",Context.MODE_APPEND);
        SharedPreferences.Editor editor = sharedPref.edit();
        editor.putString(key,value);
        editor.apply();
        Toast.makeText(context,"Saved",Toast.LENGTH_LONG).show();
    }
    static void saveInfo(Context context, String key, int value) {
        SharedPreferences sharedPref = context.getSharedPreferences("userPref",Context.MODE_APPEND);
        SharedPreferences.Editor editor = sharedPref.edit();
        editor.putInt(key,value);
        editor.apply();
        Toast.makeText(context,"Saved",Toast.LENGTH_LONG).show();
    }
}
