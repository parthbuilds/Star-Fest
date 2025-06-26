data class AgendaItem(
    val time: String,
    val title: String
)

data class DayAgenda(
    val dayNumber: Int,
    val venue: String,
    val items: List<AgendaItem>
)
