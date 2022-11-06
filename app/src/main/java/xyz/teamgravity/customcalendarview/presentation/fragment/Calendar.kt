package xyz.teamgravity.customcalendarview.presentation.fragment

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import androidx.lifecycle.lifecycleScope
import com.kizitonwose.calendarview.utils.next
import com.kizitonwose.calendarview.utils.previous
import dagger.hilt.android.AndroidEntryPoint
import kotlinx.coroutines.flow.collectLatest
import kotlinx.coroutines.launch
import xyz.teamgravity.customcalendarview.core.util.Helper
import xyz.teamgravity.customcalendarview.databinding.FragmentCalendarBinding
import xyz.teamgravity.customcalendarview.presentation.adapter.CalendarAdapter
import xyz.teamgravity.customcalendarview.presentation.adapter.CalendarHeaderAdapter
import xyz.teamgravity.customcalendarview.presentation.adapter.DataAdapter
import xyz.teamgravity.customcalendarview.presentation.viewmodel.CalendarViewModel
import java.time.LocalDate
import java.time.YearMonth
import javax.inject.Inject

@AndroidEntryPoint
class Calendar : Fragment(), CalendarAdapter.CalendarListener {

    companion object {
        fun instance(): Calendar {
            return Calendar()
        }
    }

    private var _binding: FragmentCalendarBinding? = null
    private val binding get() = _binding!!

    private val viewmodel by viewModels<CalendarViewModel>()

    @Inject
    lateinit var headerAdapter: CalendarHeaderAdapter

    @Inject
    lateinit var adapter: CalendarAdapter

    @Inject
    lateinit var dataAdapter: DataAdapter

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View {
        _binding = FragmentCalendarBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        updateUI()
        observe()
        button()
    }

    private fun updateUI() {
        calendar()
        recyclerview()
    }

    private fun observe() {
        observeSelectedDate()
        observeTreatments()
        observeSurveys()
        observeData()
    }

    private fun button() {
        onScroll()
        onNext()
        onBefore()
    }

    private fun calendar() {
        binding.apply {
            adapter.listener = this@Calendar
            val currentMonth = YearMonth.now()
            calendar.setup(currentMonth.minusMonths(10), currentMonth.plusMonths(10), Helper.daysOfWeekFromLocale().first())
            calendar.scrollToMonth(currentMonth)
            calendar.dayBinder = adapter
            calendar.monthHeaderBinder = headerAdapter
        }
    }

    private fun recyclerview() {
        binding.recyclerview.adapter = dataAdapter
    }

    private fun observeSelectedDate() {
        viewLifecycleOwner.lifecycleScope.launch {
            viewmodel.selectedDate.collectLatest { data ->
                adapter.submitSelectedDate(data)
            }
        }
    }

    private fun observeTreatments() {
        viewLifecycleOwner.lifecycleScope.launch {
            viewmodel.treatments.collectLatest { data ->
                val treatments = data.groupBy { it.time }.mapValues {}
                adapter.submitTreatments(treatments)
            }
        }
    }

    private fun observeSurveys() {
        viewLifecycleOwner.lifecycleScope.launch {
            viewmodel.surveys.collectLatest { data ->
                val surveys = data.groupBy { it.time }.mapValues {}
                adapter.submitSurveys(surveys)
            }
        }
    }

    private fun observeData() {
        viewLifecycleOwner.lifecycleScope.launch {
            viewmodel.data.collectLatest { data ->
                dataAdapter.submitList(data)
            }
        }
    }

    private fun onScroll() {
        binding.apply {
            calendar.monthScrollListener = { month ->
                monthT.text = Helper.formatMonthYear(month)
            }
        }
    }

    private fun onNext() {
        binding.apply {
            navigateNextB.setOnClickListener {
                calendar.findFirstVisibleMonth()?.let { month ->
                    calendar.smoothScrollToMonth(month.yearMonth.next)
                }
            }
        }
    }

    private fun onBefore() {
        binding.apply {
            navigateBeforeB.setOnClickListener {
                calendar.findFirstVisibleMonth()?.let { month ->
                    calendar.smoothScrollToMonth(month.yearMonth.previous)
                }
            }
        }
    }

    override fun onDateChange(date: LocalDate) {
        binding.calendar.notifyDateChanged(date)
    }

    override fun onDateClick(date: LocalDate) {
        viewmodel.onSelectedDateChange(date)
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }
}