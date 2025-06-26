// StarFest.kt
package com.example.starfest

import android.appwidget.AppWidgetManager
import android.appwidget.AppWidgetProvider
import android.content.Context
import android.graphics.*
import android.view.View
import android.widget.RemoteViews
import androidx.core.content.res.ResourcesCompat
import java.text.SimpleDateFormat
import java.util.*

class StarFest : AppWidgetProvider() {

    private val backgroundImages = listOf(
        R.drawable.day1, R.drawable.day2, R.drawable.day3, R.drawable.day4, R.drawable.day5,
        R.drawable.day6, R.drawable.day7, R.drawable.day8, R.drawable.day9, R.drawable.day10
    )

    override fun onUpdate(context: Context, appWidgetManager: AppWidgetManager, appWidgetIds: IntArray) {
        val cutoffDateStr = context.getString(R.string.countdown_end_date)
        val sdf = SimpleDateFormat("yyyy-MM-dd", Locale.getDefault())
        val today = getTestCalendar() // Use test calendar
        val cutoffDate = sdf.parse(cutoffDateStr) ?: return

        for (widgetId in appWidgetIds) {
            val views = RemoteViews(context.packageName, R.layout.star_fest)

            if (today.time.before(cutoffDate)) {
                views.setViewVisibility(R.id.countdown_container, View.VISIBLE)
                views.setViewVisibility(R.id.agenda_container, View.GONE)
                val millisDiff = cutoffDate.time - today.timeInMillis
                val fontId = R.font.audiowide
                val white = Color.WHITE

                views.setImageViewBitmap(R.id.celebration_heading, renderTextBitmap(context, "Celebration Countdown", 20f, fontId, white, true))
                views.setImageViewResource(R.id.logo_image, R.drawable.logo)
                views.setImageViewBitmap(R.id.countdown_label, renderTextBitmap(context, "StarFest", 28f, fontId, white, true))

                val daysLeft = (millisDiff / (1000 * 60 * 60 * 24)).toInt()
                val hoursLeft = ((millisDiff / (1000 * 60 * 60)) % 24).toInt()
                val minutesLeft = ((millisDiff / (1000 * 60)) % 60).toInt()

                val timerText = String.format("%02d : %02d : %02d", daysLeft, hoursLeft, minutesLeft)
                val daysText = "$daysLeft days left"

                views.setImageViewBitmap(R.id.time_remaining_text, renderTextBitmap(context, timerText, 50f, fontId, white, true))
                views.setImageViewBitmap(R.id.countdown_text, renderTextBitmap(context, daysText, 20f, fontId, white, true))
                val imageIndex = (10 - daysLeft).coerceIn(0, 9)
                views.setImageViewResource(R.id.bg_image, backgroundImages[imageIndex])
            } else {
                views.setViewVisibility(R.id.countdown_container, View.GONE)
                views.setViewVisibility(R.id.agenda_container, View.VISIBLE)

                val fontId = R.font.audiowide
                val white = Color.WHITE

                val startDate = sdf.parse("2025-07-07") ?: continue
                val dayOffset = ((today.time.time - startDate.time) / (1000 * 60 * 60 * 24)).toInt() + 1
                val agenda = AgendaRepository.agendaByDay[dayOffset] ?: continue

                views.setImageViewBitmap(R.id.day_number, renderTextBitmap(context, "Day ${agenda.dayNumber}", 20f, fontId, white))
                views.setImageViewBitmap(R.id.venue_text, renderTextBitmap(context, agenda.venue, 16f, fontId, white))

                val currentTime = getCurrentTimeIn24h()
                val currentMinutes = parseTime(currentTime)

                val parsedItems = agenda.items.mapNotNull { item ->
                    val range = item.time.split("-", "to", ignoreCase = true).map { it.trim() }
                    val start = parseTime(range[0])
                    val end = if (range.size > 1) parseTime(range[1]) else start + 30
                    if (start != -1 && end != -1) ParsedAgendaItem(start, end, item.title, item.time) else null
                }

                val currentItem = parsedItems.find { currentMinutes in it.start..it.end }
                val nextItem = parsedItems.firstOrNull { currentMinutes < it.start }

                val currentText = currentItem?.title ?: "Break"
                val currentTimeText = currentItem?.time ?: "--"
                views.setImageViewBitmap(R.id.current_event_text, renderTextBitmap(context, currentText, 30f, fontId, white, true))
                views.setImageViewBitmap(R.id.time_range, renderTextBitmap(context, currentTimeText, 16f, fontId, white, false))

                val nextText = nextItem?.let { "Next: ${it.title}" } ?: "Enjoy the Event!"
                views.setImageViewBitmap(R.id.upcoming_event_label, renderTextBitmap(context, nextText, 22f, fontId, white, false))
                views.setImageViewResource(R.id.bg_image, R.drawable.bg_image)
            }

            appWidgetManager.updateAppWidget(widgetId, views)
        }
    }

    private fun getCurrentTimeIn24h(): String {
        val cal = getTestCalendar() // Use test calendar
        val sdf = SimpleDateFormat("hh:mm a", Locale.US)
        return sdf.format(cal.time)
    }

    private fun getTestCalendar(): Calendar {
        return Calendar.getInstance().apply {
            set(2025, Calendar.JULY, 7, 13, 6) // <- Change only this line for testing
        }
    }

    private fun parseTime(time: String): Int {
        val sdf = SimpleDateFormat("hh:mm a", Locale.US)
        return try {
            val date = sdf.parse(time.lowercase(Locale.US))
            val cal = Calendar.getInstance()
            cal.time = date!!
            cal.get(Calendar.HOUR_OF_DAY) * 60 + cal.get(Calendar.MINUTE)
        } catch (e: Exception) {
            -1
        }
    }

    private fun renderTextBitmap(
        context: Context,
        text: String,
        spSize: Float,
        fontResId: Int,
        color: Int,
        shadow: Boolean = false
    ): Bitmap {
        val paint = Paint(Paint.ANTI_ALIAS_FLAG).apply {
            this.color = color
            this.textSize = spToPx(context, spSize)
            this.typeface = ResourcesCompat.getFont(context, fontResId)
            this.textAlign = Paint.Align.LEFT
            if (shadow) {
                setShadowLayer(10f, 4f, 4f, Color.parseColor("#99000000"))
            }
        }

        val bounds = Rect()
        paint.getTextBounds(text, 0, text.length, bounds)

        val padding = 32
        val bitmap = Bitmap.createBitmap(bounds.width() + padding, bounds.height() + padding, Bitmap.Config.ARGB_8888)
        val canvas = Canvas(bitmap)
        canvas.drawText(text, 16f, bounds.height().toFloat() + 16f, paint)

        return bitmap
    }

    private fun spToPx(context: Context, sp: Float): Float {
        return sp * context.resources.displayMetrics.scaledDensity
    }
}

data class ParsedAgendaItem(val start: Int, val end: Int, val title: String, val time: String)