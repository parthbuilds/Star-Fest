package com.example.starfest

import android.appwidget.AppWidgetManager
import android.appwidget.AppWidgetProvider
import android.content.Context
import android.widget.RemoteViews
import java.text.SimpleDateFormat
import java.util.*

class StarFest : AppWidgetProvider() {

    // List of 10 drawable backgrounds for countdown days
    private val backgroundImages = listOf(
        R.drawable.day1, R.drawable.day2, R.drawable.day3, R.drawable.day4, R.drawable.day5,
        R.drawable.day6, R.drawable.day7, R.drawable.day8, R.drawable.day9, R.drawable.day10
    )

    override fun onUpdate(context: Context, appWidgetManager: AppWidgetManager, appWidgetIds: IntArray) {
        val cutoffDateStr = context.getString(R.string.countdown_end_date) // e.g. "2025-07-07"

        for (widgetId in appWidgetIds) {
            val views = RemoteViews(context.packageName, R.layout.star_fest)

            val sdf = SimpleDateFormat("yyyy-MM-dd", Locale.getDefault())
            val now = Calendar.getInstance()
            val endDate: Date? = try {
                sdf.parse(cutoffDateStr)
            } catch (e: Exception) {
                e.printStackTrace()
                null
            }

            if (endDate != null) {
                val millisDiff = endDate.time - now.timeInMillis

                if (millisDiff <= 0) {
                    // Event day has arrived
                    views.setTextViewText(R.id.countdown_text, "Let's Go!")
                    views.setTextViewText(R.id.countdown_label, "StarFest")
                    views.setTextViewText(R.id.time_remaining_text, "00 : 00 : 00")
                    views.setImageViewResource(R.id.bg_image, R.drawable.day1) // fallback bg
                } else {
                    val daysLeft = (millisDiff / (1000 * 60 * 60 * 24)).toInt()
                    val hoursLeft = ((millisDiff / (1000 * 60 * 60)) % 24).toInt()
                    val minutesLeft = ((millisDiff / (1000 * 60)) % 60).toInt()

                    // Set bottom text
                    views.setTextViewText(R.id.countdown_text, "$daysLeft days left")
                    views.setTextViewText(R.id.countdown_label, "StarFest")

                    // Set center countdown timer
                    val timerText = String.format("%02d : %02d : %02d", daysLeft, hoursLeft, minutesLeft)
                    views.setTextViewText(R.id.time_remaining_text, timerText)

                    // Background image per day logic
                    val imageIndex = (10 - daysLeft).coerceIn(0, 9)
                    views.setImageViewResource(R.id.bg_image, backgroundImages[imageIndex])
                }

                // Set logo
                views.setImageViewResource(R.id.logo_image, R.drawable.logo)

                // Update the widget
                appWidgetManager.updateAppWidget(widgetId, views)
            }
        }
    }
}
