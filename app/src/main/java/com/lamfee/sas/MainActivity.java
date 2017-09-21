package com.lamfee.sas;
import android.Manifest;
import android.annotation.SuppressLint;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.net.Uri;
import android.os.Build;
import android.provider.Settings;
import android.support.annotation.NonNull;
import android.support.annotation.RequiresApi;
import android.support.design.widget.Snackbar;
import android.support.design.widget.TabLayout;
import android.support.v4.app.ActivityCompat;
import android.support.v4.content.ContextCompat;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.support.v4.app.FragmentPagerAdapter;
import android.support.v4.view.ViewPager;
import android.os.Bundle;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.Toast;

import com.google.android.gms.ads.AdRequest;
import com.google.android.gms.ads.AdView;
import com.google.android.gms.ads.MobileAds;


public class MainActivity extends AppCompatActivity {
    private static final int PERMISSION_REQUEST_CODE =1;
    private static final int REQUEST_ID_MULTIPLE_PERMISSIONS = 1;
    // private static final String TAG = "MainActivity";
    private AdView mAdView;
    private  View view;

    /**
     * The {@link android.support.v4.view.PagerAdapter} that will provide
     * fragments for each of the sections. We use a
     * {@link FragmentPagerAdapter} derivative, which will keep every
     * loaded fragment in memory. If this becomes too memory intensive, it
     * may be best to switch to a
     * {@link android.support.v4.app.FragmentStatePagerAdapter}.
     */
    //private SectionsPagerAdapter mSectionsPagerAdapter;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        // Here, thisActivity is the current activity
        view =findViewById(android.R.id.content);
      //  ActivityCompat.requestPermissions(this,new String[]{Manifest.permission.SEND_SMS},1);
    //    ActivityCompat.requestPermissions(this,new String[]{Manifest.permission.ACCESS_FINE_LOCATION},1);



      /* if (checkPermission(Manifest.permission.ACCESS_FINE_LOCATION) && checkPermission(Manifest.permission.SEND_SMS)) {
           // Snackbar.make(view,"Permission already granted.",Snackbar.LENGTH_LONG).show();
            Toast.makeText(this,"Permission is granted.", Toast.LENGTH_LONG).show();

        }
        if (!checkPermission(Manifest.permission.ACCESS_FINE_LOCATION) && checkPermission(Manifest.permission.SEND_SMS)) {
            requestPermission(Manifest.permission.ACCESS_FINE_LOCATION);
        }
        if (checkPermission(Manifest.permission.ACCESS_FINE_LOCATION) && !checkPermission(Manifest.permission.SEND_SMS)) {
            requestPermission(Manifest.permission.SEND_SMS);
        }*/



        Log.d("TAG", "onCreate Starting");
        //SectionsPagerAdapter sectionsPagerAdapter = new SectionsPagerAdapter(getSupportFragmentManager());
        /*
      The {@link ViewPager} that will host the section contents.
     */
        ViewPager mViewPager = (ViewPager) findViewById(R.id.container);
        setupViewPager(mViewPager);
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

