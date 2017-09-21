package com.lamfee.sas;

import android.Manifest;
import android.annotation.SuppressLint;
import android.app.Dialog;
import android.app.NotificationManager;
import android.app.PendingIntent;
import android.appwidget.AppWidgetManager;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.graphics.Color;
import android.location.Criteria;
import android.location.Location;
import android.location.LocationListener;
import android.location.LocationManager;
import android.os.Build;
import android.os.Bundle;
import android.os.CountDownTimer;
import android.os.Vibrator;
import android.support.annotation.Nullable;
import android.support.design.widget.AppBarLayout;
import android.support.design.widget.TabLayout;
import android.support.v4.app.ActivityCompat;
import android.support.v4.app.Fragment;
import android.support.v4.app.NotificationCompat;
import android.support.v4.content.ContextCompat;
import android.support.v7.app.AlertDialog;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.Window;
import android.view.WindowManager;
import android.widget.FrameLayout;
import android.widget.TextView;
import android.widget.Toast;


import com.github.ybq.android.spinkit.SpinKitView;

import at.markushi.ui.CircleButton;

import static android.content.Context.NOTIFICATION_SERVICE;


public class Tab1Fragment extends Fragment implements LocationListener {
    public static final String TAG = "Tab1Fragment";
    private FrameLayout frameLayout;
    private CircleButton circleButton;
    private CircleButton safeButton;
    private TextView textView;
    // public   static  boolean isSafe=true;
    private Toolbar toolbar;
    private AppBarLayout appBarLayout;
    private TabLayout tabLayout;
    private SpinKitView redCircle;
    private SpinKitView greenCircle;
    private Vibrator vibe;
    LocationManager locationManager;
    String provider;
    static String googlemapslink;
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        final View view = inflater.inflate(R.layout.fragment_tab1, container, false);
        // final View viewList = inflater.inflate(R.layout.costume_list,container,false);
        frameLayout = (FrameLayout) view.findViewById(R.id.frame_layout);
        circleButton = (CircleButton) view.findViewById(R.id.circleButton);
        safeButton = (CircleButton) view.findViewById(R.id.safeButton);
        textView = (TextView) view.findViewById(R.id.textView);
        toolbar = (Toolbar) getActivity().findViewById(R.id.toolbar);
        appBarLayout = (AppBarLayout) getActivity().findViewById(R.id.appbar);
        tabLayout = (TabLayout) getActivity().findViewById(R.id.tabs);
        redCircle = (SpinKitView) view.findViewById(R.id.redCircle);
        greenCircle = (SpinKitView) view.findViewById(R.id.spin_kit);
        vibe = (Vibrator) getActivity().getSystemService(Context.VIBRATOR_SERVICE);
        locationManager = (LocationManager) getContext().getSystemService(Context.LOCATION_SERVICE);
        provider = locationManager.getBestProvider(new Criteria(), false);



