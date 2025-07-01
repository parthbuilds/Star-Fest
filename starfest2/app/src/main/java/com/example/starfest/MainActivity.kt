package com.example.starfest

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.verticalScroll
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.font.Font
import androidx.compose.ui.text.font.FontFamily
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.starfest.ui.theme.StarfestTheme
import java.text.SimpleDateFormat
import java.util.*

val Audiowide = FontFamily(Font(R.font.audiowide))

class MainActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContent {
            StarfestTheme {
                Surface(
                    modifier = Modifier.fillMaxSize(),
                    color = Color.Black
                ) {
                    EventTimeline()
                }
            }
        }
    }
}

@Composable
fun EventTimeline() {
    val allDays = AgendaRepository.agendaByDay.values.toList()
    val today = Calendar.getInstance()
    val timeFormatter = SimpleDateFormat("hh:mm a", Locale.getDefault())
    val currentTime = timeFormatter.format(today.time)

    var showWidgetGuide by remember { mutableStateOf(false) }

    Column(
        modifier = Modifier
            .fillMaxSize()
            .background(Color.Black)
            .verticalScroll(rememberScrollState())
            .padding(16.dp)
    ) {
        HeaderCard()

        Spacer(modifier = Modifier.height(16.dp))

        val eventStart = Calendar.getInstance().apply {
            set(2025, Calendar.JULY, 7)
        }
        val daysUntil = ((eventStart.timeInMillis - today.timeInMillis) / (1000 * 60 * 60 * 24)).toInt()

        if (daysUntil > 0) {
            BentoCard(
                title = "⏳ Days Until Event",
                content = "$daysUntil days to go!",
                color = Color(0xFF80ED99),
                modifier = Modifier.fillMaxWidth()
            )
            Spacer(modifier = Modifier.height(16.dp))
        }

        Row(
            modifier = Modifier.fillMaxWidth(),
            horizontalArrangement = Arrangement.spacedBy(16.dp)
        ) {
            BentoCard(
                title = "🏨 Venue & Swags",
                content = buildString {
                    appendLine(allDays.firstOrNull()?.venue ?: "--")
                    append("🎁 Swags: T-shirt, Mug, Stickers")
                },
                color = Color(0xFF57CC99),
                modifier = Modifier.weight(1f)
            )

            val currentEvent = allDays.flatMap { it.items }
                .find { event -> currentTime in event.time }?.title ?: "--"

            BentoCard(
                title = "🟢 Current Event",
                content = currentEvent,
                color = Color(0xFFFFC75F),
                modifier = Modifier.weight(1f)
            )
        }

        Spacer(modifier = Modifier.height(16.dp))

        allDays.forEach { day ->
            Column(modifier = Modifier.fillMaxWidth()) {
                Text(
                    text = "📅 Day ${day.dayNumber} - ${day.venue}",
                    fontSize = 20.sp,
                    fontFamily = Audiowide,
                    fontWeight = FontWeight.Bold,
                    color = Color.White,
                    modifier = Modifier.padding(start = 8.dp, bottom = 8.dp)
                )
                Column(
                    modifier = Modifier
                        .fillMaxWidth()
                        .background(Color(0xFF222222), shape = RoundedCornerShape(16.dp))
                        .padding(16.dp)
                ) {
                    day.items.forEachIndexed { index, item ->
                        Row(verticalAlignment = Alignment.CenterVertically) {
                            Column(horizontalAlignment = Alignment.CenterHorizontally) {
                                Box(
                                    modifier = Modifier
                                        .size(12.dp)
                                        .background(Color.White, shape = RoundedCornerShape(50))
                                )
                                if (index != day.items.lastIndex) {
                                    Box(
                                        modifier = Modifier
                                            .width(2.dp)
                                            .height(32.dp)
                                            .background(Color.Gray)
                                    )
                                }
                            }
                            Spacer(modifier = Modifier.width(12.dp))
                            Column {
                                Text(text = item.time, color = Color.LightGray, fontSize = 14.sp)
                                Text(text = item.title, color = Color.White, fontSize = 16.sp, fontWeight = FontWeight.SemiBold)
                            }
                        }
                        Spacer(modifier = Modifier.height(12.dp))
                    }
                }
                Spacer(modifier = Modifier.height(16.dp))
            }
        }

        BentoCard(
            title = "📲 Install Widget Guide",
            content = if (showWidgetGuide) "1. Long press on home screen\n2. Select Widgets\n3. Find StarFest\n4. Drag to screen\nEnjoy!" else "Tap to view how to install widget",
            color = Color(0xFFFF6B6B),
            modifier = Modifier
                .fillMaxWidth()
                .clickable { showWidgetGuide = !showWidgetGuide }
        )
    }
}

@Composable
fun HeaderCard() {
    Row(
        modifier = Modifier
            .fillMaxWidth()
            .background(Color.DarkGray, shape = RoundedCornerShape(16.dp))
            .padding(16.dp),
        verticalAlignment = Alignment.CenterVertically
    ) {
        Image(
            painter = painterResource(id = R.drawable.logo),
            contentDescription = "StarFest Logo",
            modifier = Modifier.size(36.dp)
        )

        Spacer(modifier = Modifier.width(12.dp))

        Text(
            text = "StarFest 2025",
            fontSize = 34.sp,
            fontFamily = Audiowide,
            fontWeight = FontWeight.Bold,
            color = Color.White
        )
    }
}

@Composable
fun BentoCard(title: String, content: String, color: Color, modifier: Modifier = Modifier) {
    Column(
        modifier = modifier
            .background(
                brush = Brush.verticalGradient(
                    colors = listOf(color, color.copy(alpha = 0.95f))
                ),
                shape = RoundedCornerShape(20.dp)
            )
            .padding(16.dp)
    ) {
        Text(
            text = title,
            fontSize = 20.sp,
            fontWeight = FontWeight.Bold,
            fontFamily = Audiowide,
            color = Color.Black
        )
        Spacer(modifier = Modifier.height(6.dp))
        Text(
            text = content,
            fontSize = 14.sp,
            color = Color.DarkGray
        )
    }
}