        if(!checkPermission(Manifest.permission.ACCESS_FINE_LOCATION) && !checkPermission(Manifest.permission.SEND_SMS)) {
            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
                requestPermission(null);
            } else {
                requestPermission(Manifest.permission.ACCESS_FINE_LOCATION);
                requestPermission(Manifest.permission.SEND_SMS);
            }
        } else
       if (!checkPermission(Manifest.permission.ACCESS_FINE_LOCATION) && checkPermission(Manifest.permission.SEND_SMS)) {
            requestPermission(null);
        }
        if (checkPermission(Manifest.permission.ACCESS_FINE_LOCATION) && !checkPermission(Manifest.permission.SEND_SMS)) {
            requestPermission(null);
        }



        MobileAds.initialize(this, "ca-app-pub-3940256099942544~3347511713");

        //toolbar.setTitle("Hello There!");
        //for red colored toolbar title
        // Create the adapter that will return a fragment for each of the three
        // primary sections of the activity.
       // mSectionsPagerAdapter = new SectionsPagerAdapter(getSupportFragmentManager());

        // Set up the ViewPager with the sections adapter.
       // mViewPager = (ViewPager) findViewById(R.id.container);
      //  mViewPager.setAdapter(mSectionsPagerAdapter);*/

        TabLayout tabLayout = (TabLayout) findViewById(R.id.tabs);
        tabLayout.setupWithViewPager(mViewPager);
       //this
        //AppBarLayout appBarLayout = (AppBarLayout) findViewById(R.id.appbar);
       // appBarLayout.setBackgroundColor(Color.RED);




        @SuppressLint("HardwareIds") String android_id = Settings.Secure.getString(getContentResolver(),
                Settings.Secure.ANDROID_ID);
         mAdView = (AdView) findViewById(R.id.adView);

        AdRequest request = new AdRequest.Builder()
                .addTestDevice(AdRequest.DEVICE_ID_EMULATOR)        // All emulators
                .addTestDevice(android_id)  // My Galaxy Nexus test phone
                .build();


        mAdView.loadAd(request );



    }

    @Override
    protected void onPause() {
        if (mAdView != null) {
            mAdView.pause();
        }

        super.onPause();
    }

    @Override
    protected void onResume() {
        super.onResume();
        if (mAdView != null) {
            mAdView.resume();
        }
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        if (mAdView != null) {
            mAdView.destroy();
        }
    }

    public void setupViewPager(ViewPager viewPager) {
        SectionsPagerAdapter adapter = new SectionsPagerAdapter(getSupportFragmentManager());
        adapter.addFragment(new Tab1Fragment(),"STATUS");
        adapter.addFragment(new Tab2Fragment(),"PREFERENCES");
        adapter.addFragment(new Tab3Fragment(),"MORE");
        viewPager.setAdapter(adapter);
    }


   @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.menu_main, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
        int id = item.getItemId();

        //noinspection SimplifiableIfStatement
        if (id == R.id.action_settings) {
            Intent intent = new Intent (this, SettingsActivity.class);
            startActivity(intent);


            return true;

        }

        return super.onOptionsItemSelected(item);
    }

    private boolean checkPermission(String permission){
        int result = ContextCompat.checkSelfPermission(getApplicationContext(), permission);
        return result == PackageManager.PERMISSION_GRANTED;

    }
    private void requestPermission(String permission){
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
                requestPermissions(new String[]{
                                Manifest.permission.SEND_SMS,
                                Manifest.permission.ACCESS_FINE_LOCATION},
                    REQUEST_ID_MULTIPLE_PERMISSIONS);
        } else {
            if
                    (ActivityCompat.shouldShowRequestPermissionRationale(this,permission)){

            Toast.makeText(getApplicationContext(),"GPS/SMS permission allows us to access location data. Please allow in App Settings for the app to work.",Toast.LENGTH_LONG).show();

        } else {
            Toast.makeText(getApplicationContext(),"fuck", Toast.LENGTH_LONG).show();
            ActivityCompat.requestPermissions(this,new String[]{permission},PERMISSION_REQUEST_CODE);
        }
        }
    }


    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull final String permissions[], @NonNull int[] grantResults) {
        switch (requestCode) {
            case REQUEST_ID_MULTIPLE_PERMISSIONS:
                if (grantResults.length > 0 && grantResults[1] == PackageManager.PERMISSION_GRANTED) {
                    Snackbar.make(view,"Permission Granted",Snackbar.LENGTH_LONG).show();

                } else {

                   permissionCheck();
                  //  Snackbar.make(view,"Please allow Locations and SMS permissions otherwise the app won't work",Snackbar.LENGTH_LONG).show();



                }
                break;
        }
    }

    private void permissionCheck(){
        final AlertDialog.Builder builder = new AlertDialog.Builder(this);
        builder.setMessage("This app requires Locations and SMS permissions to work, please enable them to continue otherwise this App will stop working.")
                .setCancelable(false)
                .setPositiveButton("Yes", new DialogInterface.OnClickListener() {
                    public void onClick(@SuppressWarnings("unused") final DialogInterface dialog, @SuppressWarnings("unused") final int id) {
                        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {

                            if (shouldShowRequestPermissionRationale(Manifest.permission.ACCESS_FINE_LOCATION) && shouldShowRequestPermissionRationale(Manifest.permission.SEND_SMS)) {
                                requestPermission(null);
                            } else {
                                Intent intent = new Intent();
                                intent.setAction(Settings.ACTION_APPLICATION_DETAILS_SETTINGS);
                                Uri uri = Uri.fromParts("package", getPackageName(), null);
                                intent.setData(uri);
                                startActivity(intent);
                                finishAffinity();
                            }
                        }}
                })
                .setNegativeButton("No", new DialogInterface.OnClickListener() {
                    @RequiresApi(api = Build.VERSION_CODES.JELLY_BEAN)
                    public void onClick(final DialogInterface dialog, @SuppressWarnings("unused") final int id) {
                        finishAffinity();
                    }
                });
        final AlertDialog alert = builder.create();
        alert.show();
    }


    /* public static class PlaceholderFragment extends Fragment {

        private static final String ARG_SECTION_NUMBER = "section_number";

        public PlaceholderFragment() {
        }


        public static PlaceholderFragment newInstance(int sectionNumber) {
            PlaceholderFragment fragment = new PlaceholderFragment();
            Bundle args = new Bundle();
            args.putInt(ARG_SECTION_NUMBER, sectionNumber);
            fragment.setArguments(args);
            return fragment;
        }

        @Override
        public View onCreateView(LayoutInflater inflater, ViewGroup container,
                                 Bundle savedInstanceState) {
            View rootView = inflater.inflate(R.layout.fragment_main, container, false);
            TextView textView = (TextView) rootView.findViewById(R.id.section_label);
            textView.setText(getString(R.string.section_format, getArguments().getInt(ARG_SECTION_NUMBER)));
            return rootView;
        }
    }

    /**
     * A {@link FragmentPagerAdapter} that returns a fragment corresponding to
     * one of the sections/tabs/pages.
     */
    /*public class SectionsPagerAdapter extends FragmentPagerAdapter {

        public SectionsPagerAdapter(FragmentManager fm) {
            super(fm);
        }

        @Override
        public Fragment getItem(int position) {
            // getItem is called to instantiate the fragment for the given page.
            // Return a PlaceholderFragment (defined as a static inner class below).
            return PlaceholderFragment.newInstance(position + 1);
        }

        @Override
        public int getCount() {
            // Show 3 total pages.
            return 3;
        }

        @Override
        public CharSequence getPageTitle(int position) {
            switch (position) {
                case 0:
                    return "SECTION 1";
                case 1:
                    return "SECTION 2";
                case 2:
                    return "SECTION 3";
            }
            return null;
        }

   }*/
}


