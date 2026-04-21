package com.example.fitcontrol.feature_members.presentation.home

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.fitcontrol.feature_auth.domain.repository.AuthRepository
import com.example.fitcontrol.feature_members.domain.model.Member
import com.example.fitcontrol.feature_members.domain.use_case.DeleteMember
import com.example.fitcontrol.feature_members.domain.use_case.GetMembers
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch
import javax.inject.Inject

// 1. Clase de estado para la RÚBRICA
data class HomeState(
    val isLoading: Boolean = false,
    val members: List<Member> = emptyList(),
    val error: String? = null
)

@HiltViewModel
class HomeViewModel @Inject constructor(
    private val getMembers: GetMembers,
    private val deleteMemberUseCase: DeleteMember,
    private val authRepository: AuthRepository // <--- AQUÍ LO INYECTAMOS
) : ViewModel() {

    private val _state = MutableStateFlow(HomeState())
    val state: StateFlow<HomeState> = _state.asStateFlow()

    init {
        refreshMembers()
    }

    fun refreshMembers() {
        viewModelScope.launch {
            _state.update { it.copy(isLoading = true, error = null) }

            try {
                getMembers().collect { memberList ->
                    _state.update { it.copy(
                        isLoading = false,
                        members = memberList
                    ) }
                }
            } catch (e: Exception) {
                _state.update { it.copy(
                    isLoading = false,
                    error = "No se pudieron cargar los socios: ${e.message}"
                ) }
            }
        }
    }

    fun onDeleteMember(member: Member) {
        viewModelScope.launch {
            try {
                deleteMemberUseCase(member)
                refreshMembers()
            } catch (e: Exception) {
                _state.update { it.copy(error = "No se pudo eliminar al socio") }
            }
        }
    }

    // Ahora authRepository ya funciona porque está arriba en el constructor
    fun onLogout() {
        viewModelScope.launch {
            authRepository.logout()
        }
    }
}