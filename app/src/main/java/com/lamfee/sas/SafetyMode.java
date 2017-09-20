package com.lamfee.sas;


import android.app.AlarmManager;
import android.app.AlertDialog;
import android.app.PendingIntent;
import android.app.Service;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.IBinder;
import android.support.annotation.Nullable;
import android.telephony.SmsManager;

public class SafetyMode extends Service {
    public static boolean isSafe = true;
    private static int smsPeriod;

    public static void unSafeMode(Context context,String maps) {
        //  Toast.makeText(context,"SafetyCheck",Toast.LENGTH_LONG).show();
        if (!isSafe) {
            SharedPreferences sharedPref = context.getSharedPreferences("userPref", Context.MODE_APPEND);
            String costumeSMS = sharedPref.getString("costumeSMS", "");
            smsPeriod = sharedPref.getInt("timePeriod", 0);
            MySQLiteHelper db = new MySQLiteHelper(context);
            try {
                for (int i = 0; i < db.getAllContacts().size(); i++) {
                    if (costumeSMS.equals("")) {
                        costumeSMS = "Im in trouble i need help here is my location!";
                    }
                    SmsManager.getDefault().sendTextMessage(db.getAllContacts().get(i).getContactNumber(), null,
                            costumeSMS + " " + maps, null, null);
                }

            } catch (Exception e) {
                AlertDialog.Builder alertDialogBuilder = new
                        AlertDialog.Builder(context);
                AlertDialog dialog = alertDialogBuilder.create();
                dialog.setMessage(e.getMessage());
                dialog.show();
            }
        }
    }



    @Override
    public void onCreate() {
        super.onCreate();
        isSafe = true;

   }

    @Override
    public int onStartCommand(Intent intent, int flags, int startId) {
     //   Toast.makeText(getApplicationContext(),"Service Started",Toast.LENGTH_LONG).show();
         isSafe=false;
        AlarmManager alarmManager=(AlarmManager) getSystemService(Context.ALARM_SERVICE);
        Intent someIntent = new Intent(this, MyReceiver.class);
        PendingIntent pendingIntent = PendingIntent.getBroadcast(this, 0, someIntent, 0);
        alarmManager.setRepeating(AlarmManager.RTC_WAKEUP,System.currentTimeMillis(),60000*smsPeriod,
                pendingIntent);
        return START_STICKY;
    }

    @Override
    public void onDestroy() {
      //  Toast.makeText(SafetyMode.this,"Service Stopped",Toast.LENGTH_LONG).show();
        AlarmManager alarmManager=(AlarmManager) getSystemService(Context.ALARM_SERVICE);
        Intent intent = new Intent(this, MyReceiver.class);
        PendingIntent pendingIntent = PendingIntent.getBroadcast(this, 0, intent, 0);
        alarmManager.cancel(pendingIntent);
        isSafe = true;
    }

    @Nullable
    @Override
    public IBinder onBind(Intent intent) {
        return null;
    }



}
