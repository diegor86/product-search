package com.diegor.productsearch.ui.list

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import com.diegor.productsearch.R
import com.diegor.productsearch.data.entities.Product

class ProductViewHolder(view: View) : RecyclerView.ViewHolder(view) {
    private val title: TextView = view.findViewById(R.id.product_title)

    private var product: Product? = null

    init {
        view.setOnClickListener {
            /*product?.url?.let { url ->
                val intent = Intent(Intent.ACTION_VIEW, Uri.parse(url))
                view.context.startActivity(intent)
            }*/
        }
    }

    fun bind(product: Product?) {
        if (product == null) {
            val resources = itemView.resources
            //title.text = resources.getString(R.string.loading)
        } else {
            showRepoData(product)
        }
    }

    private fun showRepoData(product: Product) {
        this.product = product
        title.text = product.title
    }

    companion object {
        fun create(parent: ViewGroup): ProductViewHolder {
            val view = LayoutInflater.from(parent.context)
                .inflate(R.layout.search_result_item, parent, false)
            return ProductViewHolder(view)
        }
    }
}