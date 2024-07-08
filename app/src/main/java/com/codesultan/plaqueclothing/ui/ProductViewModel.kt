package com.codesultan.plaqueclothing.ui

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.codesultan.plaqueclothing.data.ApiService
import com.codesultan.plaqueclothing.data.ProductsState
import com.codesultan.plaqueclothing.data.ResponseItem
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableSharedFlow
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asSharedFlow
import kotlinx.coroutines.flow.collectLatest
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class ProductViewModel @Inject constructor() : ViewModel() {
    private val productsStateFlow = MutableStateFlow<List<ResponseItem>>(emptyList())
    val productsState: StateFlow<List<ResponseItem>> = productsStateFlow

    private val productLoadingFlow = MutableStateFlow(false)
    val productloading: StateFlow<Boolean> = productLoadingFlow

    private val api = ApiService()

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


    init {
        viewModelScope.launch {

            val result = api.getProducts()
            result.collectLatest {

                when (it) {
                    is ProductsState.Error -> {

                        productLoadingFlow.value = false
                        productErrorFlow.value = ErrorState(state = true, message = it.errorMessage)
                    }

                    ProductsState.Loading -> {

                        productLoadingFlow.value = true
                        productErrorFlow.value = ErrorState()
                    }

                    is ProductsState.Success -> {
                        productsStateFlow.value = it.data
                        productLoadingFlow.value = false
                        productErrorFlow.value = ErrorState()
                    }
                }
            }

        }

    }

    fun fetchProducts() {

        viewModelScope.launch {

            val result = api.getProducts()
            result.collect { productsState ->

                when (productsState) {
                    is ProductsState.Error -> {

                        productLoadingFlow.value = false
                        productErrorFlow.value =
                            ErrorState(state = true, message = productsState.errorMessage)
                    }

                    ProductsState.Loading -> {
                        productLoadingFlow.value = true
                        productErrorFlow.value = ErrorState()
                    }

                    is ProductsState.Success -> {
                        productLoadingFlow.value = false
                        productErrorFlow.value = ErrorState()
                        productsStateFlow.value = productsState.data

                    }
                }
            }

        }
    }
}