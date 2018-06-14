package com.coutocode.mychat;

import android.appwidget.AppWidgetManager;
import android.content.ComponentName;
import android.content.Context;
import android.content.SharedPreferences;
import android.util.Log;
import android.widget.RemoteViews;

import com.coutocode.mychat.utils.Constants;
import com.coutocode.mychat.widget.ChatAppWidget;
import com.firebase.jobdispatcher.JobParameters;
import com.firebase.jobdispatcher.JobService;

public class MyJobService extends JobService {
    @Override
    public boolean onStartJob(JobParameters job) {
        AppWidgetManager appWidgetManager = AppWidgetManager.getInstance(getApplicationContext());
        ComponentName thisWidget = new ComponentName(getApplicationContext(), ChatAppWidget.class);
        RemoteViews views = new RemoteViews(getApplicationContext().getPackageName(), R.layout.chat_app_widget);
        SharedPreferences pref =  getApplicationContext().getSharedPreferences(Constants.PREF_KEY, Context.MODE_PRIVATE);
        String user = pref.getString(Constants.USR_KEY, Constants.USR_KEY);
        String message = pref.getString(Constants.MSG_KEY, Constants.MSG_KEY);
        views.setTextViewText(R.id.tvUser, user);
        views.setTextViewText(R.id.tvMessage, message);
        appWidgetManager.updateAppWidget(thisWidget, views);
        return true;
    }

    @Override
    public boolean onStopJob(JobParameters job) {
        return false;
    }
}