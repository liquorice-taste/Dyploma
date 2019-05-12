package com.carys.dyploma.activities.dataModels

data class LightController(var lightType: String? = "",
                           var lightBrightness: Int = 0,
                           var lightId: Int = 0,
                           var lightName: String? = "",
                           var lightUuid: String? = "",
                           var lightPort: Int? = 0,
                           var lightStatus: String? = "",
                           var lightSystem: Int? = 0,
                           var lightRoom: String? = "")