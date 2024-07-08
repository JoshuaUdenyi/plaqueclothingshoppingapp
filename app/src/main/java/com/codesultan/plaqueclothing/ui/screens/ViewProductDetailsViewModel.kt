package com.codesultan.plaqueclothing.ui.screens

import android.util.Log
import androidx.lifecycle.SavedStateHandle
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.codesultan.plaqueclothing.data.ApiService
import com.codesultan.plaqueclothing.data.ProductState
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableSharedFlow
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asSharedFlow
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class ViewProductDetailsViewModel @Inject constructor(private val savedStateHandle: SavedStateHandle) :
    ViewModel() {
    data class Product(
        val name: String = "",
        val price: String = "",
        val description: String? = null,
        val imageUrl: String = ""
    )


    private var productPrice: String = ""
    private var productId: String = ""
    private val productStateFlow = MutableStateFlow(Product())
    val productState: StateFlow<Product> = productStateFlow

    private val productLoadingFlow = MutableStateFlow(true)
    val productloading: StateFlow<Boolean> = productLoadingFlow


    private val productErrorFlow = MutableStateFlow(ErrorState())
    val productError: StateFlow<ErrorState> = productErrorFlow

    private val _eventFlow = MutableSharedFlow<UiEvent>()
    val eventFlow = _eventFlow.asSharedFlow()

    data class ErrorState(val state: Boolean = false, val message: String = "")

    sealed class UiEvent {
        data class ShowSnackbar(val message: String) : UiEvent()

        data object Loading : UiEvent()
        data object Success : UiEvent()

    }

    private val api = ApiService()

    init {

        savedStateHandle.get<String>("id")?.let { id ->
            Log.d("ProductId", id)
            if (id.isNotEmpty()) {
                productId = id
                fetchProduct(id)

            }
        }
        savedStateHandle.get<String>("price").let { price ->
            if (price != null) {
                productPrice = price
            }
        }
    }

    fun fetchProduct(id: String = productId) {
        viewModelScope.launch {
            val result = api.getProduct(id)

            when (result) {
                is ProductState.Error -> {
                    _eventFlow.emit(UiEvent.ShowSnackbar(result.errorMessage))
                    productLoadingFlow.value = false
                    productErrorFlow.value = ErrorState(state = true, message = result.errorMessage)
                }

                ProductState.Loading -> {
                    _eventFlow.emit(UiEvent.Loading)
                    productLoadingFlow.value = true
                    productErrorFlow.value = ErrorState()
                }

                is ProductState.Success -> {
                    // Now you cansafely access result.data
                    productStateFlow.value = Product(
                        name = result.data.name,
                        price = productPrice,
                        description = result.data.description ?: "No description available",
                        imageUrl = result.data.photos[0].url
                    )
                    _eventFlow.emit(UiEvent.Success)
                    productLoadingFlow.value = false
                    productErrorFlow.value = ErrorState()
                }
            }
        }
    }
}