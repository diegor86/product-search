package com.diegor.productsearch.data

import com.diegor.productsearch.data.api.ProductsService
import com.diegor.productsearch.data.entities.Product
import com.diegor.productsearch.util.result.Result
import javax.inject.Inject
import javax.inject.Singleton

@Singleton
class ProductsRepository @Inject constructor(
    private val service: ProductsService
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
}