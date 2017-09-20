package com.lamfee.sas;

import android.app.PendingIntent;
import android.appwidget.AppWidgetManager;
import android.appwidget.AppWidgetProvider;
import android.content.ComponentName;
import android.content.Context;
import android.content.Intent;
import android.widget.RemoteViews;




public class MyWidgetProvider extends AppWidgetProvider {

    private static final String ACTION_CLICK = "ACTION_CLICK";


    @Override
    public void onUpdate(Context context, AppWidgetManager appWidgetManager, int[] appWidgetIds) {

        // Get all ids
        ComponentName thisWidget = new ComponentName(context,
                MyWidgetProvider.class);
        int[] allWidgetIds = appWidgetManager.getAppWidgetIds(thisWidget);
        for (int widgetId : allWidgetIds) {
            // Create some random data
           // int number = (new Random().nextInt(100));

            RemoteViews remoteViews = new RemoteViews(context.getPackageName(),
                    R.layout.widget_layout);
           // Log.w("WidgetExample", String.valueOf(number));
            // Set the text
            if(SafetyMode.isSafe){
                remoteViews.setInt(R.id.widgetlayout, "setBackgroundResource", R.drawable.widget_circle);
                remoteViews.setInt(R.id.widget_tick, "setBackgroundResource", R.drawable.ic_action_tick);
            } else  {
                remoteViews.setInt(R.id.widgetlayout, "setBackgroundResource", R.drawable.widget_circlex);
                remoteViews.setInt(R.id.widget_tick, "setBackgroundResource", R.drawable.ic_action1_tick);
            }

            // Register an onClickListener
            Intent intent = new Intent(context, MainActivity.class);


            intent.putExtra(AppWidgetManager.EXTRA_APPWIDGET_IDS, appWidgetIds);

            PendingIntent pendingIntent = PendingIntent.getActivity(context, 0, intent, 0);
            remoteViews.setOnClickPendingIntent(R.id.widgetlayout, pendingIntent);
            appWidgetManager.updateAppWidget(widgetId, remoteViews);
        }
    }

}