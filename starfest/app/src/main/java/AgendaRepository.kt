object AgendaRepository {

    val agendaByDay = mapOf(
        1 to DayAgenda(
            1,
            "Grand Hyatt",
            listOf(
                AgendaItem("09:00 am - 11:00 am", "Arrive at airport, move by coach. Check-in by 11 am at Grand Hyatt"),
                AgendaItem("12:00 pm - 02:00 pm", "Lunch"),
                AgendaItem("03:00 pm - 05:00 pm", "Sessions Start with KK"),
                AgendaItem("05:00 pm - 05:30 pm", "Hi-Tea"),
                AgendaItem("05:30 pm - 07:00 pm", "Sessions Continue"),
                AgendaItem("07:00 pm - 08:00 pm", "Motivational Speaker"),
                AgendaItem("08:00 pm - 09:30 pm", "Rewards & Recognition"),
                AgendaItem("09:30 pm - 11:59 pm", "Dinner & Gala Night")
            )
        ),
        2 to DayAgenda(
            2,
            "Main Stage Area",
            listOf(
                AgendaItem("07:00 am - 10:00 am", "Breakfast"),
                AgendaItem("10:00 am - 02:00 pm", "Team Engagement Activities"),
                AgendaItem("02:00 pm - 03:00 pm", "Lunch @ The Chulha"),
                AgendaItem("03:00 pm - 03:30 pm", "Team Photo"),
                AgendaItem("03:30 pm - 07:00 pm", "Free Time"),
                AgendaItem("07:00 pm - 08:00 pm", "Drum Jam"),
                AgendaItem("08:00 pm - 08:15 pm", "Sand Art"),
                AgendaItem("08:15 pm - 09:00 pm", "Mentalist"),
                AgendaItem("09:00 pm - 11:59 pm", "Dinner & Gala Night")
            )
        ),
        3 to DayAgenda(
            3,
            "The Chulha",
            listOf(
                AgendaItem("07:00 am - 10:00 am", "Breakfast"),
                AgendaItem("10:00 am - 12:00 pm", "Free Time"),
                AgendaItem("12:00 pm - 01:00 pm", "Lunch @ The Chulha"),
                AgendaItem("01:00 pm - 01:30 pm", "Check Out")
            )
        )
    )
}
