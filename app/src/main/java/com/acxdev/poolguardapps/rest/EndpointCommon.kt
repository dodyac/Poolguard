package com.acxdev.poolguardapps.rest

import com.acxdev.poolguardapps.model.Chia
import com.acxdev.poolguardapps.model.NewsCategoriesResponse
import com.acxdev.poolguardapps.model.NewsResponse
import com.acxdev.poolguardapps.model.PriceMultiResponse
import com.acxdev.poolguardapps.model.Whattomine
import com.acxdev.poolguardapps.model.coin.CoinStats
import retrofit2.Call
import retrofit2.Response
import retrofit2.http.GET
import retrofit2.http.Path
import retrofit2.http.Query

interface EndpointCommon {

    @GET("coins/{code}.json")
    fun coinStats(@Path("code") code: Int?): Call<Whattomine>

    @GET("price")
    fun getCryptoCompare(
        @Query("fsym") symbol: String?,
        @Query("tsyms") toWhat: String?
    ): Call<Map<String, Double>>

    @GET("netspace")
    fun chiaNetspace(): Call<Chia>

    @GET("pricemultifull")
    fun getPriceMultiFull(
        @Query("fsyms") symbol: String?,
        @Query("tsyms") toWhat: String?
    ): Call<PriceMultiResponse>

    @GET("charts")
    fun charts(
        @Query("period") period: String?,
        @Query("coinId") coinId: String?
    ): Call<CoinStats>

    @GET("v2/news/")
    fun getNews(
        @Query("lang") lang: String?,
        @Query("categories") categories: String?
    ): Call<NewsResponse>

    @GET("news/categories")
    fun newsCategories(): Call<List<NewsCategoriesResponse>>

    @GET("coins/{code}.json")
    fun gpuCalculator(
        @Path("code") code: Int?,
        @Query("hr") hashRate: Double?,
        @Query("p") power: Double?,
        @Query("cost") cost: Double?,
        @Query("span_br") blockReward: String?,
        @Query("span_d") difficulty: String?,
    ): Call<Whattomine>
}