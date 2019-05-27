package com.carys.dyploma.activities.dataModels

data class LightController(val type: String? = "",
                           val brightness: Int = 0,
                           val id: Int = 0,
                           val name: String? = "",
                           val uuid: String? = "",
                           val port: Int? = 0,
                           val status: String? = "",
                           val room: String? = "")