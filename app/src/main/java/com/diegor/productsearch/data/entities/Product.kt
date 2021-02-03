package com.diegor.productsearch.data.entities

import android.os.Parcelable
import com.google.gson.annotations.SerializedName
import kotlinx.android.parcel.Parcelize
import java.math.BigDecimal

@Parcelize
data class Product(
    @SerializedName("title") val title: String,
    @SerializedName("price") val price: BigDecimal
) : Parcelable