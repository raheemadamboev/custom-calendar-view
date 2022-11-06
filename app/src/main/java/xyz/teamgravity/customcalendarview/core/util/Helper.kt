package xyz.teamgravity.customcalendarview.core.util

import com.kizitonwose.calendarview.model.CalendarMonth
import java.time.DayOfWeek
import java.time.format.DateTimeFormatter
import java.time.temporal.WeekFields
import java.util.*

object Helper {

    /**
     * Formatter that is used to format month and year
     */
    private val monthYearFormatter: DateTimeFormatter by lazy { DateTimeFormatter.ofPattern("MMMM yyyy") }

    ///////////////////////////////////////////////////////////////////////////
    // API
    ///////////////////////////////////////////////////////////////////////////

    /**
     * Returns array of DayOfWeek according to locale order
     * (MUN or SUN) first?
     */
    fun daysOfWeekFromLocale(): Array<DayOfWeek> {
        val firstDayOfWeek = WeekFields.of(Locale.getDefault()).firstDayOfWeek
        val daysOfWeek = DayOfWeek.values()
        // Order `daysOfWeek` array so that firstDayOfWeek is at index 0.
        // Only necessary if firstDayOfWeek is not DayOfWeek.MONDAY which has ordinal 0.
        if (firstDayOfWeek != DayOfWeek.MONDAY) {
            val rhs = daysOfWeek.sliceArray(firstDayOfWeek.ordinal..daysOfWeek.indices.last)
            val lhs = daysOfWeek.sliceArray(0 until firstDayOfWeek.ordinal)
            return rhs + lhs
        }
        return daysOfWeek
    }

    /**
     * Returns month and year formatted string in order to display in TextView
     */
    fun formatMonthYear(month: CalendarMonth): String {
        return monthYearFormatter.format(month.yearMonth)
    }
}