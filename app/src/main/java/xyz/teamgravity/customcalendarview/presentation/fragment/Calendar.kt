package xyz.teamgravity.customcalendarview.presentation.fragment

import android.annotation.SuppressLint
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import com.kizitonwose.calendarview.utils.next
import com.kizitonwose.calendarview.utils.previous
import xyz.teamgravity.customcalendarview.presentation.adapter.CalendarAdapter
import xyz.teamgravity.customcalendarview.presentation.adapter.CalendarHeaderAdapter
import xyz.teamgravity.customcalendarview.presentation.adapter.DataAdapter
import xyz.teamgravity.customcalendarview.databinding.FragmentCalendarBinding
import xyz.teamgravity.customcalendarview.data.model.DataModel
import xyz.teamgravity.customcalendarview.data.model.SurveyModel
import xyz.teamgravity.customcalendarview.data.model.TreatmentModel
import java.time.DayOfWeek
import java.time.LocalDate
import java.time.YearMonth
import java.time.format.DateTimeFormatter
import java.time.temporal.WeekFields
import java.util.*

class Calendar : Fragment(), CalendarAdapter.CalendarListener {

    companion object {
        fun instance(): Calendar {
            return Calendar()
        }

        private val surveys = listOf(
            SurveyModel(name = "RipRoad", time = LocalDate.of(2022, 9, 7)),
            SurveyModel(name = "Aspiration", time = LocalDate.of(2022, 9, 23)),
            SurveyModel(name = "DSR", time = LocalDate.of(2022, 9, 28)),
            SurveyModel(name = "Gitlab", time = LocalDate.of(2022, 10, 21))
        ).groupBy { it.time }

        private val treatments = listOf(
            TreatmentModel(name = "Github", LocalDate.of(2022, 9, 10)),
            TreatmentModel(name = "Bitbucket", LocalDate.of(2022, 9, 13)),
            TreatmentModel(name = "Mercury", LocalDate.of(2022, 9, 28))
        ).groupBy { it.time }
    }

    private var _binding: FragmentCalendarBinding? = null
    private val binding get() = _binding!!

    private lateinit var dataAdapter: DataAdapter

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View {
        _binding = FragmentCalendarBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        updateUI()
    }

    @SuppressLint("SetTextI18n")
    private fun updateUI() {
        binding.apply {
            val daysOfWeek = daysOfWeekFromLocale()
            val currentMonth = YearMonth.now()
            calendar.setup(currentMonth.minusMonths(10), currentMonth.plusMonths(10), daysOfWeek.first())
            calendar.scrollToMonth(currentMonth)
            val adapter = CalendarAdapter(
                surveys = surveys,
                treatments = treatments,
                selectedDate = null,
                listener = this@Calendar
            )
            calendar.dayBinder = adapter
            val headerAdapter = CalendarHeaderAdapter(daysOfWeek)
            calendar.monthHeaderBinder = headerAdapter

            calendar.monthScrollListener = { month ->
                monthT.text = "${DateTimeFormatter.ofPattern("MMMM").format(month.yearMonth)} ${month.yearMonth.year}"
            }

            navigateBeforeB.setOnClickListener {
                calendar.findFirstVisibleMonth()?.let { month ->
                    calendar.smoothScrollToMonth(month.yearMonth.previous)
                }
            }

            navigateNextB.setOnClickListener {
                calendar.findFirstVisibleMonth()?.let { month ->
                    calendar.smoothScrollToMonth(month.yearMonth.next)
                }
            }

            dataAdapter = DataAdapter()
            recyclerview.adapter = dataAdapter
        }
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }

    private fun daysOfWeekFromLocale(): Array<DayOfWeek> {
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

    override fun notifyDateChanged(date: LocalDate) {
        binding.calendar.notifyDateChanged(date)
    }

    override fun onDateClick(data: List<DataModel>) {
        dataAdapter.submitList(data)
    }
}