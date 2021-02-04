package com.diegor.productsearch.data

import androidx.paging.PagingSource
import androidx.paging.PagingState
import com.diegor.productsearch.data.api.ProductsService
import com.diegor.productsearch.data.entities.Product
import dagger.assisted.Assisted
import dagger.assisted.AssistedFactory
import dagger.assisted.AssistedInject
import retrofit2.HttpException
import java.io.IOException
import java.lang.Exception

private const val API_STARTING_PAGE_INDEX = 0

class ProductsPagingSource @AssistedInject constructor(
    private val service: ProductsService,
    @Assisted private val query: String
) : PagingSource<Int, Product>() {
    override suspend fun load(params: LoadParams<Int>): LoadResult<Int, Product> {
        val position = params.key ?: API_STARTING_PAGE_INDEX
        val apiQuery = query
        return try {
            val response = service.getProducts(apiQuery, position, params.loadSize)

            if (!response.isSuccessful || response.body()?.results == null) {
                return LoadResult.Error(Exception())
            }

            val repos = response.body()?.results!!

            LoadResult.Page(
                data = repos,
                prevKey = if (position == API_STARTING_PAGE_INDEX) null else position - NETWORK_PAGE_SIZE,
                nextKey = if (repos.isEmpty()) null else position + params.loadSize
            )
        } catch (exception: IOException) {
            LoadResult.Error(exception)
        } catch (exception: HttpException) {
            LoadResult.Error(exception)
        }
    }

    override fun getRefreshKey(state: PagingState<Int, Product>): Int? {
        return state.anchorPosition
    }

    @AssistedFactory
    interface ProductsPagingSourceFactory { fun create(query: String) : ProductsPagingSource }

    companion object {
        const val NETWORK_PAGE_SIZE = 10
    }
}