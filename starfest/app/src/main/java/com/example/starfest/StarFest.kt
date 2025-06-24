package com.example.starfest

import android.appwidget.AppWidgetManager
import android.appwidget.AppWidgetProvider
import android.content.Context
import android.widget.RemoteViews
import java.text.SimpleDateFormat
import java.util.*

class StarFest : AppWidgetProvider() {

    private val eventDate = "2025-07-01" // Your event date

    override fun onUpdate(context: Context, appWidgetManager: AppWidgetManager, appWidgetIds: IntArray) {
        for (widgetId in appWidgetIds) {
            val views = RemoteViews(context.packageName, R.layout.star_fest)

            // Countdown logic
            val daysLeft = getDaysLeft(eventDate)
            views.setTextViewText(R.id.countdown_text, "$daysLeft days left")

            // Static background image and logo
            views.setImageViewResource(R.id.bg_image, R.drawable.day1)
            views.setImageViewResource(R.id.logo_image, R.drawable.logo)

            appWidgetManager.updateAppWidget(widgetId, views)
        }
    }

    private fun getDaysLeft(targetDate: String): Int {
        val sdf = SimpleDateFormat("yyyy-MM-dd", Locale.getDefault())
        val event = sdf.parse(targetDate)
        val today = Calendar.getInstance().apply {
            set(Calendar.HOUR_OF_DAY, 0)
            set(Calendar.MINUTE, 0)
            set(Calendar.SECOND, 0)
            set(Calendar.MILLISECOND, 0)
        }.time
        return (((event?.time ?: 0) - today.time) / (1000 * 60 * 60 * 24)).toInt().coerceAtLeast(0)
    }
}
