package com.example.currencyconverter.domain.entity

import com.example.currencyconverter.data.dataSource.remote.dto.RateDto

data class Exchange(
    val targetRate: RateDto,
    val sourceRate: RateDto
)