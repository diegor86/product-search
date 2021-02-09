package com.diegor.productsearch.ui

import android.content.Context
import androidx.arch.core.executor.testing.InstantTaskExecutorRule
import com.diegor.productsearch.data.entities.Description
import com.diegor.productsearch.data.entities.Detail
import com.diegor.productsearch.data.entities.FullDetail
import com.diegor.productsearch.data.entities.Picture
import com.diegor.productsearch.domain.GetProductDetailUseCase
import com.diegor.productsearch.ui.detail.ProductDetailUiModel
import com.diegor.productsearch.ui.detail.ProductDetailViewModel
import com.diegor.productsearch.util.MainCoroutineRule
import com.diegor.productsearch.util.getOrAwaitValue
import com.diegor.productsearch.util.provideFakeCoroutinesDispatcherProvider
import com.diegor.productsearch.util.result.Result
import com.nhaarman.mockitokotlin2.doReturn
import com.nhaarman.mockitokotlin2.mock
import com.nhaarman.mockitokotlin2.whenever
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.flow.flow
import kotlinx.coroutines.test.runBlockingTest
import org.junit.Assert
import org.junit.Before
import org.junit.Rule
import org.junit.Test
import org.mockito.ArgumentMatchers
import java.lang.Exception
import java.math.BigDecimal

@ExperimentalCoroutinesApi
class ProductDetailViewModelTest {

    private lateinit var getProductDetailUseCase: GetProductDetailUseCase
    private lateinit var appContext: Context

    private lateinit var viewModel: ProductDetailViewModel

    @get:Rule
    var coroutinesRule = MainCoroutineRule()

    @get:Rule
    var instantTaskExecutorRule = InstantTaskExecutorRule()

    @Before
    fun before() {
        getProductDetailUseCase = mock()
        appContext = mock()

        viewModel = ProductDetailViewModel(
            getProductDetailUseCase,
            provideFakeCoroutinesDispatcherProvider(coroutinesRule.testDispatcher),
            appContext
        )
    }

    @Test
    fun `Test get correct product detail`() = runBlockingTest  {
        val productId = "Some product id"

        val title = "Some title"
        val pictureUrl = "some url"
        val description = "Some product description"

        val formatedPrice = "$ 3.467.00"

        val fullProductDetail = FullDetail(
            Detail(
                title,
                listOf(Picture(pictureUrl)),
                BigDecimal(3467)
            ),
            Description(description)
        )

        whenever(getProductDetailUseCase.invoke(productId)) doReturn flow {
            emit(Result.Loading)
            emit(Result.Success(fullProductDetail))
        }

        whenever(appContext.getString(ArgumentMatchers.anyInt(), ArgumentMatchers.anyString())) doReturn (formatedPrice)

        val expected = ProductDetailUiModel(
            title,
            listOf(pictureUrl),
            description,
            formatedPrice
        )

        viewModel.getProductDetail(productId)
        val uiState = viewModel.product.getOrAwaitValue()

        Assert.assertEquals(expected, uiState)
    }

    @Test
    fun `Test error emited when obtained from use case`() = runBlockingTest  {
        val productId = "Some product id"

        val message = "Some exception"
        val exception = Exception(message)

        whenever(getProductDetailUseCase.invoke(productId)) doReturn flow {
            emit(Result.Loading)
            emit(Result.Error(exception))
        }

        val expected = exception.toString()

        viewModel.getProductDetail(productId)
        val uiState = viewModel.errors.getOrAwaitValue()

        Assert.assertEquals(expected, uiState.getContentIfNotHandled())
    }
}