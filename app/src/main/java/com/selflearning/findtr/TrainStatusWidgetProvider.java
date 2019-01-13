package com.selflearning.findtr;

import android.app.PendingIntent;
import android.appwidget.AppWidgetManager;
import android.appwidget.AppWidgetProvider;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.util.Log;
import android.widget.RemoteViews;

import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.Locale;
import java.util.Random;

import static android.os.Build.VERSION_CODES.M;
import static com.selflearning.findtr.R.id.trainNumber;

/**
 * Created by venkat on 05-01-2019.
 */

public class TrainStatusWidgetProvider extends AppWidgetProvider {

    HttpGetRequest getRequest ;
    String myUrl,result,fDate;
    @Override
    public void onUpdate(Context context, AppWidgetManager appWidgetManager, int[] appWidgetIds) {
        SharedPreferences pref = context.getSharedPreferences("MyPref", 0);
        Date cDate = new Date();
        fDate = new SimpleDateFormat("yyyy-MM-dd").format(cDate);
        final int count = appWidgetIds.length;
        try {

            for (int i = 0; i < count; i++) {
                int widgetId = appWidgetIds[i];
                //String number = String.format("%03d", (new Random().nextInt(900) + 100));

                RemoteViews remoteViews = new RemoteViews(context.getPackageName(),
                        R.layout.simple_widget);
                remoteViews.setTextViewText(R.id.textView, "Fetching Data... Please wait..");
                Log.v("pref.getString()",pref.getString("trNumber", ""));
                if (pref.getString("trNumber", "") != "") {
                    myUrl = "https://indianrailapi.com/api/v2/livetrainstatus/apikey/6f446204319af149bd8b07908793cf56/trainnumber/" + pref.getString("trNumber", "") + "/date/"+ fDate.replace("-","")+"/";
                    getRequest = new HttpGetRequest();
                    result = getRequest.execute(myUrl).get();
                    remoteViews.setTextViewText(R.id.textView,result);
                    Log.v("WidgetUpdate","Updated from Server");
                    Log.v("Server Message",result);
                }
                else {
                    //remoteViews.setTextViewText(R.id.textView, pref.getString("trStatus", ""));
                    Log.v("DataHolder", DataHolder.getStatus());
                }
                Intent intent = new Intent(context, TrainStatusWidgetProvider.class);
                intent.setAction(AppWidgetManager.ACTION_APPWIDGET_UPDATE);
                intent.putExtra(AppWidgetManager.EXTRA_APPWIDGET_IDS, appWidgetIds);
                PendingIntent pendingIntent = PendingIntent.getBroadcast(context,
                        0, intent, PendingIntent.FLAG_UPDATE_CURRENT);
                remoteViews.setOnClickPendingIntent(R.id.actionButton, pendingIntent);
                appWidgetManager.updateAppWidget(widgetId, remoteViews);
            }
        }
        catch (Exception e){
            Log.v("WidgetException",e.getMessage());
        }
    }
}
