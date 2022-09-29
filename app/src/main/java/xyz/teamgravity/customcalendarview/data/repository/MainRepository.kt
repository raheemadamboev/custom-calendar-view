package xyz.teamgravity.customcalendarview.data.repository

import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.flow
import xyz.teamgravity.customcalendarview.data.model.SurveyModel
import xyz.teamgravity.customcalendarview.data.model.TreatmentModel
import java.time.LocalDate

class MainRepository {

    companion object {
        private val surveys = listOf(
            SurveyModel(name = "RipRoad", time = LocalDate.of(2022, 9, 7)),
            SurveyModel(name = "Aspiration", time = LocalDate.of(2022, 9, 23)),
            SurveyModel(name = "DSR", time = LocalDate.of(2022, 9, 28)),
            SurveyModel(name = "Gitlab", time = LocalDate.of(2022, 10, 21))
        )

        private val treatments = listOf(
            TreatmentModel(name = "Github", LocalDate.of(2022, 9, 10)),
            TreatmentModel(name = "Bitbucket", LocalDate.of(2022, 9, 13)),
            TreatmentModel(name = "Mercury", LocalDate.of(2022, 9, 28))
        )
    }

    ///////////////////////////////////////////////////////////////////////////
    // GET
    ///////////////////////////////////////////////////////////////////////////

    fun getSurveys(): Flow<List<SurveyModel>> {
        return flow { emit(surveys) }
    }

    fun getTreatments(): Flow<List<TreatmentModel>> {
        return flow { emit(treatments) }
    }
}