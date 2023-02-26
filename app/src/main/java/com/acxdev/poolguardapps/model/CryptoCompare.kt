package com.acxdev.poolguardapps.model

data class PriceMultiResponse(val RAW: Map<String, Map<String, Raw>>, val DISPLAY: Map<String, Map<String, Map<String, String>>>)

data class Raw(val TYPE: String, val MARKET: String, val FROMSYMBOL: String, val TOSYMBOL: String, val FLAGS: String, val PRICE: Double, val LASTUPDATE: Long,
               val MEDIAN: Double? = null, val LASTVOLUME: Double, val LASTVOLUMETO: Double, val LASTTRADEID: String, val VOLUMEDAY: Double, val VOLUMEDAYTO: Double,
               val VOLUME24HOUR: Double, val VOLUME24HOURTO: Double, val OPENDAY: Double, val HIGHDAY: Double, val LOWDAY: Double, val OPEN24HOUR: Double,
               val HIGH24HOUR: Double, val LOW24HOUR: Double, val LASTMARKET: String, val VOLUMEHOUR: Double, val VOLUMEHOURTO: Double, val OPENHOUR: Double,
               val HIGHHOUR: Double, val LOWHOUR: Double, val TOPTIERVOLUME24HOUR: Double, val TOPTIERVOLUME24HOURTO: Double, val CHANGE24HOUR: Double,
               val CHANGEPCT24HOUR: Double, val CHANGEDAY: Double, val CHANGEPCTDAY: Double, val CHANGEHOUR: Double, val CHANGEPCTHOUR: Double, val CONVERSIONTYPE: String,
               val CONVERSIONSYMBOL: String, val SUPPLY: Double, val MKTCAP: Double, val MKTCAPPENALTY: Long, val TOTALVOLUME24H: Double, val TOTALVOLUME24HTO: Double,
               val TOTALTOPTIERVOLUME24H: Double, val TOTALTOPTIERVOLUME24HTO: Double, val IMAGEURL: String)

data class NewsResponse(val Type: Long, val Message: String, val Promoted: List<Any?>, val Data: List<News>, val RateLimit: RateLimit, val HasWarning: Boolean)

class RateLimit

data class News(val id: String, val guid: String, val published_on: Long, val imageurl: String, val title: String, val url: String, val source: String, val body: String,
                val tags: String, val categories: String, val upvotes: String, val downvotes: String, val lang: String, val source_info: SourceInfo)

data class SourceInfo(val name: String, val lang: String, val img: String)

data class NewsCategoriesResponse(val categoryName: String, val wordsAssociatedWithCategory: List<String>, val excludedPhrases: List<String>, val includedPhrases: List<String>)