package com.diegor.productsearch.ui.list

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.diegor.productsearch.data.ProductsRepository
import com.diegor.productsearch.data.entities.Product
import com.diegor.productsearch.util.CoroutinesDispatcherProvider
import com.diegor.productsearch.util.result.Event
import dagger.hilt.android.lifecycle.HiltViewModel
import com.diegor.productsearch.util.result.Result
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext
import javax.inject.Inject

@HiltViewModel
class ProductSearchViewModel @Inject constructor(
    private val repository: ProductsRepository,
    private val dispatcherProvider: CoroutinesDispatcherProvider
): ViewModel() {

    private val _products = MutableLiveData<List<Product>>()
    val products: LiveData<List<Product>>
        get() = _products

    private val _errors = MutableLiveData<Event<String>>()
    val errors: LiveData<Event<String>>
        get() = _errors

    private val _loading = MutableLiveData<Boolean>()
    val loading: LiveData<Boolean>
        get() = _loading

    fun getProducts(query: String) = viewModelScope.launch(dispatcherProvider.computation) {
        val result = repository.getProducts(query)

        when (result) {
            is Result.Loading -> {
                withContext(dispatcherProvider.main) {
                    _loading.value = true
                }
            }
            is Result.Success -> {
                withContext(dispatcherProvider.main) {
                    _loading.value = false
                    _products.value = result.data
                }
            }
            is Result.Error -> {
                withContext(dispatcherProvider.main) {
                    _loading.value = false
                    _errors.value = Event(result.exception.toString())
                }
            }
        }
    }

}