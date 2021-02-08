package com.diegor.productsearch.ui.detail

import android.content.Context
import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import androidx.activity.viewModels
import androidx.lifecycle.Observer
import androidx.viewpager2.widget.ViewPager2
import com.diegor.productsearch.R
import com.diegor.productsearch.databinding.ActivityProductDetailBinding
import dagger.hilt.android.AndroidEntryPoint

@AndroidEntryPoint
class ProductDetailActivity : AppCompatActivity() {
    private lateinit var binding: ActivityProductDetailBinding

    private val viewModel: ProductDetailViewModel by viewModels()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        binding = ActivityProductDetailBinding.inflate(layoutInflater)
        val view = binding.root
        setContentView(view)

        viewModel.product.observe(this, Observer<ProductDetailUiModel> { product: ProductDetailUiModel? ->
            product?.let { showProductData(it) }
        })

        intent.getStringExtra(PRODUCT_ID_EXTRA)?.let {
            viewModel.getProductDetail(it)
        }
    }

    private fun showProductData(product: ProductDetailUiModel) {
        binding.title.text = product.title
        binding.description.text = product.description
        binding.price.text = product.price

        initGallery(product)
    }

    private fun initGallery(product: ProductDetailUiModel) {
        if (product.images.isNotEmpty()) {
            val pagerAdapter = GalleryAdapter(product.images)
            binding.galleryPager.adapter = pagerAdapter
            binding.imageSelected.text = getString(R.string.image_selected, 1, product.images.size)
            binding.galleryPager.registerOnPageChangeCallback(object : ViewPager2.OnPageChangeCallback() {
                override fun onPageSelected(position: Int) {
                    binding.imageSelected.text = getString(R.string.image_selected, position + 1, product.images.size)
                }
            })
        }
    }

    companion object {
        private const val PRODUCT_ID_EXTRA = "PRODUCT_ID_EXTRA"

        fun newIntent(context: Context, productId: String): Intent {
            val intent = Intent(context, ProductDetailActivity::class.java);
            intent.putExtra(PRODUCT_ID_EXTRA, productId)
            return intent
        }
    }
}