        if (SafetyMode.isSafe) {
            safeStatue();
            //SafetyMode.isSafe=true;
            Log.d("TAG", "SAFE");

        } else {
            unSaveStatue();
          //  SafetyMode.isSafe = false;
            //SafetyMode.unSafeMode(getContext());
            Log.d("TAG", "not SAFE");
        }
        view.findViewById(R.id.circleButton).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                SafetyMode.isSafe = true;
                safetyStart();
                safeStatue();
                vibe.vibrate(200);



            }
        });

        view.findViewById(R.id.safeButton).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                //  Toast.makeText(getActivity(), "Button clicked", Toast.LENGTH_SHORT).show();

                if (!locationManager.isProviderEnabled(LocationManager.GPS_PROVIDER)) {
                    final AlertDialog.Builder builder = new AlertDialog.Builder(getContext());
                    builder.setMessage("Your GPS seems to be disabled, do you want to enable it?")
                            .setCancelable(false)
                            .setPositiveButton("Yes", new DialogInterface.OnClickListener() {
                                public void onClick(@SuppressWarnings("unused") final DialogInterface dialog, @SuppressWarnings("unused") final int id) {
                                    startActivity(new Intent(android.provider.Settings.ACTION_LOCATION_SOURCE_SETTINGS));
                                }
                            })
                            .setNegativeButton("No", new DialogInterface.OnClickListener() {
                                public void onClick(final DialogInterface dialog, @SuppressWarnings("unused") final int id) {
                                    dialog.cancel();
                                }
                            });
                    final AlertDialog alert = builder.create();
                    alert.show();
                }
                 else {
                    long[] pattern = {0, 100, 1000};
                    vibe.vibrate(pattern,0);

                final Dialog dialog = new Dialog(getActivity());
                dialog.setTitle("Confirmation");
                dialog.setContentView(R.layout.dialog_confirmation);
                dialog.setCanceledOnTouchOutside(false);
                dialog.show();
                final TextView label = (TextView) dialog.findViewById(R.id.Labeltext);


                //CountDown

                new CountDownTimer(30000, 1000) {

                    public void onTick(long millisUntilFinished) {
                        label.setText(String.format(getString(R.string.seconds_remaining), millisUntilFinished / 1000));
                    }

                    public void onFinish() {
                        if (dialog.isShowing()) {
                            label.setText(R.string.done);
                            unSaveStatue();
                            SafetyMode.isSafe = false;
                            safetyStart();
                            dialog.cancel();
                        }
                    }
                }.start();


                TextView cancelButton = (TextView) dialog.findViewById(R.id.cancelButton);

                cancelButton.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View view) {
                        //user cancels
                        dialog.cancel();
                        vibe.cancel();
                    }
                });

                TextView okButton = (TextView) dialog.findViewById(R.id.okButton);
                okButton.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View view) {

                      // starting the service
                        unSaveStatue();
                        SafetyMode.isSafe = false;
                        safetyStart();
                        //vibe.vibrate(300);
                        dialog.cancel();

                        //code when the user accepts / change to unSave


                    }
                }); }


            }
        });
        return view;


    }
    private void startNotification() {
        Log.i("NextActivity", "startNotification");

        // Sets an ID for the notification
        int mNotificationId = 1;

        // Build Notification , setOngoing keeps the notification always in status bar
        NotificationCompat.Builder mBuilder;
        mBuilder = new NotificationCompat.Builder(getContext())
                .setSmallIcon(R.drawable.ic_action1_tick)
                .setContentTitle(getString(R.string.notif_title))
                .setContentText(getString(R.string.notif_desc))
                .setOngoing(true)
                .setColor(ContextCompat.getColor(getContext(),R.color.BtnNotSafe));


        // Create pending intent, mention the Activity which needs to be
        //triggered when user clicks on notification(StopScript.class in this case)

        PendingIntent contentIntent = PendingIntent.getActivity(getContext(), 0,
                new Intent(getContext(), MainActivity.class), PendingIntent.FLAG_UPDATE_CURRENT);


        mBuilder.setContentIntent(contentIntent);


        // Gets an instance of the NotificationManager service
        NotificationManager mNotifyMgr =
                (NotificationManager) getContext().getSystemService(NOTIFICATION_SERVICE);

        // Builds the notification and issues it.
        mNotifyMgr.notify(mNotificationId, mBuilder.build());


    }
