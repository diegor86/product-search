package com.diegor.productsearch.data.entities

import android.os.Parcelable
import com.google.gson.annotations.SerializedName
import kotlinx.android.parcel.Parcelize

@Parcelize
data class Page(
    @SerializedName("paging") val paging: Paging,
    @SerializedName("results") val results: List<Product>
): Parcelable