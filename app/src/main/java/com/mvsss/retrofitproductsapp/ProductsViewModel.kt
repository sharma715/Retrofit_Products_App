package com.mvsss.retrofitproductsapp

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.mvsss.retrofitproductsapp.model.Product
import kotlinx.coroutines.channels.Channel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.collectLatest
import kotlinx.coroutines.flow.receiveAsFlow
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch

class ProductsViewModel(private val productRepository: ProductRepositoryImp) : ViewModel(){

    private val _products = MutableStateFlow<List<Product>>(emptyList())
     val products = _products.asStateFlow()

    private val _showError = Channel<Boolean>()
     val showError = _showError.receiveAsFlow()

    init {
        viewModelScope.launch {
            productRepository.getProducts().collectLatest { result ->
                when(result){
                    is Result.Error -> { _showError.send(true) }
                    is Result.Success ->{
                        result.data?.let {products ->
                            _products.update { products }
                        }
                    }
                }
            }
        }
    }
}