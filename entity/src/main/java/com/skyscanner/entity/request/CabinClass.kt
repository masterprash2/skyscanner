package com.skyscanner.challenge.entity.network.request

enum class CabinClass(val cabinClassName: String) {
    ECONOMY("economy"),
    PREMIUMECONOMY("premiumeconomy"),
    BUSINESS("business"),
    FIRST("first")
}