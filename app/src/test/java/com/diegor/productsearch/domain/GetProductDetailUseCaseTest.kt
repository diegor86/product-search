package com.diegor.productsearch.domain

import com.diegor.productsearch.data.ProductsRepository
import com.diegor.productsearch.data.entities.Description
import com.diegor.productsearch.data.entities.Detail
import com.diegor.productsearch.data.entities.FullDetail
import com.diegor.productsearch.util.result.Result
import com.nhaarman.mockitokotlin2.*
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.flow.toList
import kotlinx.coroutines.test.runBlockingTest
import org.junit.Assert
import org.junit.Before
import org.junit.Test
import org.mockito.ArgumentMatchers
import java.lang.Exception
import java.math.BigDecimal

@ExperimentalCoroutinesApi
class GetProductDetailUseCaseTest {

    lateinit var getProductDetailUseCase: GetProductDetailUseCase
    lateinit var repository: ProductsRepository

    @Before
    fun before() {
        repository = mock()

        getProductDetailUseCase = GetProductDetailUseCase(repository)
    }

    @Test
    fun `Test get full detail when detail and description are success`() = runBlockingTest {
        val productId = "Some product id"

        val detail = Detail(
            "Some Title",
            listOf(),
            BigDecimal(1537)
        )
        repository.stub {
            onBlocking { getProductDetail(productId) }.doReturn(Result.Success(detail))
        }

        val description = Description("Some description")

        repository.stub {
            onBlocking { getProductDescription(productId) }.doReturn(Result.Success(description))
        }

        val expected = listOf(
            Result.Loading,
            Result.Success(FullDetail(detail, description))
        )

        Assert.assertEquals(getProductDetailUseCase(productId).toList(), expected)
    }

    @Test
    fun `Test get error when description error`() = runBlockingTest {
        val detail = Detail(
            "Some Title",
            listOf(),
            BigDecimal(1537)
        )
        repository.stub {
            onBlocking { getProductDetail(ArgumentMatchers.anyString()) }.doReturn(Result.Success(detail))
        }

        val exception = Exception("Some exception")

        repository.stub {
            onBlocking { getProductDescription(ArgumentMatchers.anyString()) }.doReturn(Result.Error(exception))
        }

        val expected = listOf(
            Result.Loading,
            Result.Error(exception)
        )

        Assert.assertEquals(getProductDetailUseCase("some id").toList(), expected)
    }

    @Test
    fun `Test get error when detail error`() = runBlockingTest {
        val exception = Exception("Some exception")

        repository.stub {
            onBlocking { getProductDetail(ArgumentMatchers.anyString()) }.doReturn(Result.Error(exception))
        }

        val description = Description("Some description")

        repository.stub {
            onBlocking { getProductDescription(ArgumentMatchers.anyString()) }.doReturn(Result.Success(description))
        }

        val expected = listOf(
            Result.Loading,
            Result.Error(exception)
        )

        Assert.assertEquals(getProductDetailUseCase("some id").toList(), expected)
    }
}