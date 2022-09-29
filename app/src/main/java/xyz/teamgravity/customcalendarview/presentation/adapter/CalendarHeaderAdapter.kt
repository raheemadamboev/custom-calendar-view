package xyz.teamgravity.customcalendarview.presentation.adapter

import android.view.View
import android.widget.TextView
import androidx.core.view.children
import com.kizitonwose.calendarview.model.CalendarMonth
import com.kizitonwose.calendarview.ui.MonthHeaderFooterBinder
import com.kizitonwose.calendarview.ui.ViewContainer
import xyz.teamgravity.customcalendarview.databinding.CardHeaderBinding
import java.time.DayOfWeek
import java.time.format.TextStyle
import java.util.*

class CalendarHeaderAdapter(
    private val daysOfWeek: Array<DayOfWeek>,
) : MonthHeaderFooterBinder<CalendarHeaderAdapter.CalendarHeaderViewHolder> {

    inner class CalendarHeaderViewHolder(private val binding: CardHeaderBinding) : ViewContainer(binding.root) {

        fun bind(month: CalendarMonth) {
            binding.apply {
                if (binding.root.tag == null) {
                    root.tag = month.yearMonth
                    root.children.map { it as TextView }.forEachIndexed { index, textView ->
                        textView.text = daysOfWeek[index].getDisplayName(TextStyle.SHORT, Locale.getDefault())
                    }
                }
            }
        }
    }

    override fun create(view: View): CalendarHeaderViewHolder {
        return CalendarHeaderViewHolder(CardHeaderBinding.bind(view))
    }

    override fun bind(container: CalendarHeaderViewHolder, month: CalendarMonth) {
        container.bind(month)
    }
}