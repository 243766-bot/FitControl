package com.example.fitcontrol.feature_members.presentation.payment

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.fitcontrol.feature_members.domain.model.Membership
import com.example.fitcontrol.feature_members.domain.use_case.SaveMembership
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch
import java.text.SimpleDateFormat
import java.util.*
import javax.inject.Inject

data class PaymentState(
    val isLoading: Boolean = false,
    val isSuccess: Boolean = false,
    val error: String? = null
)

@HiltViewModel
class PaymentViewModel @Inject constructor(
    private val saveMembership: SaveMembership
) : ViewModel() {

    private val _state = MutableStateFlow(PaymentState())
    val state = _state.asStateFlow()

    fun onPay(memberId: String, planName: String, amount: Double) {
        viewModelScope.launch {
            _state.update { it.copy(isLoading = true) }
            
            val calendar = Calendar.getInstance()
            val monthsToAdd = when(planName) {
                "Mensual" -> 1
                "Trimestral" -> 3
                "Anual" -> 12
                else -> 1
            }
            calendar.add(Calendar.MONTH, monthsToAdd)
            val expiryDate = SimpleDateFormat("yyyy-MM-dd", Locale.getDefault()).format(calendar.time)

            val newMembership = Membership(
                member_id = memberId,
                plan_name = planName,
                amount = amount,
                expiry_date = expiryDate
            )

            try {
                saveMembership(newMembership)
                _state.update { it.copy(isLoading = false, isSuccess = true) }
            } catch (e: Exception) {
                _state.update { it.copy(isLoading = false, error = e.message) }
            }
        }
    }
}