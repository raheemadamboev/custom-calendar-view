package xyz.teamgravity.customcalendarview.data.model

import java.time.LocalDate

data class TreatmentModel(
    val name: String,
    val time: LocalDate,
) : DataModel()
