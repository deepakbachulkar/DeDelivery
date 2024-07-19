package com.demo.dedelivery.model

data class RequestLogs(
    val all: Int?=1,
    val app_version: String?= "1.0",
    val battery_level:String? = "100",
    val device_token: String?= "token",
    val device_type: String? = "android",
    val heading_angle: Int? = 180,
    var lat: Double? = 11.01276210,
    var long: Double? = 77.36301790,
    val on_route: String? = "y",
    var os_version: String? = "12",
)