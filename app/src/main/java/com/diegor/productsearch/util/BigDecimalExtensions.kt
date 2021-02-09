package com.diegor.productsearch.util

import android.content.Context
import com.diegor.productsearch.R
import java.math.BigDecimal
import java.math.RoundingMode
import java.text.DecimalFormat
import java.text.DecimalFormatSymbols

fun BigDecimal.asPrice(context: Context) = context.getString(R.string.price_format, this.toStringWithTwoDecimals())

fun BigDecimal.toStringWithTwoDecimals(): String {

    return formatNumber(
        number = this,
        decimalSeparator = ',',
        thousandsSeparator = '.',
        minimumDecimalPlaces = 2,
        maximumDecimalPlaces = 2,
        separator = "",
        currencySymbol = ""
    )
}

private fun formatNumber(number: BigDecimal, decimalSeparator: Char, thousandsSeparator: Char, minimumDecimalPlaces: Int, maximumDecimalPlaces: Int, currencySymbol: String, separator: String): String {

    // Set formatters
    val dfs = DecimalFormatSymbols()
    dfs.decimalSeparator = decimalSeparator
    dfs.groupingSeparator = thousandsSeparator
    val df = DecimalFormat()
    df.decimalFormatSymbols = dfs
    df.minimumFractionDigits = minimumDecimalPlaces
    df.maximumFractionDigits = maximumDecimalPlaces
    df.roundingMode = RoundingMode.HALF_UP

    // return formatted string
    return if (currencySymbol.isNotEmpty()) {
        currencySymbol + separator + df.format(number)
    } else {
        df.format(number)
    }
}