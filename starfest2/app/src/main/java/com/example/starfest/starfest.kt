package com.example.starfest

import android.appwidget.AppWidgetManager
import android.appwidget.AppWidgetProvider
import android.content.Context
import android.graphics.*
import android.util.Log
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
        val today = Calendar.getInstance()
        val cutoffDate = sdf.parse(cutoffDateStr) ?: return

        for (widgetId in appWidgetIds) {
            val views = RemoteViews(context.packageName, R.layout.star_fest)
            val fontId = R.font.audiowide
            val white = Color.WHITE

            try {
                if (today.time.before(cutoffDate)) {
                    // Countdown mode
                    views.setViewVisibility(R.id.countdown_container, View.VISIBLE)
                    views.setViewVisibility(R.id.agenda_container, View.GONE)

                    val millisDiff = cutoffDate.time - today.timeInMillis
                    val daysLeft = (millisDiff / (1000 * 60 * 60 * 24)).toInt()
                    val hoursLeft = ((millisDiff / (1000 * 60 * 60)) % 24).toInt()
                    val minutesLeft = ((millisDiff / (1000 * 60)) % 60).toInt()

                    val timerText = String.format("%02d : %02d : %02d", daysLeft, hoursLeft, minutesLeft)
                    val daysText = "$daysLeft days left"

                    views.setImageViewBitmap(R.id.celebration_heading, renderTextBitmap(context, "Celebration Countdown", 20f, fontId, white, true))
                    views.setImageViewBitmap(R.id.countdown_label, renderTextBitmap(context, "StarFest", 28f, fontId, white, true))
                    views.setImageViewBitmap(R.id.countdown_text, renderTextBitmap(context, daysText, 20f, fontId, white, true))
                    views.setImageViewBitmap(R.id.time_remaining_text, renderTextBitmap(context, timerText, 50f, fontId, white, true))
                    views.setImageViewResource(R.id.logo_image, R.drawable.logo)

                    val imageIndex = (10 - daysLeft).coerceIn(0, 9)
                    views.setImageViewResource(R.id.bg_image, backgroundImages[imageIndex])
                } else {
                    // Agenda mode
                    views.setViewVisibility(R.id.countdown_container, View.GONE)
                    views.setViewVisibility(R.id.agenda_container, View.VISIBLE)
                    views.setImageViewResource(R.id.bg_image, R.drawable.bg_image)

                    val startDate = sdf.parse("2025-07-07") ?: continue
                    val dayOffset = ((today.timeInMillis - startDate.time) / (1000 * 60 * 60 * 24)).toInt() + 1

                    val agenda = AgendaRepository.agendaByDay[dayOffset] ?: continue

                    views.setImageViewBitmap(R.id.day_number, renderTextBitmap(context, "Day ${agenda.dayNumber}", 20f, fontId, white, true))
                    views.setImageViewBitmap(R.id.venue_text, renderTextBitmap(context, agenda.venue, 16f, fontId, white, false))

                    val currentTime = getCurrentTimeIn24h()
                    val currentMinutes = parseTime(currentTime)

                    val parsedItems = agenda.items.mapNotNull { item ->
                        try {
                            val range = item.time.split("-", "to", ignoreCase = true).map { it.trim() }
                            if (range.size < 2) return@mapNotNull null
                            val start = parseTime(range[0])
                            val end = parseTime(range[1])
                            if (start in 0..1440 && end in 0..1440 && start <= end) {
                                ParsedAgendaItem(start, end, item.title, item.time)
                            } else null
                        } catch (e: Exception) {
                            Log.e("StarFest", "Failed to parse agenda item: ${item.title} - ${item.time}", e)
                            null
                        }
                    }

                    var currentItem: ParsedAgendaItem? = null
                    var nextItem: ParsedAgendaItem? = null

                    for (item in parsedItems) {
                        if (currentMinutes in item.start until item.end) {
                            currentItem = item
                        } else if (currentMinutes < item.start && nextItem == null) {
                            nextItem = item
                        }
                    }

                    val currentText = currentItem?.title ?: "Break"
                    val currentTimeText = currentItem?.time ?: "--"
                    val nextText = nextItem?.let { "Next: ${it.title}" } ?: "Enjoy the Event!"

                    views.setImageViewBitmap(R.id.current_event_text, renderTextBitmap(context, currentText, 30f, fontId, white, true))
                    views.setImageViewBitmap(R.id.time_range, renderTextBitmap(context, currentTimeText, 16f, fontId, white, false))
                    views.setImageViewBitmap(R.id.upcoming_event_label, renderTextBitmap(context, nextText, 22f, fontId, white, false))
                }

                appWidgetManager.updateAppWidget(widgetId, views)

            } catch (e: Exception) {
                Log.e("StarFest", "Widget update failed: ${e.message}", e)
            }
        }
    }

    private fun getCurrentTimeIn24h(): String {
        val cal = Calendar.getInstance()
        val sdf = SimpleDateFormat("hh:mm a", Locale.US)
        return sdf.format(cal.time)
    }

    private fun parseTime(time: String): Int {
        val sdf = SimpleDateFormat("hh:mm a", Locale.US)
        return try {
            val date = sdf.parse(time.lowercase(Locale.US))
            val cal = Calendar.getInstance()
            cal.time = date!!
            cal.get(Calendar.HOUR_OF_DAY) * 60 + cal.get(Calendar.MINUTE)
        } catch (e: Exception) {
            Log.e("StarFest", "parseTime failed for time: $time", e)
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
            this.typeface = try {
                ResourcesCompat.getFont(context, fontResId) ?: Typeface.DEFAULT
            } catch (e: Exception) {
                Log.e("StarFest", "Font load failed: ${e.message}")
                Typeface.DEFAULT
            }
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

object AgendaRepository {
    val agendaByDay: Map<Int, Agenda> = mapOf(
        1 to Agenda(1, "Grand Hyatt", listOf(
            AgendaItem("Arrive at airport, move by coach. Check-in by 11 am at Grand Hyatt", "09:00 am - 11:00 am"),
            AgendaItem("Lunch", "12:00 pm - 02:00 pm"),
            AgendaItem("Sessions Start with KK", "03:00 pm - 05:00 pm"),
            AgendaItem("Hi-Tea", "05:00 pm - 05:30 pm"),
            AgendaItem("Sessions Continue", "05:30 pm - 07:00 pm"),
            AgendaItem("Motivational Speaker", "07:00 pm - 08:00 pm"),
            AgendaItem("Rewards & Recognition", "08:00 pm - 09:30 pm"),
            AgendaItem("Dinner & Gala Night", "09:30 pm - 11:59 pm")
        )),
        2 to Agenda(2, "Main Stage Area", listOf(
            AgendaItem("Breakfast", "07:00 am - 10:00 am"),
            AgendaItem("Team Engagement Activities", "10:00 am - 02:00 pm"),
            AgendaItem("Lunch @ The Chulha", "02:00 pm - 03:00 pm"),
            AgendaItem("Team Photo", "03:00 pm - 03:30 pm"),
            AgendaItem("Free Time", "03:30 pm - 07:00 pm"),
            AgendaItem("Drum Jam", "07:00 pm - 08:00 pm"),
            AgendaItem("Sand Art", "08:00 pm - 08:15 pm"),
            AgendaItem("Mentalist", "08:15 pm - 09:00 pm"),
            AgendaItem("Dinner & Gala Night", "09:00 pm - 11:59 pm")
        )),
        3 to Agenda(3, "The Chulha", listOf(
            AgendaItem("Breakfast", "07:00 am - 10:00 am"),
            AgendaItem("Free Time", "10:00 am - 12:00 pm"),
            AgendaItem("Lunch @ The Chulha", "12:00 pm - 01:00 pm"),
            AgendaItem("Check Out", "01:00 pm - 01:30 pm")
        ))
    )
}

data class Agenda(val dayNumber: Int, val venue: String, val items: List<AgendaItem>)
data class AgendaItem(val title: String, val time: String)
data class ParsedAgendaItem(val start: Int, val end: Int, val title: String, val time: String)
