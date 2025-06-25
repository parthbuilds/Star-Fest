package com.example.starfest

import android.appwidget.AppWidgetManager
import android.appwidget.AppWidgetProvider
import android.content.Context
import android.graphics.*
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

            val fontId = R.font.audiowide
            val white = Color.WHITE

            // Celebration Heading
            views.setImageViewBitmap(
                R.id.celebration_heading,
                renderTextBitmap(context, "Celebration Countdown", 20f, fontId, white, true)
            )

            // Set logo
            views.setImageViewResource(R.id.logo_image, R.drawable.logo)

            if (endDate != null) {
                val millisDiff = endDate.time - now.timeInMillis

                // Main title
                val titleBitmap = renderTextBitmap(context, "StarFest", 28f, fontId, white, true)
                views.setImageViewBitmap(R.id.countdown_label, titleBitmap)

                if (millisDiff <= 0) {
                    // Post-event mode
                    views.setImageViewBitmap(
                        R.id.time_remaining_text,
                        renderTextBitmap(context, "00 : 00 : 00", 48f, fontId, white, true)
                    )
                    views.setImageViewBitmap(
                        R.id.countdown_text,
                        renderTextBitmap(context, "See you next year!", 14f, fontId, white, true)
                    )
                    views.setImageViewResource(R.id.bg_image, R.drawable.day1)
                } else {
                    val daysLeft = (millisDiff / (1000 * 60 * 60 * 24)).toInt()
                    val hoursLeft = ((millisDiff / (1000 * 60 * 60)) % 24).toInt()
                    val minutesLeft = ((millisDiff / (1000 * 60)) % 60).toInt()

                    val timerText = String.format("%02d : %02d : %02d", daysLeft, hoursLeft, minutesLeft)
                    val daysText = "$daysLeft days left"

                    views.setImageViewBitmap(
                        R.id.time_remaining_text,
                        renderTextBitmap(context, timerText, 50f, fontId, white, true)
                    )
                    views.setImageViewBitmap(
                        R.id.countdown_text,
                        renderTextBitmap(context, daysText, 20f, fontId, white, true)
                    )

                    val imageIndex = (10 - daysLeft).coerceIn(0, 9)
                    views.setImageViewResource(R.id.bg_image, backgroundImages[imageIndex])
                }
            }

            appWidgetManager.updateAppWidget(widgetId, views)
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
