package com.diegor.productsearch.data.entities

import com.google.gson.annotations.SerializedName
import java.math.BigDecimal

class Detail (
    @SerializedName("title") val title: String,
    @SerializedName("pictures") val pictures: List<Picture> = listOf(),
    @SerializedName("price") val price: BigDecimal
)