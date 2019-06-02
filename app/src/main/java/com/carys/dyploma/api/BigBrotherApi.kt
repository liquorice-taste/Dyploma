package com.carys.dyploma.api

import com.carys.dyploma.dataModels.*
import io.reactivex.Observable
import okhttp3.OkHttpClient
import okhttp3.logging.HttpLoggingInterceptor
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

    @GET("api/lightcontroller/by_room/{roomid}")
    fun getLightControllers(
        @Header("Authorization") token: String,
        @Path("roomid") roomid: Int) :
            Observable<ResponseJSON<LightController>>

    @GET("api/sensor/")
    fun getSensors(
        @Header("Authorization") token: String) :
            Observable<ResponseJSON<Sensor>>

    @GET("api/sensor/{roomid}")
    fun getSensors(
        @Header("Authorization") token: String,
        @Path("roomid") roomid: Int) :
            Observable<ResponseJSON<Sensor>>

    @GET("api/room/by_sys/{sysid}")
    fun getRooms(
        @Header("Authorization") token: String,
        @Path("sysid") sysid: Int) :
            Observable<ResponseJSON<Room>>

    @FormUrlEncoded
    @POST("api/lightcontroller/brightness/")
    fun putLightControllers(
        @Header("Authorization") token: String,
        @Field("brightness") brightness: Int,
        @Field("controller") controller: Int) :
            Observable<ResponseJSON<LightController>>

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