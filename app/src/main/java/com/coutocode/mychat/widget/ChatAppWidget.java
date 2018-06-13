package com.coutocode.mychat.widget;

import android.appwidget.AppWidgetManager;
import android.appwidget.AppWidgetProvider;
import android.content.Context;
import android.content.SharedPreferences;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.widget.RemoteViews;

import com.coutocode.mychat.FirebaseAPI;
import com.coutocode.mychat.MessageModel;
import com.coutocode.mychat.R;
import com.coutocode.mychat.utils.Constants;
import com.google.firebase.database.ChildEventListener;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

public class ChatAppWidget extends AppWidgetProvider {

    @Override
    public void onUpdate(Context context, AppWidgetManager appWidgetManager, int[] appWidgetIds) {
        for (int appWidgetId : appWidgetIds) {
            updateAppWidget(context, appWidgetManager, appWidgetId);
        }
    }

    @Override
    public void onEnabled(Context context) { }

    @Override
    public void onDisabled(Context context) { }

    void updateAppWidget(Context context, AppWidgetManager appWidgetManager,
                                int appWidgetId) {

        RemoteViews views = new RemoteViews(context.getPackageName(), R.layout.chat_app_widget);
        SharedPreferences pref =  context.getSharedPreferences(Constants.PREF_KEY, Context.MODE_PRIVATE);
        String user = pref.getString(Constants.USR_KEY, Constants.USR_KEY);
        String message = pref.getString(Constants.MSG_KEY, Constants.MSG_KEY);
        views.setTextViewText(R.id.tvUser, user);
        views.setTextViewText(R.id.tvMessage, message);

        appWidgetManager.updateAppWidget(appWidgetId, views);
    }
}

