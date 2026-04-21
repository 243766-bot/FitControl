package com.example.fitcontrol.feature_members.presentation.reports

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.fitcontrol.feature_members.domain.model.DailyReport
import com.example.fitcontrol.feature_members.domain.repository.MemberRepository
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.update // Importante para actualizar el estado
import kotlinx.coroutines.launch
import javax.inject.Inject

// 1. Definimos una clase de estado para cumplir con la RÚBRICA (Loading, Success, Error)
data class ReportState(
    val report: DailyReport? = null,
    val isLoading: Boolean = false,
    val error: String? = null
)

@HiltViewModel
class ReportViewModel @Inject constructor(
    private val repository: MemberRepository
) : ViewModel() {

    // 2. Cambiamos la variable simple por el objeto de estado completo
    private val _state = MutableStateFlow(ReportState())
    val state = _state.asStateFlow()

    init {
        loadReport()
    }

    fun loadReport() {
        viewModelScope.launch {
            // 3. ESTADO: LOADING (Iniciamos la carga)
            _state.update { it.copy(isLoading = true, error = null) }

            try {
                // 4. ESTADO: SUCCESS (Obtenemos los datos con éxito)
                val dailyReport = repository.getDailyReport()
                _state.update { it.copy(
                    report = dailyReport,
                    isLoading = false
                ) }
            } catch (e: Exception) {
                // 5. ESTADO: ERROR (Capturamos fallos de red o base de datos)
                _state.update { it.copy(
                    isLoading = false,
                    error = "Error al conectar con AWS: ${e.localizedMessage}"
                ) }
            }
        }
    }
}