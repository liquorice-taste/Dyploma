package com.hetal.googleassistantsdkdemo.device

import android.os.Build
import androidx.annotation.RequiresApi
import com.carys.dyploma.voiceService.config.DeviceRegisterConf
import com.carys.dyploma.voiceService.device.Device
import com.carys.dyploma.voiceService.device.DeviceInterface
import com.carys.dyploma.voiceService.device.DeviceModel
import com.carys.dyploma.voiceService.exception.DeviceRegisterException
import com.google.gson.Gson
import com.google.gson.stream.JsonReader

import java.io.File
import java.io.FileReader
import java.io.FileWriter
import java.io.IOException
import java.util.Optional
import java.util.UUID

import org.slf4j.Logger
import org.slf4j.LoggerFactory

import okhttp3.OkHttpClient
import okhttp3.Request
import retrofit2.Response
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory

class DeviceRegister(// Configuration from typesafe
    private val deviceRegisterConf: DeviceRegisterConf, accessToken: String
) {

    var deviceModel: DeviceModel? = null
        private set

    var device: Device? = null
        private set

    // The API interface (used by retrofit)
    private val deviceInterface: DeviceInterface

    // The Gson object to read/write the device model and instance in a file
    private val gson: Gson = Gson()

    init {

        // Add an interceptor to add our accessToken in the queries
        val client = OkHttpClient.Builder().addInterceptor { chain ->
            val newRequest = chain.request().newBuilder()
                .addHeader("Authorization", "Bearer $accessToken")
                .build()
            chain.proceed(newRequest)
        }.build()

        val retrofit = Retrofit.Builder()
            .client(client)
            .baseUrl(deviceRegisterConf.getApiEndpoint())
            .addConverterFactory(GsonConverterFactory.create())
            .build()

        deviceInterface = retrofit.create(DeviceInterface::class.java!!)
    }

    @RequiresApi(Build.VERSION_CODES.N)
    @Throws(Throwable::class)
    fun register() {
        val projectId = deviceRegisterConf.projectId
        // Register the device model
        deviceModel = registerModel(projectId)
            .orElseThrow<Throwable> { DeviceRegisterException("Unable to register the device model") }
        // Now we can register the instance
        device = registerInstance(projectId, deviceModel!!.deviceModelId)
            .orElseThrow<Throwable> { DeviceRegisterException("Unable to register the device instance") }
    }

    @RequiresApi(Build.VERSION_CODES.N)
    @Throws(DeviceRegisterException::class)
    private fun registerModel(projectId: String): Optional<DeviceModel> {
        val optionalDeviceModel = readFromFile(deviceRegisterConf.deviceModelFilePath, DeviceModel::class.java)
        if (optionalDeviceModel.isPresent) {
            LOGGER.info("Got device model from file")
            return optionalDeviceModel
        }

        // If we can't get the device model from a file, continue with the webservice
        val modelId = projectId + UUID.randomUUID()

        val manifest = DeviceModel.Manifest()
        manifest.manufacturer = "mautini"
        manifest.productName = "Assistant SDK Demo"
        manifest.deviceDescription = "Assistant SDK Demo in Java"

        val deviceModel = DeviceModel()
        deviceModel.deviceModelId = modelId
        deviceModel.projectId = projectId
        deviceModel.name = String.format("projects/%s/deviceModels/%s", projectId, modelId)
        // https://developers.google.com/assistant/sdk/reference/device-registration/model-and-instance-schemas#device_model_json
        // Light does not fit this project but there is nothing better in the API
        deviceModel.deviceType = "action.devices.types.LIGHT"
        deviceModel.manifest = manifest

        try {
            LOGGER.info("Creating device model")
            val response = deviceInterface.registerModel(projectId, deviceModel).execute()
            if (response.isSuccessful && response.body() != null) {
                // Save the device model in a file to not request the api each time we start the project
                FileWriter(deviceRegisterConf.deviceModelFilePath).use { writer ->
                    gson.toJson(
                        response.body(),
                        writer
                    )
                }
                return Optional.of<DeviceModel>(response.body()!!)
            } else {
                return Optional.empty<DeviceModel>()
            }
        } catch (e: IOException) {
            throw DeviceRegisterException("Error during registration of the device model", e)
        }

    }

    @RequiresApi(Build.VERSION_CODES.N)
    @Throws(DeviceRegisterException::class)
    private fun registerInstance(projectId: String, modelId: String): Optional<Device> {
        val optionalDevice = readFromFile(deviceRegisterConf.getDeviceInstanceFilePath(), Device::class.java)
        if (optionalDevice.isPresent()) {
            LOGGER.info("Got device instance from file")
            return optionalDevice
        }

        val device = Device()
        device.setId(UUID.randomUUID().toString())
        device.setModelId(modelId)
        // Here we use the Google Assistant Service
        device.clientType = "SDK_SERVICE"

        try {
            LOGGER.info("Creating device instance")
            val response = deviceInterface.registerDevice(projectId, device).execute()
            if (response.isSuccessful && response.body() != null) {
                // Save the device instance in a file to not request the api each time we start the project
                FileWriter(deviceRegisterConf.getDeviceInstanceFilePath()).use { writer ->
                    gson.toJson(
                        response.body(),
                        writer
                    )
                }
                return Optional.of<Device>(response.body()!!)
            } else {
                return Optional.empty<Device>()
            }
        } catch (e: IOException) {
            throw DeviceRegisterException("Error during registration of the device instance", e)
        }

    }

    /**
     * Deserialize from json an object in a file
     *
     * @param filePath    the file in which we stored the object to deserialize
     * @param targetClass the target class for the deserialization
     * @param <T>         the type of the object to deserialize
     * @return an optional with the object deserialized if the process succeed
    </T> */
    @RequiresApi(Build.VERSION_CODES.N)
    private fun <T> readFromFile(filePath: String, targetClass: Class<T>): Optional<T> {
        val file = File(filePath)
        if (file.exists()) {
            try {
                return Optional.of(gson.fromJson(JsonReader(FileReader(file)), targetClass))
            } catch (e: IOException) {
                LOGGER.warn("Unable to read the content of the file", e)
            }

        }
        return Optional.empty()
    }

    companion object {

        private val LOGGER = LoggerFactory.getLogger(DeviceRegister::class.java)
    }
}