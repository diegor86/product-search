package com.diegor.productsearch.data.api

import com.diegor.productsearch.data.entities.Description
import com.diegor.productsearch.data.entities.Detail
import com.diegor.productsearch.data.entities.Page
import retrofit2.Response
import retrofit2.http.GET
import retrofit2.http.Path
import retrofit2.http.Query

interface ProductsService {
    @GET("/sites/MLA/search")
    suspend fun getProducts(
        @Query("q") query: String,
        @Query("offset") offset: Int? = null,
        @Query("limit") limit: Int = 25,
    ): Response<Page>

    @GET("/items/{productId}")
    suspend fun getProductDetail(
        @Path("productId") productId: String
    ): Response<Detail>

    @GET("/items/{productId}/description")
    suspend fun getProductDescription(
        @Path("productId") productId: String
    ): Response<Description>

    companion object {
        const val BASE_URL = "https://api.mercadolibre.com"
    }
}