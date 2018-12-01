package com.hliejun.dev.whatsappwidgets;

import android.appwidget.AppWidgetManager;
import android.appwidget.AppWidgetProvider;

import android.content.Context;

import android.widget.RemoteViews;

import com.hliejun.dev.whatsappwidgets.models.WidgetData;

// TODO: Replace preview image of widget

/**
 * Implementation of App Widget functionality.
 * App Widget Configuration implemented in {@link ConfigurationActivity ConfigurationActivity}
 */
public class MediumWidget extends AppWidgetProvider {

    static void updateAppWidget(Context context, AppWidgetManager appWidgetManager, int appWidgetId) {
        WidgetData data = ConfigurationActivity.loadWidgetPref(context, appWidgetId);

        // Construct the RemoteViews object
        RemoteViews views = new RemoteViews(context.getPackageName(), R.layout.medium_widget);

        // TODO: Relink view components and data
//        views.setTextViewText(R.id.appwidget_text, "STATIC TEXT");


        // Instruct the widget manager to update the widget
        appWidgetManager.updateAppWidget(appWidgetId, views);
    }

    @Override
    public void onUpdate(Context context, AppWidgetManager appWidgetManager, int[] appWidgetIds) {
        // There may be multiple widgets active, so update all of them
        for (int appWidgetId : appWidgetIds) {
            updateAppWidget(context, appWidgetManager, appWidgetId);
        }
    }

    @Override
    public void onDeleted(Context context, int[] appWidgetIds) {
        // When the user deletes the widget, delete the preference associated with it.
        for (int appWidgetId : appWidgetIds) {
            ConfigurationActivity.deleteWidgetPref(context, appWidgetId);
        }
    }

    @Override
    public void onEnabled(Context context) {
        // Enter relevant functionality for when the first widget is created
    }

    @Override
    public void onDisabled(Context context) {
        // Enter relevant functionality for when the last widget is disabled
    }

}

