package xyz.teamgravity.customcalendarview.model

import java.time.LocalDate

data class TreatmentModel(
    val name: String,
    val time: LocalDate,
) : DataModel()
