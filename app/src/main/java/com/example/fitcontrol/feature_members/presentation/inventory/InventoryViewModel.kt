package com.example.fitcontrol.feature_members.presentation.inventory

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.fitcontrol.feature_members.domain.model.Product
import com.example.fitcontrol.feature_members.domain.model.InventoryMovement
import com.example.fitcontrol.feature_members.domain.repository.MemberRepository
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch
import javax.inject.Inject

data class InventoryState(
    val products: List<Product> = emptyList(),
    val isLoading: Boolean = false,
    val error: String? = null
)

@HiltViewModel
class InventoryViewModel @Inject constructor(
    private val repository: MemberRepository
) : ViewModel() {

    private val _state = MutableStateFlow(InventoryState())
    val state = _state.asStateFlow()

    init {
        loadProducts()
    }

    fun loadProducts() {
        viewModelScope.launch {
            _state.update { it.copy(isLoading = true, error = null) }
            try {
                val products = repository.getProducts()
                _state.update { it.copy(products = products, isLoading = false) }
            } catch (e: Exception) {
                // CAPTURAMOS EL ERROR PARA LA RÚBRICA
                _state.update { it.copy(
                    isLoading = false,
                    error = "Error al cargar inventario: ${e.message}"
                ) }
            }
        }
    }

    fun onSaveProduct(name: String, category: String, buyPrice: Double, sellPrice: Double, stock: Int, minStock: Int) {
        viewModelScope.launch {
            try {
                val newProduct = Product(
                    name = name,
                    category = category,
                    buy_price = buyPrice,
                    sell_price = sellPrice,
                    stock = stock,
                    min_stock = minStock
                )
                repository.saveProduct(newProduct)
                loadProducts()
            } catch (e: Exception) {
                _state.update { it.copy(error = "No se pudo guardar el producto") }
            }
        }
    }

    fun onRegisterMovement(productId: Int, type: String, quantity: Int, reason: String) {
        viewModelScope.launch {
            try {
                val movement = InventoryMovement(productId, type, quantity, reason)
                repository.registerMovement(movement)
                loadProducts()
            } catch (e: Exception) {
                _state.update { it.copy(error = "Error al registrar movimiento") }
            }
        }
    }
}