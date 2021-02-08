package com.diegor.productsearch.data.entities

import com.google.gson.annotations.SerializedName

class Description (
    @SerializedName("plain_text") val plainText: String = ""
)