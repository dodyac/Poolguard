package com.acxdev.poolguardapps.rest

import com.acxdev.poolguardapps.model.pool_response.BalanceEzil
import com.acxdev.poolguardapps.model.pool_response.ChartEzil
import com.acxdev.poolguardapps.model.pool_response.CurrentStatsEzil
import com.acxdev.poolguardapps.model.pool_response.PayoutEzil
import com.acxdev.poolguardapps.model.pool_response.WorkerEzil
import retrofit2.Call
import retrofit2.http.GET
import retrofit2.http.Path
import retrofit2.http.Query

interface EndpointEzil {

    @GET("historical_stats/{wallet}")
    fun chart(
        @Path("wallet") wallet: String?,
        @Query("time_from") time_from: String?,
        @Query("time_to") time_to: String?,
    ): Call<List<ChartEzil>>

    @GET("historical_stats/{wallet}/{worker}")
    fun chartWorker(
        @Path("wallet") wallet: String?,
        @Path("worker") worker: String?,
        @Query("time_from") time_from: String?,
        @Query("time_to") time_to: String?,
    ): Call<List<ChartEzil>>

    @GET("current_stats/{wallet}/reported")
    fun currentStats(
        @Path("wallet") wallet: String?
    ): Call<CurrentStatsEzil>

    @GET("current_stats/{wallet}/{worker}")
    fun currentStatsWorker(
        @Path("wallet") wallet: String?,
        @Path("worker") worker: String?,
    ): Call<CurrentStatsEzil>

    @GET("current_stats/{wallet}/workers")
    fun worker(
        @Path("wallet") wallet: String?,
    ): Call<List<WorkerEzil>>


    @GET("balances/{wallet}")
    fun balance(
        @Path("wallet") wallet: String?
    ): Call<BalanceEzil>

    @GET("withdrawals/{wallet}")
    fun payout(
        @Path("wallet") wallet: String?
    ): Call<PayoutEzil>
}