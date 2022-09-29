package xyz.teamgravity.customcalendarview.presentation.adapter

import android.view.View
import com.kizitonwose.calendarview.model.CalendarDay
import com.kizitonwose.calendarview.model.DayOwner
import com.kizitonwose.calendarview.ui.DayBinder
import com.kizitonwose.calendarview.ui.ViewContainer
import xyz.teamgravity.customcalendarview.R
import xyz.teamgravity.customcalendarview.databinding.CardDayBinding
import java.time.LocalDate

class CalendarAdapter(
    private var selectedDate: LocalDate,
) : DayBinder<CalendarAdapter.CalendarViewHolder> {

    var listener: CalendarListener? = null

    private var treatments: Map<LocalDate, Unit> = emptyMap()
    private var surveys: Map<LocalDate, Unit> = emptyMap()

    inner class CalendarViewHolder(private val binding: CardDayBinding) : ViewContainer(binding.root) {

        private lateinit var day: CalendarDay

        init {
            binding.apply {
                root.setOnClickListener {
                    if (day.owner == DayOwner.THIS_MONTH) {
                        if (selectedDate != day.date) {
                            val formerSelectedDate = selectedDate
                            selectedDate = day.date
                            listener?.onDataChanged(formerSelectedDate)
                            listener?.onDataChanged(selectedDate)
                            listener?.onDateClick(selectedDate)
                        }
                    }
                }
            }
        }

        fun bind(model: CalendarDay) {
            binding.apply {
                this@CalendarViewHolder.day = model
                dayT.text = model.date.dayOfMonth.toString()

                if (model.owner == DayOwner.THIS_MONTH) {
                    root.setBackgroundResource(if (selectedDate == model.date) R.drawable.background_selected else R.drawable.background_unselected)
                    treatmentI.visibility = if (treatments[model.date] == Unit) View.VISIBLE else View.GONE
                    surveyI.visibility = if (surveys[model.date] == Unit) View.VISIBLE else View.GONE

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

    fun submitTreatments(treatments: Map<LocalDate, Unit>) {
        this.treatments = treatments
        treatments.keys.forEach { date ->
            listener?.onDataChanged(date)
        }
    }

    fun submitSurveys(surveys: Map<LocalDate, Unit>) {
        this.surveys = surveys
        surveys.keys.forEach { date ->
            listener?.onDataChanged(date)
        }
    }

    interface CalendarListener {
        fun onDataChanged(date: LocalDate)
        fun onDateClick(date: LocalDate)
    }
}