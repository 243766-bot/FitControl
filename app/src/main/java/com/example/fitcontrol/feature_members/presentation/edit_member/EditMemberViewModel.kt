package com.example.fitcontrol.feature_members.presentation.edit_member

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.fitcontrol.feature_members.domain.model.Member
import com.example.fitcontrol.feature_members.domain.use_case.GetMemberById
import com.example.fitcontrol.feature_members.domain.use_case.UpdateMember
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch
import javax.inject.Inject

data class EditMemberState(
    val member: Member? = null,
    val isLoading: Boolean = false,
    val isSuccess: Boolean = false,
    val error: String? = null
)

@HiltViewModel
class EditMemberViewModel @Inject constructor(
    private val getMemberById: GetMemberById,
    private val updateMemberUseCase: UpdateMember
) : ViewModel() {

    private val _state = MutableStateFlow(EditMemberState())
    val state = _state.asStateFlow()

    fun loadMember(id: String) {
        viewModelScope.launch {
            _state.update { it.copy(isLoading = true) }
            val member = getMemberById(id)
            if (member != null) {
                _state.update { it.copy(member = member, isLoading = false) }
            } else {
                _state.update { it.copy(isLoading = false, error = "Socio no encontrado") }
            }
        }
    }

    fun onUpdateMember(name: String, phone: String, email: String, birthDate: String) {
        val currentMember = _state.value.member ?: return
        viewModelScope.launch {
            _state.update { it.copy(isLoading = true) }
            try {
                val updatedMember = currentMember.copy(
                    name = name,
                    phone = phone,
                    email = email,
                    birthDate = birthDate
                )
                updateMemberUseCase(updatedMember)
                _state.update { it.copy(isLoading = false, isSuccess = true) }
            } catch (e: Exception) {
                _state.update { it.copy(isLoading = false, error = e.message) }
            }
        }
    }
}