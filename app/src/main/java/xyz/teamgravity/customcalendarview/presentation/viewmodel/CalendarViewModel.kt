package xyz.teamgravity.customcalendarview.presentation.viewmodel

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.Job
import kotlinx.coroutines.flow.*
import kotlinx.coroutines.launch
import xyz.teamgravity.customcalendarview.data.model.DataModel
import xyz.teamgravity.customcalendarview.data.model.SurveyModel
import xyz.teamgravity.customcalendarview.data.model.TreatmentModel
import xyz.teamgravity.customcalendarview.data.repository.MainRepository
import java.time.LocalDate
import javax.inject.Inject

@HiltViewModel
class CalendarViewModel @Inject constructor(
    private val repository: MainRepository,
) : ViewModel() {

    private val _treatments = MutableStateFlow(emptyList<TreatmentModel>())
    val treatments: StateFlow<List<TreatmentModel>> = _treatments.asStateFlow()

    private val _surveys = MutableStateFlow(emptyList<SurveyModel>())
    val surveys: StateFlow<List<SurveyModel>> = _surveys.asStateFlow()

    private val _data = MutableStateFlow(emptyList<DataModel>())
    val data: StateFlow<List<DataModel>> = _data.asStateFlow()

    private var dataJob: Job? = null

    init {
        observe()
    }

    private fun observe() {
        observeTreatments()
        observeSurveys()
    }

    private fun observeTreatments() {
        viewModelScope.launch {
            repository.getTreatments().collectLatest { data ->
                _treatments.emit(data)
            }
        }
    }

    private fun observeSurveys() {
        viewModelScope.launch {
            repository.getSurveys().collectLatest { data ->
                _surveys.emit(data)
            }
        }
    }

    ///////////////////////////////////////////////////////////////////////////
    // API
    ///////////////////////////////////////////////////////////////////////////

    fun onDateChange(date: LocalDate) {
        dataJob?.cancel()
        dataJob = viewModelScope.launch {
            combine(treatments, surveys) { treatments, surveys ->
                val currentTreatments = treatments.filter { it.time == date }
                val currentSurveys = surveys.filter { it.time == date }
                return@combine currentTreatments + currentSurveys
            }.collectLatest { data ->
                _data.emit(data)
            }
        }
    }
}