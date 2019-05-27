package com.carys.dyploma.activities.api

import com.carys.dyploma.activities.SharedUtils
import com.carys.dyploma.activities.dataModels.*
import io.reactivex.Observable
import io.reactivex.Single
import okhttp3.OkHttpClient
import okhttp3.logging.HttpLoggingInterceptor
import org.json.JSONObject
import org.reactivestreams.Subscriber
import retrofit2.Call
import retrofit2.Response
import retrofit2.Retrofit
import retrofit2.adapter.rxjava2.RxJava2CallAdapterFactory
import retrofit2.converter.gson.GsonConverterFactory
import retrofit2.http.*



interface BigBrotherApi {

    @GET("api/system/")
    fun getSystems(
        @Header("Authorization") token: String) :
            Observable<ResponseJSON<HomeSystem>>

    @GET("api/lightcontroller/")
    fun getLightControllers(
        @Header("Authorization") token: String) :
            Observable<ResponseJSON<LightController>>

    @GET("api/room/by_sys/{sysid}")
    fun getRooms(
        @Header("Authorization") token: String,
        @Path("sysid") sysid: Int) :
            Observable<ResponseJSON<Room>>

    @POST("api/lightcontroller={controller}/brightness={brightness}")
    fun putLightControllers(
        @Header("Authorization") token: String,
        @Path("brightness") brightness: Int,
        @Path("controller") controller: Int) :
            Observable<ResponseJSON<Responset>>

    @GET("api/sensor/")
    fun getSensors(
        @Header("Authorization") token: String) :
            Observable<ResponseJSON<Sensor>>

    @POST("api/token/")
    fun getToken(
        @Body credentials: Credentials):
            Observable<Token>


    companion object {
        private val URL = "http://188.32.136.71:8000/"
        fun create(): BigBrotherApi {

            val logging = HttpLoggingInterceptor()
                .setLevel(HttpLoggingInterceptor.Level.BODY)
            val httpClient = OkHttpClient.Builder()
// add your other interceptors …
// add logging as last interceptor
            httpClient.addInterceptor(logging)

            val retrofit = Retrofit.Builder()
                .addCallAdapterFactory(RxJava2CallAdapterFactory.create())
                .addConverterFactory(GsonConverterFactory.create())
                .baseUrl(URL)
                .client(httpClient.build())
                .build()

            return retrofit.create(BigBrotherApi::class.java)
        }
    }
}