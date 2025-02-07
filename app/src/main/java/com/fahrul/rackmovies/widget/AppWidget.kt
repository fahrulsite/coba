package com.fahrul.rackmovies.widget

import android.app.PendingIntent
import android.appwidget.AppWidgetManager
import android.appwidget.AppWidgetProvider
import android.content.ComponentName
import android.content.Context
import android.content.Intent
import android.net.Uri
import android.util.Log
import android.widget.RemoteViews
import androidx.core.net.toUri
import com.fahrul.rackmovies.R
import com.fahrul.rackmovies.ui.activity.DetailMoviesActivity

/**
 * Implementation of App Widget functionality.
 */
class AppWidget : AppWidgetProvider() {
    companion object {

        private const val TOAST_ACTION = "com.fahrul.rackmovies.CLICK_ITEM_ACTION"
        const val EXTRA_ITEM = "com.fahrul.rackmovies.EXTRA_ITEM"
        const val UPDATE_WIDGET = "com.fahrul.rackmovies.UPDATE_WIDGET"

        internal fun updateAppWidget(
            context: Context, appWidgetManager: AppWidgetManager,
            appWidgetId: Int
        ) {
            // Construct the RemoteViews object
            val intent = Intent(context, StackWidgetService::class.java)
            intent.putExtra(AppWidgetManager.EXTRA_APPWIDGET_ID, appWidgetId)
            intent.data = Uri.parse(intent.toUri(Intent.URI_INTENT_SCHEME))

            val views = RemoteViews(context.packageName, R.layout.app_widget)
            views.setRemoteAdapter(R.id.stack_view, intent)
            views.setEmptyView(R.id.stack_view, R.id.empty_view)

            val toastIntent = Intent(context, AppWidget::class.java)
            toastIntent.action = TOAST_ACTION
            toastIntent.putExtra(AppWidgetManager.EXTRA_APPWIDGET_ID, appWidgetId)
            intent.data = Uri.parse(intent.toUri(Intent.URI_INTENT_SCHEME))
            val toastPendingIntent = PendingIntent.getBroadcast(
                context,
                0,
                toastIntent,
                PendingIntent.FLAG_UPDATE_CURRENT
            )
            views.setPendingIntentTemplate(R.id.stack_view, toastPendingIntent)

            appWidgetManager.updateAppWidget(appWidgetId, views)
        }
    }

    override fun onReceive(context: Context, intent: Intent) {
        super.onReceive(context, intent)

        if (intent.action != null) {
            if (intent.action == UPDATE_WIDGET) {
                val widgetManager = AppWidgetManager.getInstance(context)
                val widgetIds = widgetManager.getAppWidgetIds(ComponentName(context, AppWidget::class.java))
                Log.d("widget", "update")
                for (widgetId in widgetIds) {
                    widgetManager.notifyAppWidgetViewDataChanged(widgetId, R.id.stack_view)
                }
            }
        }
    }

    override fun onUpdate(
        context: Context, appWidgetManager: AppWidgetManager, appWidgetIds: IntArray) {
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

}