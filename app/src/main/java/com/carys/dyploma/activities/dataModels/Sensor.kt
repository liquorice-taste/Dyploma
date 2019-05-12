package com.carys.dyploma.activities.dataModels

data class DeviceList(var type: String?, var devices: ArrayList<Sensor>?)

data class Sensor(var id: Int? = 0,
                  var name: String?,
                  var uuid: String? = "",
                  var port: Int? = 0,
                  var status: String? = "",
                  var current_data: Int? = 0,
                  var unit_of_measure: String? = "",
                  var system: Int? = 0,
                  var room: String?)