package com.diegor.productsearch.ui.list

import android.content.Context
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import androidx.paging.PagingData
import androidx.paging.cachedIn
import androidx.paging.map
import com.diegor.productsearch.data.ProductsRepository
import com.diegor.productsearch.data.entities.Product
import com.diegor.productsearch.util.CoroutinesDispatcherProvider
import com.diegor.productsearch.util.asPrice
import com.diegor.productsearch.util.result.Event
import dagger.hilt.android.lifecycle.HiltViewModel
import com.diegor.productsearch.util.result.Result
import com.google.gson.annotations.SerializedName
import dagger.hilt.android.qualifiers.ApplicationContext
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.map
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext
import java.math.BigDecimal
import javax.inject.Inject

@HiltViewModel
class ProductSearchViewModel @Inject constructor(
    private val repository: ProductsRepository,
    private val dispatcherProvider: CoroutinesDispatcherProvider,
    @ApplicationContext private val appContext: Context
): ViewModel() {

    private var currentQueryValue: String? = null

    private var currentSearchResult: Flow<PagingData<ProductUiModel>>? = null

    fun searchProducts(queryString: String): Flow<PagingData<ProductUiModel>> {
        val lastResult = currentSearchResult
        if (queryString == currentQueryValue && lastResult != null) {
            return lastResult
        }
        currentQueryValue = queryString
        val newResult: Flow<PagingData<ProductUiModel>> = repository.getSearchResultStream(queryString)
            .map { pagingData -> pagingData.map { ProductUiModel(
                it.id,
                it.title,
                it.price.asPrice(appContext),
                it.thumbnail
            ) } }
            .cachedIn(viewModelScope)
        currentSearchResult = newResult
        return newResult
    }
}

data class ProductUiModel(
    val id: String,
    val title: String,
    val price: String,
    val thumbnail: String
)