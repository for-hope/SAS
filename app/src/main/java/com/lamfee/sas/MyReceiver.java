package com.lamfee.sas;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.widget.Toast;

public class MyReceiver extends BroadcastReceiver {

    @Override
    public void onReceive(Context context, Intent intent)
    {
        Toast.makeText(context,"An SMS has been sent",Toast.LENGTH_LONG).show();
        SafetyMode.unSafeMode(context,Tab1Fragment.googlemapslink);

    }
}
