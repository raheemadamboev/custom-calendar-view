package xyz.teamgravity.customcalendarview.presentation.adapter

import android.view.View
import com.kizitonwose.calendarview.model.CalendarDay
import com.kizitonwose.calendarview.model.DayOwner
import com.kizitonwose.calendarview.ui.DayBinder
import com.kizitonwose.calendarview.ui.ViewContainer
import xyz.teamgravity.customcalendarview.data.model.DataModel
import xyz.teamgravity.customcalendarview.R
import xyz.teamgravity.customcalendarview.data.model.SurveyModel
import xyz.teamgravity.customcalendarview.data.model.TreatmentModel
import xyz.teamgravity.customcalendarview.databinding.CardDayBinding
import java.time.LocalDate

class CalendarAdapter(
    private val surveys: Map<LocalDate, List<SurveyModel>>,
    private val treatments: Map<LocalDate, List<TreatmentModel>>,
    private var selectedDate: LocalDate?,
    private val listener: CalendarListener,
) : DayBinder<CalendarAdapter.CalendarViewHolder> {

    inner class CalendarViewHolder(private val binding: CardDayBinding) : ViewContainer(binding.root) {

        private lateinit var day: CalendarDay

        init {
            binding.apply {
                root.setOnClickListener {
                    if (day.owner == DayOwner.THIS_MONTH) {
                        val currentSelection = selectedDate
                        if (currentSelection == day.date) {
                            selectedDate = null
                            listener.notifyDateChanged(currentSelection)
                            listener.onDateClick(emptyList())
                        } else {
                            selectedDate = day.date
                            listener.notifyDateChanged(day.date)
                            if (currentSelection != null) listener.notifyDateChanged(currentSelection)
                            listener.onDateClick(getData(day.date))
                        }
                    }
                }
            }
        }

        fun bind(day: CalendarDay) {
            binding.apply {
                this@CalendarViewHolder.day = day
                dayT.text = day.date.dayOfMonth.toString()

                if (day.owner == DayOwner.THIS_MONTH) {
                    root.setBackgroundResource(if (selectedDate == day.date) R.drawable.background_selected else R.drawable.background_unselected)
                    surveyI.visibility = if (surveys[day.date].isNullOrEmpty()) View.GONE else View.VISIBLE
                    treatmentI.visibility = if (treatments[day.date].isNullOrEmpty()) View.GONE else View.VISIBLE

                    root.visibility = View.VISIBLE
                } else {
                    root.visibility = View.INVISIBLE
                }
            }
        }
    }

    override fun create(view: View): CalendarViewHolder {
        return CalendarViewHolder(CardDayBinding.bind(view))
    }

    override fun bind(container: CalendarViewHolder, day: CalendarDay) {
        container.bind(day)
    }

    private fun getData(date: LocalDate): List<DataModel> {
        val data = mutableListOf<DataModel>()
        treatments[date]?.let { data.addAll(it) }
        surveys[date]?.let { data.addAll(it) }
        return data
    }

    interface CalendarListener {
        fun notifyDateChanged(date: LocalDate)
        fun onDateClick(data: List<DataModel>)
    }
}