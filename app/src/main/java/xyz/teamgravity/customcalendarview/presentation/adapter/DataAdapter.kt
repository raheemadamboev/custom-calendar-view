package xyz.teamgravity.customcalendarview.presentation.adapter

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.ListAdapter
import androidx.recyclerview.widget.RecyclerView.ViewHolder
import xyz.teamgravity.customcalendarview.data.model.DataModel
import xyz.teamgravity.customcalendarview.data.model.SurveyModel
import xyz.teamgravity.customcalendarview.data.model.TreatmentModel
import xyz.teamgravity.customcalendarview.databinding.CardSurveyBinding
import xyz.teamgravity.customcalendarview.databinding.CardTreatmentBinding

class DataAdapter(
    diff: DataDiff,
) : ListAdapter<DataModel, ViewHolder>(diff) {

    companion object {
        const val TREATMENT = 0x4545
        const val SURVEY = 0x4646
    }

    class TreatmentViewHolder(private val binding: CardTreatmentBinding) : ViewHolder(binding.root) {

        fun bind(model: TreatmentModel) {
            binding.apply {
                nameT.text = model.name
            }
        }
    }

    class SurveyViewHolder(private val binding: CardSurveyBinding) : ViewHolder(binding.root) {

        fun bind(model: SurveyModel) {
            binding.apply {
                nameT.text = model.name
            }
        }
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        val inflater = LayoutInflater.from(parent.context)
        return if (viewType == TREATMENT) TreatmentViewHolder(CardTreatmentBinding.inflate(inflater, parent, false))
        else SurveyViewHolder(CardSurveyBinding.inflate(inflater, parent, false))
    }

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        when (holder) {
            is TreatmentViewHolder -> holder.bind(getItem(position) as TreatmentModel)
            is SurveyViewHolder -> holder.bind(getItem(position) as SurveyModel)
        }
    }

    override fun getItemViewType(position: Int): Int {
        return if (getItem(position) is TreatmentModel) TREATMENT else SURVEY
    }

     class DataDiff : DiffUtil.ItemCallback<DataModel>() {
        override fun areItemsTheSame(oldItem: DataModel, newItem: DataModel): Boolean {
            return oldItem === newItem
        }

        override fun areContentsTheSame(oldItem: DataModel, newItem: DataModel): Boolean {
            return oldItem == newItem
        }
    }
}