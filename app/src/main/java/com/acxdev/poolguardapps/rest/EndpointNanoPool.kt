package com.acxdev.poolguardapps.rest

import com.acxdev.poolguardapps.model.pool_response.GeneralInfo
import com.acxdev.poolguardapps.model.pool_response.NanopoolAverageHashRate
import com.acxdev.poolguardapps.model.pool_response.NanopoolData
import com.acxdev.poolguardapps.model.pool_response.NanopoolHashRateChart
import com.acxdev.poolguardapps.model.pool_response.NanopoolPayments
import retrofit2.Call
import retrofit2.http.GET
import retrofit2.http.Path

interface EndpointNanoPool {
    @GET("user/{wallet}")
    fun stats(@Path("wallet") wallet: String?): Call<GeneralInfo>

    @GET("reportedhashrate/{wallet}")
    fun reportedHashRate(@Path("wallet") wallet: String?): Call<NanopoolData>

    @GET("payments/{wallet}")
    fun payments(@Path("wallet") wallet: String?): Call<NanopoolPayments>

    @GET("hashratechart/{wallet}")
    fun reportedHashRateSharesChart(@Path("wallet") wallet: String?): Call<NanopoolHashRateChart>

    @GET("history/{wallet}")
    fun currentHashRateChart(@Path("wallet") wallet: String?): Call<NanopoolHashRateChart>

    @GET("hashratechart/{wallet}/{worker}")
    fun reportedHashRateSharesChartWorker(
        @Path("wallet") wallet: String?,
        @Path("worker") worker: String?
    ): Call<NanopoolHashRateChart>

    @GET("history/{wallet}/{worker}")
    fun currentHashRateChartWorker(
        @Path("wallet") wallet: String?,
        @Path("worker") worker: String?
    ): Call<NanopoolHashRateChart>

    @GET("avghashrate/{wallet}/{worker}")
    fun averageHashRateWorker(
        @Path("wallet") wallet: String?,
        @Path("worker") worker: String?
    ): Call<NanopoolAverageHashRate>

    @GET("hashrate/{wallet}/{worker}")
    fun currentHashRateWorker(
        @Path("wallet") wallet: String?,
        @Path("worker") worker: String?
    ): Call<NanopoolData>

    @GET("reportedhashrate/{wallet}/{worker}")
    fun reportedHashRateWorker(
        @Path("wallet") wallet: String?,
        @Path("worker") worker: String?
    ): Call<NanopoolData>
}