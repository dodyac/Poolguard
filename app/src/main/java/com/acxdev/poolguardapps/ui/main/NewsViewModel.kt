package com.acxdev.poolguardapps.ui.main

import android.app.Application
import androidx.lifecycle.AndroidViewModel
import com.acxdev.poolguardapps.common.base.BaseUrl
import com.acxdev.poolguardapps.model.News
import com.acxdev.poolguardapps.rest.fetchApi
import com.acxdev.poolguardapps.rest.onLoaded
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow

class NewsViewModel(app: Application) : AndroidViewModel(app) {

    sealed class NewsResponseEvent {
        class Success(val data: MutableList<News>) : NewsResponseEvent()
        object Loading : NewsResponseEvent()
    }

    private val _newsResponse = MutableStateFlow<NewsResponseEvent>(NewsResponseEvent.Loading)
    val newsResponse: StateFlow<NewsResponseEvent> = _newsResponse

    fun getNews(categories: String) {
        _newsResponse.value = NewsResponseEvent.Loading
        fetchApi(BaseUrl.URL_CRYPTOCOMPARE).getNews("EN", categories).onLoaded(getApplication<Application>()) {
            _newsResponse.value = NewsResponseEvent.Success(Data.toMutableList())
        }
    }
}