package xyz.teamgravity.customcalendarview.presentation.adapter

import android.view.View
import com.kizitonwose.calendarview.model.CalendarDay
import com.kizitonwose.calendarview.model.DayOwner
import com.kizitonwose.calendarview.ui.DayBinder
import com.kizitonwose.calendarview.ui.ViewContainer
import xyz.teamgravity.customcalendarview.R
import xyz.teamgravity.customcalendarview.databinding.CardDayBinding
import java.time.LocalDate

class CalendarAdapter : DayBinder<CalendarAdapter.CalendarViewHolder> {

    var listener: CalendarListener? = null

    private var selectedDate: LocalDate = LocalDate.now()
    private var treatments: Map<LocalDate, Unit> = emptyMap()
    private var surveys: Map<LocalDate, Unit> = emptyMap()

    inner class CalendarViewHolder(private val binding: CardDayBinding) : ViewContainer(binding.root) {

        private lateinit var day: CalendarDay

        init {
            binding.root.setOnClickListener {
                if (day.owner == DayOwner.THIS_MONTH) {
                    if (selectedDate != day.date) {
                        listener?.onDateClick(day.date)
                    }
                }
            }
        }

        fun bind(model: CalendarDay) {
            binding.apply {
                this@CalendarViewHolder.day = model

                if (model.owner == DayOwner.THIS_MONTH) {
                    dayT.text = model.date.dayOfMonth.toString()
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

    ///////////////////////////////////////////////////////////////////////////
    // API
    ///////////////////////////////////////////////////////////////////////////

    fun submitSelectedDate(selectedDate: LocalDate) {
        val formerSelectedDate = this.selectedDate
        this.selectedDate = selectedDate
        listener?.onDateChange(formerSelectedDate)
        listener?.onDateChange(selectedDate)
    }

    fun submitTreatments(treatments: Map<LocalDate, Unit>) {
        this.treatments = treatments
        treatments.keys.forEach { date ->
            listener?.onDateChange(date)
        }
    }

    fun submitSurveys(surveys: Map<LocalDate, Unit>) {
        this.surveys = surveys
        surveys.keys.forEach { date ->
            listener?.onDateChange(date)
        }
    }

    ///////////////////////////////////////////////////////////////////////////
    // MISC
    ///////////////////////////////////////////////////////////////////////////

    interface CalendarListener {
        fun onDateChange(date: LocalDate)
        fun onDateClick(date: LocalDate)
    }
}