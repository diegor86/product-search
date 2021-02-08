package com.diegor.productsearch.ui.detail

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.diegor.productsearch.data.entities.FullDetail
import com.diegor.productsearch.domain.GetProductDetailUseCase
import com.diegor.productsearch.util.CoroutinesDispatcherProvider
import com.diegor.productsearch.util.result.Event
import dagger.hilt.android.lifecycle.HiltViewModel
import com.diegor.productsearch.util.result.Result
import kotlinx.coroutines.flow.collect
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext
import javax.inject.Inject

@HiltViewModel
class ProductDetailViewModel @Inject constructor(
    private val getProductDetailUseCase: GetProductDetailUseCase,
    private val dispatcherProvider: CoroutinesDispatcherProvider
): ViewModel() {

    private val _product = MutableLiveData<ProductDetailUiModel>()
    val product: LiveData<ProductDetailUiModel>
        get() = _product

    private val _errors = MutableLiveData<Event<String>>()
    val errors: LiveData<Event<String>>
        get() = _errors

    private val _loading = MutableLiveData<Boolean>()
    val loading: LiveData<Boolean>
        get() = _loading

    fun getProductDetail(productId: String) = viewModelScope.launch(dispatcherProvider.computation) {
        getProductDetailUseCase(productId).collect { result ->
            when (result) {
                is Result.Loading -> {
                    withContext(dispatcherProvider.main) {
                        _loading.value = true
                    }
                }
                is Result.Success -> {
                    withContext(dispatcherProvider.main) {
                        emitDetail(result.data)
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

    private fun emitDetail(fullDetail: FullDetail) {
        val uiModel = ProductDetailUiModel(fullDetail.title, fullDetail.pictures.map { it.secureUrl }, fullDetail.description, fullDetail.price.toString())
        _product.value = uiModel
    }
}

class ProductDetailUiModel(
    val title: String,
    val images: List<String>,
    val description: String,
    val price: String
)