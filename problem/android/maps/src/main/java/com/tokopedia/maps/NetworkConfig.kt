package com.tokopedia.maps

import okhttp3.Interceptor
import okhttp3.OkHttpClient
import okhttp3.Request
import okhttp3.logging.HttpLoggingInterceptor
import retrofit2.Call
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory
import retrofit2.http.GET
import retrofit2.http.Header
import retrofit2.http.Query
import java.io.IOException

class NetworkConfig {
    fun getInterceptor() : OkHttpClient {
        val logging = HttpLoggingInterceptor()
        logging.level = HttpLoggingInterceptor.Level.BODY
        return OkHttpClient.Builder()
                .addInterceptor(logging)
                .addNetworkInterceptor { chain ->
                    val requestBuilder: Request.Builder = chain.request().newBuilder()
                    requestBuilder.header("x-rapidapi-key", "2c0918251emshef44790e872b3cdp1398d2jsna034dc9fe6e7")
                    requestBuilder.header("x-rapidapi-host", "geo-services-by-mvpc-com.p.rapidapi.com")
                    chain.proceed(requestBuilder.build())
                }
                .build()
    }
    fun getRetrofit() : Retrofit {
        return Retrofit.Builder()
                .baseUrl("https://geo-services-by-mvpc-com.p.rapidapi.com/")
                .client(getInterceptor())

                .addConverterFactory(GsonConverterFactory.create())
                .build()
    }
    fun getService() = getRetrofit().create(CountryInterface::class.java)
}

interface CountryInterface {
    @GET("countries?language=en")
    fun getCountryDetail(@Query("countrycode") locale: String): Call<CountryJSON>
}