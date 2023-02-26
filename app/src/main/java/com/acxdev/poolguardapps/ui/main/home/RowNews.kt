package com.acxdev.poolguardapps.ui.main.home

import com.acxdev.commonFunction.adapter.BaseAdapterLib
import com.acxdev.commonFunction.adapter.OnClick
import com.acxdev.poolguardapps.common.Constant
import com.acxdev.poolguardapps.databinding.RowNewsBinding
import com.acxdev.poolguardapps.model.News
import com.acxdev.poolguardapps.util.imageUrl
import com.acxdev.poolguardapps.util.toTimesAgo

class RowNews(
    private val list: MutableList<News>,
    private val isViewAll: Boolean,
    private val onClick: OnClick<News>
) :
    BaseAdapterLib<RowNewsBinding, News>(RowNewsBinding::inflate, list) {

    override fun ViewHolder<RowNewsBinding>.bind(item: News) {
        scopeLayout {
            title.text = item.title
            image.imageUrl(item.imageurl)
            date.text = item.published_on.toTimesAgo(context)

            card.setOnClickListener {
                onClick.onItemClick(item, adapterPosition)
            }
        }
    }

    override fun getItemCount(): Int =
        if (!isViewAll && list.size > Constant.DefaultValue.MAX_NEWS) {
            Constant.DefaultValue.MAX_NEWS
        } else {
            list.size
        }

}