package com.example.fitcontrol.feature_members.presentation.add_member

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.fitcontrol.feature_members.domain.model.Member
import com.example.fitcontrol.feature_members.domain.use_case.SaveMember
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch
import java.util.UUID
import javax.inject.Inject

@HiltViewModel
class AddMemberViewModel @Inject constructor(
    private val saveMember: SaveMember
) : ViewModel() {

    private val _state = MutableStateFlow(AddMemberState())
    val state = _state.asStateFlow()

    fun onImageCaptured(uri: String) {
        _state.update { it.copy(capturedImageUri = uri) }
    }

    fun onSaveMember(name: String, phone: String, email: String, birthDate: String) {
        if (name.isBlank() || phone.isBlank() || email.isBlank()) {
            _state.update { it.copy(error = "Por favor completa los campos obligatorios") }
            return
        }

        viewModelScope.launch {
            _state.update { it.copy(isLoading = true, error = null) }
            try {
                val newMember = Member(
                    id = UUID.randomUUID().toString(),
                    name = name,
                    phone = phone,
                    email = email,
                    birthDate = birthDate,
                    photoUrl = _state.value.capturedImageUri
                )
                saveMember(newMember)
                _state.update { it.copy(isLoading = false, isSuccess = true) }
            } catch (e: Exception) {
                _state.update { it.copy(isLoading = false, error = e.message ?: "Error al guardar") }
            }
        }
    }
}