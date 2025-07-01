package com.example.starfest.com.example.starfest

data class AgendaItem(
    val time: String,
    val title: String
)

data class DayAgenda(
    val dayNumber: Int,
    val venue: String,
    val items: List<com.example.starfest.AgendaItem>
)
