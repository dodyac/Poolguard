package com.acxdev.poolguardapps.ui.main.news

import android.os.Bundle
import android.view.View
import androidx.fragment.app.activityViewModels
import androidx.lifecycle.lifecycleScope
import com.acxdev.commonFunction.adapter.OnClick
import com.acxdev.commonFunction.util.ext.view.gone
import com.acxdev.commonFunction.util.ext.view.setHStack
import com.acxdev.commonFunction.util.ext.view.setVStack
import com.acxdev.commonFunction.util.ext.view.visible
import com.acxdev.poolguardapps.common.base.BaseFragment
import com.acxdev.poolguardapps.common.base.BaseUrl
import com.acxdev.poolguardapps.databinding.FragmentNewsBinding
import com.acxdev.poolguardapps.model.News
import com.acxdev.poolguardapps.model.NewsCategoriesResponse
import com.acxdev.poolguardapps.repository.Crypto
import com.acxdev.poolguardapps.rest.fetchApi
import com.acxdev.poolguardapps.rest.onLoaded
import com.acxdev.poolguardapps.ui.main.NewsViewModel
import com.acxdev.poolguardapps.ui.main.home.RowNews
import com.acxdev.poolguardapps.util.openWebView

class FragmentNews : BaseFragment<FragmentNewsBinding>(FragmentNewsBinding::inflate) {

    private val newsViewModel: NewsViewModel by activityViewModels()
    private lateinit var categories: RowCategories

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        savedInstanceState?.clear()
    }

    override fun FragmentNewsBinding.configureViews() {
        safeContext {
            fetchApi(BaseUrl.URL_CRYPTOCOMPARE).newsCategories().onLoaded(this) {
                val rowCategories = RowCategories(toMutableList(), object : OnClick<NewsCategoriesResponse>{
                    override fun onItemClick(item: NewsCategoriesResponse, position: Int) {
                        newsViewModel.getNews(item.categoryName)
                        resetNews()
                    }
                })

                categories.setHStack(rowCategories, hasFixed = true)

                val currentCategories = find { it.categoryName == Crypto.BTC.name }
                val currentCategoriesIndex = indexOf(currentCategories)

                categories.scrollToPosition(currentCategoriesIndex)
                rowCategories.selected = currentCategoriesIndex
            }

            lifecycleScope.launchWhenStarted {
                newsViewModel.newsResponse.collect {
                    if(it is NewsViewModel.NewsResponseEvent.Success) {
                        news.setVStack(RowNews(it.data,true, object : OnClick<News> {
                            override fun onItemClick(item: News, position: Int) {
                                openWebView(item.url)
                            }
                        }), hasFixed = true)
                    }
                }
            }
        }
    }

    override fun FragmentNewsBinding.onClickListener() {

    }

    private fun resetNews() {
        scopeLayout {
            news.showShimmer()
            layout.visible()
            disconnect.root.gone()
        }
    }
}