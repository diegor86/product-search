package com.diegor.productsearch.data

import androidx.paging.Pager
import androidx.paging.PagingConfig
import androidx.paging.PagingData
import com.diegor.productsearch.data.ProductsPagingSource.Companion.NETWORK_PAGE_SIZE
import com.diegor.productsearch.data.api.ProductsService
import com.diegor.productsearch.data.entities.Description
import com.diegor.productsearch.data.entities.Detail
import com.diegor.productsearch.data.entities.Product
import com.diegor.productsearch.util.result.Result
import kotlinx.coroutines.flow.Flow
import javax.inject.Inject
import javax.inject.Singleton

@Singleton
class ProductsRepository @Inject constructor(
    private val service: ProductsService,
    private val productsPagingSourceFactory: ProductsPagingSource.ProductsPagingSourceFactory
) {
    suspend fun getProducts(query: String): Result<List<Product>> {
        val response = service.getProducts(query)

        if (response.isSuccessful) {
            response.body()?.let {
                return Result.Success(it.results)
            }
        }
        return Result.Error()
    }

    suspend fun getProductDetail(productId: String): Result<Detail> {
        val response = service.getProductDetail(productId)

        if (response.isSuccessful) {
            response.body()?.let {
                return Result.Success(it)
            }
        }
        return Result.Error()
    }

    suspend fun getProductDescription(productId: String): Result<Description> {
        val response = service.getProductDescription(productId)

        if (response.isSuccessful) {
            response.body()?.let {
                return Result.Success(it)
            }
        }
        return Result.Error()
    }

    fun getSearchResultStream(query: String): Flow<PagingData<Product>> {
        return Pager(
            config = PagingConfig(pageSize = NETWORK_PAGE_SIZE, enablePlaceholders = false),
            pagingSourceFactory = { productsPagingSourceFactory.create(query) }
        ).flow
    }

}