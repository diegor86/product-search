package com.diegor.productsearch.ui.list

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import com.diegor.productsearch.R
import com.facebook.drawee.view.SimpleDraweeView

class ProductViewHolder(view: View) : RecyclerView.ViewHolder(view) {
    private val title: TextView = view.findViewById(R.id.product_title)
    private val thumbnail: SimpleDraweeView = view.findViewById(R.id.product_thumbnail)
    private val price: TextView = view.findViewById(R.id.price)

    private var product: ProductUiModel? = null

    init {
        view.setOnClickListener {
            /*product?.url?.let { url ->
                val intent = Intent(Intent.ACTION_VIEW, Uri.parse(url))
                view.context.startActivity(intent)
            }*/
        }
    }

    fun bind(product: ProductUiModel?) {
        if (product == null) {
            val resources = itemView.resources
            //title.text = resources.getString(R.string.loading)
        } else {
            showProductData(product)
        }
    }

    private fun showProductData(product: ProductUiModel) {
        this.product = product
        title.text = product.title

        thumbnail.setImageURI(product.thumbnail)
        price.text = product.price
    }

    companion object {
        fun create(parent: ViewGroup): ProductViewHolder {
            val view = LayoutInflater.from(parent.context)
                .inflate(R.layout.search_result_item, parent, false)
            return ProductViewHolder(view)
        }
    }
}