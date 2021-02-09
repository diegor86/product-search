package com.diegor.productsearch.data.entities

data class FullDetail (
    private val detail: Detail,
    private val _description: Description
) {
    val title = detail.title
    val pictures = detail.pictures
    val price = detail.price
    val description = _description.plainText
}