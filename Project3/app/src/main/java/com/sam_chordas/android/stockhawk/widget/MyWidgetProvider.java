package com.sam_chordas.android.stockhawk.widget;

import android.app.PendingIntent;
import android.app.TaskStackBuilder;
import android.appwidget.AppWidgetManager;
import android.appwidget.AppWidgetProvider;
import android.content.ComponentName;
import android.content.Context;
import android.content.Intent;
import android.widget.RemoteViews;

import com.sam_chordas.android.stockhawk.R;
import com.sam_chordas.android.stockhawk.ui.MyStocksActivity;

public class MyWidgetProvider extends AppWidgetProvider {


    @Override
    public void onUpdate(Context context, AppWidgetManager appWidgetManager, int[] appWidgetIds) {
        super.onUpdate(context, appWidgetManager, appWidgetIds);

        for (int appWidgetId : appWidgetIds) {
            RemoteViews views = new RemoteViews(context.getPackageName(), R.layout.app_widget);

            setRemoteAdapter(context, views);

            Intent widgetClickIntent = new Intent(context, MyStocksActivity.class);
            PendingIntent widgetClickPendingIntent = TaskStackBuilder.create(context)
                    .addNextIntentWithParentStack(widgetClickIntent)
                    .getPendingIntent(0, PendingIntent.FLAG_UPDATE_CURRENT);
            views.setPendingIntentTemplate(R.id.stock_widget_list, widgetClickPendingIntent);

            appWidgetManager.updateAppWidget(appWidgetId, views);
        }
    }

    @Override
    public void onReceive(Context context, Intent intent) {
        super.onReceive(context, intent);

        AppWidgetManager appWidgetManager = AppWidgetManager.getInstance(context);
        int[] appWidgetIds = appWidgetManager.getAppWidgetIds(
                new ComponentName(context, getClass())
        );
        appWidgetManager.notifyAppWidgetViewDataChanged(appWidgetIds, R.id.stock_widget_list);
    }

    private void setRemoteAdapter(Context context, RemoteViews views) {
        views.setRemoteAdapter(R.id.stock_widget_list, new Intent(context, MyWidgetRemoteViewsService.class));
    }
}
