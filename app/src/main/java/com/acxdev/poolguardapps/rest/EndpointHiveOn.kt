package com.acxdev.poolguardapps.rest

import com.acxdev.poolguardapps.model.pool_response.Billing
import com.acxdev.poolguardapps.model.pool_response.HashrateResponse
import com.acxdev.poolguardapps.model.pool_response.Overview
import com.acxdev.poolguardapps.model.pool_response.PayoutHiveOn
import com.acxdev.poolguardapps.model.pool_response.SharesResponse
import com.acxdev.poolguardapps.model.pool_response.WorkerResponse
import retrofit2.Call
import retrofit2.http.GET
import retrofit2.http.Path
import retrofit2.http.Query

interface EndpointHiveOn {
    @GET("stats/miner/{wallet}/{symbol}")
    fun overview(
        @Path("wallet") wallet: String?,
        @Path("symbol") symbol: String?,
    ): Call<Overview>

    @GET("stats/miner/{wallet}/{symbol}/workers")
    fun workers(
        @Path("wallet") wallet: String?,
        @Path("symbol") symbol: String?,
    ): Call<WorkerResponse>

    @GET("stats/miner/{wallet}/{symbol}/billing-acc")
    fun billing(
        @Path("wallet") wallet: String?,
        @Path("symbol") symbol: String?,
    ): Call<Billing>

    @GET("stats/hashrates")
    fun hashrateChart(
        @Query("minerAddress") minerAddress: String?,
        @Query("coin") symbol: String?,
        @Query("window") window: String? = "10m",
        @Query("limit") limit: String? = "144",
        @Query("worker") worker: String? = "",
    ): Call<HashrateResponse>

    @GET("stats/shares")
    fun sharesChart(
        @Query("minerAddress") minerAddress: String?,
        @Query("coin") symbol: String?,
        @Query("window") window: String? = "10m",
        @Query("limit") limit: String? = "144",
        @Query("worker") worker: String? = "",
    ): Call<SharesResponse>

    @GET("payouts/find")
    fun payouts(
        @Query("userAddress") userAddress: String?,
        @Query("coin") symbol: String?,
        @Query("sortBy") sortBy: String? = "-createdAt",
        @Query("type") type: String? = "miner_reward",
    ): Call<PayoutHiveOn>
}