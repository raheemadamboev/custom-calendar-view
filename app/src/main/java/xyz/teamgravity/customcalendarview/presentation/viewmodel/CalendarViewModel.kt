package xyz.teamgravity.customcalendarview.presentation.viewmodel

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import dagger.hilt.android.lifecycle.HiltViewModel
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

    private val _selectedDate = MutableStateFlow(LocalDate.now())
    val selectedDate: StateFlow<LocalDate> = _selectedDate.asStateFlow()

    private val _treatments = MutableStateFlow(emptyList<TreatmentModel>())
    val treatments: StateFlow<List<TreatmentModel>> = _treatments.asStateFlow()

    private val _surveys = MutableStateFlow(emptyList<SurveyModel>())
    val surveys: StateFlow<List<SurveyModel>> = _surveys.asStateFlow()

    private val _data = MutableStateFlow(emptyList<DataModel>())
    val data: StateFlow<List<DataModel>> = _data.asStateFlow()

    init {
        observe()
    }

    private fun observe() {
        observeTreatments()
        observeSurveys()
        observeData()
    }

    // FIXME if data gets changed, notify calendar to reflect to removed dates
    private fun observeTreatments() {
        viewModelScope.launch {
            repository.getTreatments().collectLatest { data ->
                _treatments.emit(data)
            }
        }
    }

    // FIXME if data gets changed, notify calendar to reflect to removed dates
    private fun observeSurveys() {
        viewModelScope.launch {
            repository.getSurveys().collectLatest { data ->
                _surveys.emit(data)
            }
        }
    }

    private fun observeData() {
        viewModelScope.launch {
            combine(selectedDate, treatments, surveys) { selectedDate, treatments, surveys ->
                val currentTreatments = treatments.filter { it.time == selectedDate }
                val currentSurveys = surveys.filter { it.time == selectedDate }
                return@combine currentTreatments + currentSurveys
            }.collectLatest { data ->
                _data.emit(data)
            }
        }
    }

    ///////////////////////////////////////////////////////////////////////////
    // API
    ///////////////////////////////////////////////////////////////////////////

    fun onSelectedDateChange(value: LocalDate) {
        viewModelScope.launch {
            _selectedDate.emit(value)
        }
    }
}