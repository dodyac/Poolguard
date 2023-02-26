package com.acxdev.poolguardapps.rest

import com.acxdev.poolguardapps.model.pool_response.RavenMiner
import com.acxdev.poolguardapps.model.pool_response.RavenPool
import com.acxdev.poolguardapps.model.pool_response.FlockpoolDashboard
import com.acxdev.poolguardapps.model.pool_response.FlockpoolPaymentsResponse
import com.acxdev.poolguardapps.model.pool_response.HeroMinersResponse
import com.acxdev.poolguardapps.model.pool_response.K1PoolResponse
import com.acxdev.poolguardapps.model.pool_response.LuckPoolHashrateResponse
import com.acxdev.poolguardapps.model.pool_response.LuckPoolResponse
import com.acxdev.poolguardapps.model.pool_response.MineXmrBalance
import com.acxdev.poolguardapps.model.pool_response.MineXmrHashrateChart
import com.acxdev.poolguardapps.model.pool_response.MineXmrPayments
import com.acxdev.poolguardapps.model.pool_response.MineXmrWorkers
import com.acxdev.poolguardapps.model.pool_response.MinerpoolResponse
import com.acxdev.poolguardapps.model.pool_response.MinerResponse
import com.acxdev.poolguardapps.model.pool_response.TwoMinersChart
import com.acxdev.poolguardapps.model.pool_response.UnMineablePayments
import com.acxdev.poolguardapps.model.pool_response.UnMineableStats
import com.acxdev.poolguardapps.model.pool_response.UnMineableWorker
import com.acxdev.poolguardapps.model.pool_response.WoolyPoolyResponse
import retrofit2.Call
import retrofit2.http.GET
import retrofit2.http.Path
import retrofit2.http.Query
import retrofit2.http.Url

interface EndpointOtherPool {
    @GET
    fun minerStatsMain(
        @Url url: String?
    ): Call<MinerResponse>

    @GET("accounts/{wallet}")
    fun woolyPooly(@Path("wallet") wallet: String?): Call<WoolyPoolyResponse>

    @GET("stats_address")
    fun heroMiners(@Query("address") wallet: String?): Call<HeroMinersResponse>

    @GET("accounts/{wallet}/hashrates/5m")
    fun twoMinersChart(@Path("wallet") wallet: String?): Call<TwoMinersChart>

    @GET("{wallet}")
    fun k1Pool(@Path("wallet") wallet: String?): Call<K1PoolResponse>

    @GET("wallet/{wallet}")
    fun ravenMiner(@Path("wallet") wallet: String?): Call<RavenMiner>

    @GET("worker_stats?{wallet}")
    fun ravenPool(@Query("wallet") wallet: String?): Call<RavenPool>

    @GET("worker_stats")
    fun minerPool(
        @Query("address") address: String?,
        @Query("window_days") window_days: Int?
    ): Call<MinerpoolResponse>

    @GET("account/{wallet}/payments")
    fun unMineablePayments(
        @Path("wallet") wallet: String?
    ): Call<UnMineablePayments>

    @GET("address/{wallet}")
    fun unMineableStats(
        @Path("wallet") wallet: String?,
        @Query("coin") coin: String?
    ): Call<UnMineableStats>

    @GET("account/{uuid}/workers")
    fun unMineableWorker(
        @Path("uuid") uuid: String?,
    ): Call<UnMineableWorker>

    @GET("main/user/stats")
    fun mineXmrBalance(@Query("address") wallet: String?): Call<MineXmrBalance>

    @GET("history/user/summary")
    fun mineXmrChart(
        @Query("days") days: Int?,
        @Query("address") wallet: String?
    ): Call<MineXmrHashrateChart>

    @GET("main/user/workers")
    fun mineXmrWorker(@Query("address") wallet: String?): Call<List<MineXmrWorkers>>

    @GET("main/user/payments")
    fun mineXmrPayments(@Query("address") wallet: String?): Call<MineXmrPayments>

    @GET("miner/{wallet}")
    fun luckPool(@Path("wallet") wallet: String?): Call<LuckPoolResponse>

    @GET("payments/{wallet}")
    fun luckPoolPayments(@Path("wallet") wallet: String?): Call<List<String>>

    @GET("miner/history/{wallet}")
    fun luckPoolChart(@Path("wallet") wallet: String?): Call<List<LuckPoolHashrateResponse>>

    @GET("v1/wallets/rtm/{wallet}")
    fun flockPoolDashboard(@Path("wallet") wallet: String?): Call<FlockpoolDashboard>

    @GET("v1/wallets/rtm/{wallet}/payments")
    fun flockPoolPayments(@Path("wallet") wallet: String?): Call<FlockpoolPaymentsResponse>
}