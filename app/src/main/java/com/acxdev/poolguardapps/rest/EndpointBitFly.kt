package com.acxdev.poolguardapps.rest

import com.acxdev.poolguardapps.model.pool_response.CurrentStats
import com.acxdev.poolguardapps.model.pool_response.Dashboard
import com.acxdev.poolguardapps.model.pool_response.History
import com.acxdev.poolguardapps.model.pool_response.Payout
import com.acxdev.poolguardapps.model.pool_response.SettingsParent
import com.acxdev.poolguardapps.model.pool_response.Workers
import retrofit2.Call
import retrofit2.http.GET
import retrofit2.http.Path

interface EndpointBitFly {
    @GET("miner/{wallet}/dashboard")
    fun dashboard(@Path("wallet") wallet: String?): Call<Dashboard>

    @GET("miner/{wallet}/currentStats")
    fun currentStats(@Path("wallet") wallet: String?): Call<CurrentStats>

    @GET("miner/{wallet}/settings")
    fun settings(@Path("wallet") wallet: String?): Call<SettingsParent>

    @GET("miner/{wallet}/history")
    fun history(@Path("wallet") wallet: String?): Call<History>

    @GET("miner/{wallet}/workers")
    fun workers(@Path("wallet") wallet: String?): Call<Workers>

    @GET("miner/{wallet}/worker/{worker}/history")
    fun workerHistory(
        @Path("wallet") wallet: String?,
        @Path("worker") worker: String?
    ): Call<Workers>

    @GET("miner/{wallet}/worker/{worker}/currentStats")
    fun workerCurrentStats(
        @Path("wallet") wallet: String?,
        @Path("worker") worker: String?
    ): Call<CurrentStats>

    @GET("miner/{wallet}/payouts")
    fun payout(@Path("wallet") wallet: String?): Call<Payout>
}