private void updateWidget(){
    Intent intent = new Intent(getContext(),MyWidgetProvider.class);
    intent.setAction(AppWidgetManager.ACTION_APPWIDGET_UPDATE);
// Use an array and EXTRA_APPWIDGET_IDS instead of AppWidgetManager.EXTRA_APPWIDGET_ID,
// since it seems the onUpdate() is only fired on that:
    int[] ids = {0};
    intent.putExtra(AppWidgetManager.EXTRA_APPWIDGET_IDS,ids);
    getContext().sendBroadcast(intent);
}
    private void unSaveStatue() {

        // Vibrate for 500 milliseconds

updateWidget();
        startNotification();
        if (ActivityCompat.checkSelfPermission(getContext(), Manifest.permission.ACCESS_FINE_LOCATION) != PackageManager.PERMISSION_GRANTED && ActivityCompat.checkSelfPermission(getContext(), Manifest.permission.ACCESS_COARSE_LOCATION) != PackageManager.PERMISSION_GRANTED) {
            ActivityCompat.requestPermissions(getActivity(),new String[]{Manifest.permission.ACCESS_FINE_LOCATION},1);
        } else {
        Location location = locationManager.getLastKnownLocation(provider);
        if (location != null) {
            Log.i("Location Info", "LOCATION ACHIEVED");
            double lat = location.getLatitude();
            double lon = location.getLongitude();
            Log.i("Latitude",Double.toString(lat));
            Log.i("Longitude",Double.toString(lon));
            @SuppressLint("DefaultLocale") String latString = String.format("%.5f", lat);
            @SuppressLint("DefaultLocale") String lonString = String.format("%.5f", lon);
            googlemapslink = "google.com/maps/search/"+latString+","+lonString;
        } else {
            Log.i("Location Info", "NO LOCATION");
            googlemapslink = "GPS is not working, try calling sender!";
        }
        }
        frameLayout.setBackgroundColor(ContextCompat.getColor(getContext(),R.color.NotSafe));
        safeButton.setVisibility(View.GONE);
        circleButton.setVisibility(View.VISIBLE);
        textView.setBackgroundColor(ContextCompat.getColor(getContext(),R.color.BtnNotSafe));
        textView.setText(R.string.unsafe);
        getActivity().setTitle(getString(R.string.warningtitle));
        toolbar.setBackgroundColor(ContextCompat.getColor(getContext(),R.color.colorPrimaryN));
        appBarLayout.setBackgroundColor(ContextCompat.getColor(getContext(),R.color.colorPrimaryN));
        tabLayout.setBackgroundColor(ContextCompat.getColor(getContext(),R.color.colorPrimaryN));
        tabLayout.setTabTextColors(Color.WHITE, ContextCompat.getColor(getContext(),R.color.colorAccentN));
        tabLayout.setSelectedTabIndicatorColor(ContextCompat.getColor(getContext(),R.color.colorAccentN));
        greenCircle.setVisibility(View.GONE);
        redCircle.setVisibility(View.VISIBLE);

        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {
            Window window = getActivity().getWindow();
            window.addFlags(WindowManager.LayoutParams.FLAG_DRAWS_SYSTEM_BAR_BACKGROUNDS);
            window.setStatusBarColor(ContextCompat.getColor(getContext(),R.color.colorPrimaryDarkN));
            window.setNavigationBarColor(ContextCompat.getColor(getContext(),R.color.colorPrimaryDarkN));

        }

    }

    private void safeStatue() {
        vibe.cancel();
        updateWidget();
        NotificationManager notificationManager = (NotificationManager) getContext().getSystemService(Context.NOTIFICATION_SERVICE);
        notificationManager.cancel(1);
        frameLayout.setBackgroundColor(Color.parseColor("#9ad100"));
        safeButton.setVisibility(View.VISIBLE);
        circleButton.setVisibility(View.GONE);
        textView.setText(R.string.safe);
        textView.setBackgroundColor(ContextCompat.getColor(getContext(),R.color.colorPrimaryDark));
        toolbar.setBackgroundColor(ContextCompat.getColor(getContext(),R.color.colorPrimary));
        appBarLayout.setBackgroundColor(ContextCompat.getColor(getContext(),R.color.colorPrimary));
        tabLayout.setBackgroundColor(ContextCompat.getColor(getContext(),R.color.colorPrimary));
        tabLayout.setTabTextColors(Color.WHITE, ContextCompat.getColor(getContext(),R.color.colorAccent));
        tabLayout.setSelectedTabIndicatorColor(ContextCompat.getColor(getContext(),R.color.colorAccent));
        greenCircle.setVisibility(View.VISIBLE);
        redCircle.setVisibility(View.GONE);

        getActivity().setTitle(getString(R.string.safe_stat));

        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {
            Window window = getActivity().getWindow();
            window.addFlags(WindowManager.LayoutParams.FLAG_DRAWS_SYSTEM_BAR_BACKGROUNDS);
            window.setStatusBarColor(ContextCompat.getColor(getContext(),R.color.colorPrimaryDark));
            window.setNavigationBarColor(ContextCompat.getColor(getContext(),R.color.colorPrimaryDark));

        }
    }

    private void safetyStart() {

        if (!SafetyMode.isSafe) {
            Intent intent = new Intent(getContext(), SafetyMode.class);
            getActivity().startService(intent);

        } else {
            Intent intent = new Intent(getContext(), SafetyMode.class);
            getActivity().stopService(intent);
        }
    }

    @Override
    public void onResume() {
        super.onResume();
      if(ActivityCompat.checkSelfPermission(getContext(), Manifest.permission.SEND_SMS) == PackageManager.PERMISSION_GRANTED && ActivityCompat.checkSelfPermission(getContext(), Manifest.permission.ACCESS_FINE_LOCATION) == PackageManager.PERMISSION_GRANTED ) {
          if (ActivityCompat.checkSelfPermission(getContext(), Manifest.permission.ACCESS_FINE_LOCATION) != PackageManager.PERMISSION_GRANTED && ActivityCompat.checkSelfPermission(getContext(), Manifest.permission.ACCESS_COARSE_LOCATION) != PackageManager.PERMISSION_GRANTED) {
            ActivityCompat.requestPermissions(getActivity(), new String[]{Manifest.permission.ACCESS_FINE_LOCATION}, 1);
        } else {
              try {
                  locationManager.requestLocationUpdates(provider, 400, 1,this);
              }catch (Exception e){
                  Log.i("error","cant access motherfucking locations");
              }


        }
    } }


    @Override
    public void onLocationChanged(Location location) {
        double lat = location.getLatitude();
        double lon = location.getLongitude();
        Log.i("Latitude",Double.toString(lat));
        Log.i("Longitude",Double.toString(lon));
       // Convert into String
        // Hello World Comment
        @SuppressLint("DefaultLocale") String latString = String.format("%.5f", lat);
        @SuppressLint("DefaultLocale") String lonString = String.format("%.5f", lon);
        googlemapslink = "google.com/maps/search/"+latString+","+lonString;
        Log.i("GOOGLE",googlemapslink);
    }


    @Override
    public void onStatusChanged(String provider, int status, Bundle extras) {

    }

    @Override
    public void onProviderEnabled(String provider) {

    }

    @Override
    public void onProviderDisabled(String provider) {

    }

    @Override
    public void onPause() {
        if(SafetyMode.isSafe) {
            locationManager.removeUpdates(this);
        }

        super.onPause();
    }

    @Override
    public void onDestroy() {
        super.onDestroy();
        if(SafetyMode.isSafe) {
            locationManager.removeUpdates(this);
        }
    }
}
