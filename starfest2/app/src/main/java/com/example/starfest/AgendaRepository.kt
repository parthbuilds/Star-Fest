package com.example.starfest.com.example.starfest

object AgendaRepository {

    val agendaByDay = mapOf(
        1 to DayAgenda(
            1,
            "Grand Hyatt",
            listOf(
                _root_ide_package_.com.example.starfest.AgendaItem(
                    "09:00 am - 11:00 am",
                    "Arrive at airport, move by coach. Check-in by 11 am at Grand Hyatt"
                ),
                _root_ide_package_.com.example.starfest.AgendaItem("12:00 pm - 02:00 pm", "Lunch"),
                _root_ide_package_.com.example.starfest.AgendaItem(
                    "03:00 pm - 05:00 pm",
                    "Sessions Start with KK"
                ),
                _root_ide_package_.com.example.starfest.AgendaItem("05:00 pm - 05:30 pm", "Hi-Tea"),
                _root_ide_package_.com.example.starfest.AgendaItem(
                    "05:30 pm - 07:00 pm",
                    "Sessions Continue"
                ),
                _root_ide_package_.com.example.starfest.AgendaItem(
                    "07:00 pm - 08:00 pm",
                    "Motivational Speaker"
                ),
                _root_ide_package_.com.example.starfest.AgendaItem(
                    "08:00 pm - 09:30 pm",
                    "Rewards & Recognition"
                ),
                _root_ide_package_.com.example.starfest.AgendaItem(
                    "09:30 pm - 11:59 pm",
                    "Dinner & Gala Night"
                )
            )
        ),
        2 to DayAgenda(
            2,
            "Main Stage Area",
            listOf(
                _root_ide_package_.com.example.starfest.AgendaItem(
                    "07:00 am - 10:00 am",
                    "Breakfast"
                ),
                _root_ide_package_.com.example.starfest.AgendaItem(
                    "10:00 am - 02:00 pm",
                    "Team Engagement Activities"
                ),
                _root_ide_package_.com.example.starfest.AgendaItem(
                    "02:00 pm - 03:00 pm",
                    "Lunch @ The Chulha"
                ),
                _root_ide_package_.com.example.starfest.AgendaItem(
                    "03:00 pm - 03:30 pm",
                    "Team Photo"
                ),
                _root_ide_package_.com.example.starfest.AgendaItem(
                    "03:30 pm - 07:00 pm",
                    "Free Time"
                ),
                _root_ide_package_.com.example.starfest.AgendaItem(
                    "07:00 pm - 08:00 pm",
                    "Drum Jam"
                ),
                _root_ide_package_.com.example.starfest.AgendaItem(
                    "08:00 pm - 08:15 pm",
                    "Sand Art"
                ),
                _root_ide_package_.com.example.starfest.AgendaItem(
                    "08:15 pm - 09:00 pm",
                    "Mentalist"
                ),
                _root_ide_package_.com.example.starfest.AgendaItem(
                    "09:00 pm - 11:59 pm",
                    "Dinner & Gala Night"
                )
            )
        ),
        3 to DayAgenda(
            3,
            "The Chulha",
            listOf(
                _root_ide_package_.com.example.starfest.AgendaItem(
                    "07:00 am - 10:00 am",
                    "Breakfast"
                ),
                _root_ide_package_.com.example.starfest.AgendaItem(
                    "10:00 am - 12:00 pm",
                    "Free Time"
                ),
                _root_ide_package_.com.example.starfest.AgendaItem(
                    "12:00 pm - 01:00 pm",
                    "Lunch @ The Chulha"
                ),
                _root_ide_package_.com.example.starfest.AgendaItem(
                    "01:00 pm - 01:30 pm",
                    "Check Out"
                )
            )
        )
    )
}
