package xyz.teamgravity.customcalendarview.model

import java.time.LocalDate

data class SurveyModel(
    val name: String,
    val time: LocalDate,
) : DataModel()