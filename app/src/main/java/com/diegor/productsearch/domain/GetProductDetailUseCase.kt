package com.diegor.productsearch.domain

import com.diegor.productsearch.data.ProductsRepository
import com.diegor.productsearch.data.entities.Description
import com.diegor.productsearch.data.entities.Detail
import com.diegor.productsearch.data.entities.FullDetail
import com.diegor.productsearch.util.result.Result
import com.diegor.productsearch.util.result.successOr
import kotlinx.coroutines.async
import kotlinx.coroutines.awaitAll
import kotlinx.coroutines.coroutineScope
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.flow
import javax.inject.Inject

class GetProductDetailUseCase @Inject constructor(
    private val repository: ProductsRepository
) {

    suspend operator fun invoke(productId: String): Flow<Result<FullDetail>> = flow {
        lateinit var detailResult: Result<Detail>
        lateinit var descriptionResult: Result<Description>

        emit(Result.Loading)

        coroutineScope {
            val deferreds = listOf(
                async { detailResult = repository.getProductDetail(productId) },
                async { descriptionResult = repository.getProductDescription(productId) }
            )
            deferreds.awaitAll()
        }

        if (detailResult is Result.Error) {
            emit(Result.Error((detailResult as Result.Error).exception))
            return@flow
        }

        if (descriptionResult is Result.Error) {
            emit(Result.Error((descriptionResult as Result.Error).exception))
            return@flow
        }

        val detail = detailResult.successOr(null)
        val description = descriptionResult.successOr(null)

        if (detail != null && description != null) {
            emit(
                Result.Success(
                    FullDetail(
                        detail,
                        description
                    )
                )
            )
        }
    }
}