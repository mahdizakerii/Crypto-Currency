package com.example.dunipool.apiManager

import com.example.dunipool.apiManager.model.CoinsInfo
import com.example.dunipool.apiManager.model.NewsData
import com.example.dunipool.util.ALL
import com.example.dunipool.util.BASE_URL
import com.example.dunipool.util.MONTH3
import com.example.dunipool.util.HISTO_DAY
import com.example.dunipool.util.HISTO_HOUR
import com.example.dunipool.util.HISTO_MINUTE
import com.example.dunipool.util.HOUR
import com.example.dunipool.util.HOURS24
import com.example.dunipool.util.MONTH
import com.example.dunipool.util.WEEK
import com.example.dunipool.util.YEAR
import ir.dunijet.dunipool.apiManager.model.ChartData
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory

class ApiManager {

    private var apiService: ApiService

    init {

        val retrofit = Retrofit
            .Builder()
            .baseUrl(BASE_URL)
            .addConverterFactory(GsonConverterFactory.create())
            .build()

        apiService = retrofit.create(ApiService::class.java)

    }

    fun getNews(apiCallback: ApiCallback<ArrayList<Pair<String, String>>>) {
        apiService.getNews().enqueue(object : Callback<NewsData> {
            override fun onResponse(call: Call<NewsData>, response: Response<NewsData>) {
                val data = response.body()!!
                val dataNews = ArrayList<Pair<String, String>>()

                data.data.forEach {
                    dataNews.add(Pair(it.title, it.url))
                }
                apiCallback.onSuccess(dataNews)
            }

            override fun onFailure(call: Call<NewsData>, t: Throwable) {
                apiCallback.onError(t.message!!)
            }

        })
    }

    fun getCoins(apiCallback: ApiCallback<List<CoinsInfo.Data>>) {
        apiService.getCoins().enqueue(object : Callback<CoinsInfo> {
            override fun onResponse(call: Call<CoinsInfo>, response: Response<CoinsInfo>) {
                val data = response.body()!!
                apiCallback.onSuccess(data.data)
            }

            override fun onFailure(call: Call<CoinsInfo>, t: Throwable) {
                apiCallback.onError(t.message!!)
            }

        })
    }

    fun getChartData(symbol: String, period: String, apiCallback: ApiCallback<Pair<  List<ChartData.Data>, ChartData.Data? >>) {

        var histoPeriod: String = ""
        var limit = 30
        var aggregate = 1

        when(period) {
            HOUR -> {
                histoPeriod = HISTO_MINUTE
                limit = 60
                aggregate = 12
            }
            HOURS24 -> {
                histoPeriod = HISTO_HOUR
                limit = 24
            }
            WEEK -> {
                histoPeriod = HISTO_HOUR
                aggregate = 6
            }
            MONTH -> {
                histoPeriod = HISTO_DAY
                aggregate = 30
            }
            MONTH3 -> {
                histoPeriod = HISTO_DAY
                limit = 90
            }
            YEAR -> {
                histoPeriod = HISTO_DAY
                aggregate = 13
            }
            ALL -> {
                histoPeriod = HISTO_DAY
                aggregate = 30
                limit = 2000
            }
        }

        apiService.getChartData(histoPeriod , symbol , limit , aggregate)
            .enqueue( object : Callback<ChartData> {
                override fun onResponse(call: Call<ChartData>, response: Response<ChartData>) {

                    val dataFul = response.body()!!
                    val data1 = dataFul.data
                    val data2 = data1.maxByOrNull { it.close.toFloat() }
                    val returningData = Pair(data1 , data2)
                    apiCallback.onSuccess(returningData)
                }

                override fun onFailure(call: Call<ChartData>, t: Throwable) {
                    apiCallback.onError(t.message!!)
                }

            })
    }

    interface ApiCallback<T> {

        fun onSuccess(data: T)
        fun onError(errorMessenger: String)

    }

}