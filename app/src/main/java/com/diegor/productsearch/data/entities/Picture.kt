package com.diegor.productsearch.data.entities

import com.google.gson.annotations.SerializedName

class Picture (
    @SerializedName("secure_url") val secureUrl: String = ""
)