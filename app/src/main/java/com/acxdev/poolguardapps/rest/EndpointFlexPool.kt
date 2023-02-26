package com.acxdev.poolguardapps.rest

import com.acxdev.poolguardapps.model.pool_response.History
import com.acxdev.poolguardapps.model.pool_response.Chart
import com.acxdev.poolguardapps.model.pool_response.Payments
import com.acxdev.poolguardapps.model.pool_response.Stats
import retrofit2.Call
import retrofit2.http.*

interface EndpointFlexPool {
    @GET("miner/stats")
    fun stats(
        @Query("coin") symbol: String?,
        @Query("address") address: String?
    ): Call<Stats>

    @GET("miner/chart")
    fun chart(
        @Query("coin") symbol: String?,
        @Query("address") address: String?
    ): Call<Chart>

    @GET("miner/balance")
    fun balance(
        @Query("coin") symbol: String?,
        @Query("address") address: String?
    ): Call<Stats>

    @GET("miner/workerCount")
    fun workerCount(
        @Query("coin") symbol: String?,
        @Query("address") address: String?
    ): Call<Stats>

    @GET("miner/workers")
    fun workers(
        @Query("coin") symbol: String?,
        @Query("address") address: String?
    ): Call<History>

    @GET("miner/payments")
    fun payments(
        @Query("coin") symbol: String?,
        @Query("address") address: String?,
        @Query("page") page: Long?
    ): Call<Payments>

    @GET("miner/stats")
    fun workerStats(
        @Query("coin") symbol: String?,
        @Query("address") address: String?,
        @Query("worker") worker: String?
    ): Call<Stats>

    @GET("miner/chart")
    fun workerChart(
        @Query("coin") symbol: String?,
        @Query("address") address: String?,
        @Query("worker") worker: String?
    ): Call<Chart>

    @FormUrlEncoded
    @POST("mine")
    fun special(
        @Field("coin") coin: String?,
        @Field("address") address: String?
    ): Call<Payments>
}