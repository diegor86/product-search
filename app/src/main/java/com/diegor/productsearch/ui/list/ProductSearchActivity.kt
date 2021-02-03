package com.diegor.productsearch.ui.list

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.widget.TextView
import androidx.activity.viewModels
import androidx.lifecycle.Observer
import com.diegor.productsearch.R
import com.diegor.productsearch.data.entities.Product
import dagger.hilt.android.AndroidEntryPoint

@AndroidEntryPoint
class ProductSearchActivity : AppCompatActivity() {

    private val viewModel: ProductSearchViewModel by viewModels()

    private val productsObserver = Observer<List<Product>> {
        val products = it ?: return@Observer

        products.firstOrNull()?.let {
            findViewById<TextView>(R.id.sarlanga).text = it.title
        }
        //adapter.submitList(entries)
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        viewModel.products.observe(this,
            Observer<List<Product>> {
                val products = it ?: return@Observer

                products.firstOrNull()?.let {
                    findViewById<TextView>(R.id.sarlanga).text = it.title
                }
                //adapter.submitList(entries)
            })

        viewModel.getProducts("Motorola")
    }
}