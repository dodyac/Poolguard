package com.acxdev.poolguardapps.rest

import android.content.Context
import com.acxdev.commonFunction.common.Response
import com.acxdev.commonFunction.common.base.BaseNetworking.Companion.splitResponseUnsuccessful
import com.acxdev.commonFunction.common.base.BaseNetworking.Companion.whenLoaded
import com.acxdev.commonFunction.util.ext.getCompatActivity
import com.acxdev.commonFunction.util.toast
import com.acxdev.poolguardapps.common.Constant
import com.acxdev.poolguardapps.common.base.BaseUrl
import com.acxdev.poolguardapps.ui.dialog.DialogProgress
import okhttp3.OkHttpClient
import okhttp3.logging.HttpLoggingInterceptor
import retrofit2.Call
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory
import java.util.concurrent.TimeUnit

private val INTERCEPTOR_TYPE = HttpLoggingInterceptor.Level.BASIC

private fun retrofit(url: String): Retrofit {
    val httpClient = OkHttpClient.Builder()
        .addInterceptor(HttpLoggingInterceptor().setLevel(INTERCEPTOR_TYPE))
        .connectTimeout(30, TimeUnit.SECONDS)
        .writeTimeout(30, TimeUnit.SECONDS)
        .readTimeout(30, TimeUnit.SECONDS)
        .build()

    return Retrofit.Builder()
        .baseUrl(url)
        .addConverterFactory(GsonConverterFactory.create())
        .client(httpClient)
        .build()
}

fun fetchApi(url: String) = retrofit(url).create(EndpointCommon::class.java)
fun fetchApiBitFly(url: String) = retrofit(url).create(EndpointBitFly::class.java)
enum class EzilType {
    STATS, BILLING
}
fun fetchApiEzil(type: EzilType) = retrofit(if(type == EzilType.STATS) BaseUrl.Ezil.STATS else BaseUrl.Ezil.BILLING).create(EndpointEzil::class.java)
fun fetchApiFlexPool() = retrofit(BaseUrl.URL_FLEXPOOL).create(EndpointFlexPool::class.java)
fun fetchApiHiveOn() = retrofit(BaseUrl.URL_HIVEON).create(EndpointHiveOn::class.java)
fun fetchApiNanoPool(url: String) = retrofit(url).create(EndpointNanoPool::class.java)
fun fetchApiOtherPool(url: String = "http://localhost/") = retrofit(url).create(EndpointOtherPool::class.java)

fun <T> Call<T>.onLoaded(context: Context, showDialog: Boolean = false, result: T.() -> Unit) {
    if(showDialog) {
        val dialogProgress = DialogProgress()
        dialogProgress.show(context.getCompatActivity().supportFragmentManager, Constant.TAG_DIALOG)
        whenLoaded {
            when(response) {
                Response.SUCCESS -> {
                    dialogProgress.dismiss()
                    data?.let {
                        result.invoke(it)
                    } ?: run {
                        context.toast("error parsing class")
                    }
                }
                Response.UNSUCCESSFUL -> {
                    dialogProgress.dismiss()
                    string.onUnsuccessful(context)
                }
                Response.FAILURE -> {
                    dialogProgress.dismiss()
                    context.toast(string)
                }
            }
        }
    } else {
        whenLoaded {
            when(response) {
                Response.SUCCESS -> {
                    data?.let {
//                            try {
//                                val json  = JSONObject(it.toJson())
//                                val success = json.getBoolean("success")
//                                val message = json.getString("message")
//                                if(success) {
//                                    result.invoke(it)
//                                } else {
//                                    context.toast(message)
//                                }
//                            } catch (e: Exception) {
//                                result.invoke(it)
//                            }
                        result.invoke(it)
                    } ?: run {
                        context.toast("error parsing class")
                    }
                }
                Response.UNSUCCESSFUL -> {
                    string.onUnsuccessful(context)
                }
                Response.FAILURE -> {
                    context.toast(string)
                }
            }
        }
    }
}

private fun String.onUnsuccessful(context: Context) {
    splitResponseUnsuccessful { code, body ->
        when(code) {
            400 -> {
                try {
//                    val badRequest = body.toClass(BadRequestResponse::class.java)
//                    context.toast(badRequest.message)
                } catch (e: Exception) {
                    // handle failure to read error
                    e.printStackTrace()
                }
            }
            else -> {
                context.toast("Error Code $code")
            }
        }
    }
}