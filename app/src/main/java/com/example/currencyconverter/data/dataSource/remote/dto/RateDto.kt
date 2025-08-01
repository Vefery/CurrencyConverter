package com.example.currencyconverter.data.dataSource.remote.dto

import android.os.Parcelable
import kotlinx.parcelize.Parcelize
import kotlinx.serialization.Serializable

@Serializable
@Parcelize
data class RateDto(val currency: String, val value: Double) : Parcelable