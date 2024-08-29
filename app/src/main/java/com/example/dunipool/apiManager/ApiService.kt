package com.example.dunipool.apiManager



import com.example.dunipool.apiManager.model.CoinsInfo
import com.example.dunipool.apiManager.model.NewsData
import com.example.dunipool.util.API_KEY
import com.example.dunipool.util.BASE_URL
import ir.dunijet.dunipool.apiManager.model.ChartData
import retrofit2.Call
import retrofit2.http.GET
import retrofit2.http.Headers
import retrofit2.http.PATCH
import retrofit2.http.Path
import retrofit2.http.Query
import java.time.Period

interface ApiService {

    @Headers(API_KEY)
    @GET("v2/news/")
    fun getNews(
        @Query("sortOrder") sortOrder :String = "Latest"
    ):Call<NewsData>


    @Headers(API_KEY)
    @GET("top/totalvolfull")
    fun getCoins(
        @Query("tsym") tsym :String = "USD" ,
        @Query("limit") limit_data :Int = 40
    ):Call<CoinsInfo>

    @Headers(API_KEY)
    @GET("{period}")
    fun getChartData(
        @Path("period") period: String ,
        @Query("fsym") fromSymbol :String ,
        @Query("limit") limit :Int ,
        @Query("aggregate")  aggregate :Int,
        @Query("tsym") toSymbol :String = "USD"
    ) :Call<ChartData>
}