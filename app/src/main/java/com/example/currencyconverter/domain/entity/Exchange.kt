package com.example.currencyconverter.domain.entity

import android.net.Uri
import android.os.Bundle
import android.os.Parcelable
import androidx.navigation.NavType
import com.example.currencyconverter.data.dataSource.remote.dto.RateDto
import kotlinx.parcelize.Parcelize
import kotlinx.serialization.Serializable
import kotlinx.serialization.json.Json

@Serializable
@Parcelize
data class Exchange(
    val targetRate: RateDto,
    val sourceRate: RateDto
) : Parcelable

val ExchangeNavType = object : NavType<Exchange>(isNullableAllowed = false) {
    override fun get(
        bundle: Bundle,
        key: String
    ): Exchange? {
        return Json.decodeFromString(bundle.getString(key) ?: return null)
    }

    override fun parseValue(value: String): Exchange {
        return Json.decodeFromString(Uri.decode(value))
    }

    override fun serializeAsValue(value: Exchange): String {
        return Uri.encode(Json.encodeToString(value))
    }

    override fun put(
        bundle: Bundle,
        key: String,
        value: Exchange
    ) {
        bundle.putString(key, Json.encodeToString(value))
    }

}