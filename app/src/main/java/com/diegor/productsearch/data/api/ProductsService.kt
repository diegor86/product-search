package com.diegor.productsearch.data.api

import com.diegor.productsearch.data.entities.Page
import retrofit2.Response
import retrofit2.http.GET
import retrofit2.http.Query

interface ProductsService {
    @GET("search")
    suspend fun getProducts(
        @Query("q") query: String,
        @Query("offset") offset: Int? = null,
        @Query("limit") limit: Int = 25,
    ): Response<Page>

    companion object {
        const val BASE_URL = "https://api.mercadolibre.com/sites/MLA/"
    }
}