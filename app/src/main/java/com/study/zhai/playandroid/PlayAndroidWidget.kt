package com.study.zhai.playandroid

import android.app.PendingIntent
import android.appwidget.AppWidgetManager
import android.appwidget.AppWidgetProvider
import android.content.Context
import android.content.Intent
import android.widget.RemoteViews

/**
 * Implementation of App Widget functionality.
 */
class PlayAndroidWidget : AppWidgetProvider() {

    override fun onUpdate(context: Context, appWidgetManager: AppWidgetManager, appWidgetIds: IntArray) {
        // There may be multiple widgets active, so update all of them
        for (appWidgetId in appWidgetIds) {
            updateAppWidget(context, appWidgetManager, appWidgetId)
        }
    }

    override fun onEnabled(context: Context) {
        // Enter relevant functionality for when the first widget is created
    }

    override fun onDisabled(context: Context) {
        // Enter relevant functionality for when the last widget is disabled
    }

    companion object {

        private val START_ACT_ACTION = "com.study.zhai.playandroid"
        private val RECEIVER_ACTION_STATUS = "com.widget.STATUS_CHANGED"
        private val OPEN_ACT_CODE = 111
        private lateinit var remoteViews : RemoteViews


        internal fun updateAppWidget(context: Context, appWidgetManager: AppWidgetManager,
                                     appWidgetId: Int) {

            val widgetText = context.getString(R.string.appwidget_text)
            // Construct the RemoteViews object
            remoteViews = RemoteViews(context.packageName, R.layout.paly_android_widget)
            remoteViews.setTextViewText(R.id.appwidget_text, widgetText)
            openAct(context)

            // Instruct the widget manager to update the widget
            appWidgetManager.updateAppWidget(appWidgetId, remoteViews)
        }

        private fun openAct(context: Context) {
            var intent = Intent()
            intent.action = START_ACT_ACTION
            var pi = PendingIntent.getActivity(context, OPEN_ACT_CODE, intent, PendingIntent.FLAG_CANCEL_CURRENT)
            remoteViews.setOnClickPendingIntent(R.id.appwidget_text, pi)
        }
    }
}

