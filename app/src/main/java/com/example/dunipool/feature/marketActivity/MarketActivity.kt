package com.example.dunipool.feature.marketActivity

import android.content.Intent
import android.net.Uri
import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView.VERTICAL
import com.example.dunipool.apiManager.ApiManager
import com.example.dunipool.apiManager.model.CoinAboutData
import com.example.dunipool.apiManager.model.CoinsInfo
import com.example.dunipool.apiManager.model.CoinAboutItem
import com.example.dunipool.databinding.ActivityMarketBinding
import com.example.dunipool.feature.coinActivity.CoinActivity
import com.example.dunipool.util.showToast
import com.google.gson.Gson

class MarketActivity : AppCompatActivity(), MarketAdapter.RecyclerCallBack {
    lateinit var binding: ActivityMarketBinding
    val apiManager = ApiManager()
    lateinit var dataNews: ArrayList<Pair<String, String>>
    lateinit var aboutDataMap: MutableMap<String, CoinAboutItem>

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityMarketBinding.inflate(layoutInflater)
        setContentView(binding.root)

        iniUi()

        binding.swipeRefreshMain.setOnRefreshListener {

            iniUi()

            binding.swipeRefreshMain.isRefreshing = false

        }

        binding.layoutWatchlist.BtnShowMore.setOnClickListener {
            val intent = Intent(
                Intent.ACTION_VIEW,
                Uri.parse("https://www.cryptocompare.com/coins/list/all/USD")
            )
            startActivity(intent)
        }

    }

    override fun onResume() {
        super.onResume()
        iniUi()
    }

    private fun iniUi() {

        getNewsFromApi()
        getCoins()
        getDataAboutCoin()

    }

    private fun getNewsFromApi() {

        apiManager.getNews(object : ApiManager.ApiCallback<ArrayList<Pair<String, String>>> {
            override fun onSuccess(data: ArrayList<Pair<String, String>>) {
                dataNews = data
                refreshNews()
            }

            override fun onError(errorMessenger: String) {
                showToast(errorMessenger)
            }

        })

    }

    private fun getCoins() {

        apiManager.getCoins(object : ApiManager.ApiCallback<List<CoinsInfo.Data>> {
            override fun onSuccess(data: List<CoinsInfo.Data>) {
                showDataInRecycler(cleanData(data))
            }

            override fun onError(errorMessenger: String) {
                showToast(errorMessenger)
            }

        })

    }

    private fun showDataInRecycler(data: List<CoinsInfo.Data>) {

        val marketAdapter = MarketAdapter(ArrayList(data), this)
        binding.layoutWatchlist.recyclerViewMain.adapter = marketAdapter
        binding.layoutWatchlist.recyclerViewMain.layoutManager =
            LinearLayoutManager(this, VERTICAL, false)
    }

    private fun refreshNews() {
        val randomNews = (0..49).random()
        binding.layoutNews.txtNews.text = dataNews[randomNews].first
        binding.layoutNews.txtNews.setOnClickListener {
            refreshNews()
        }
        binding.layoutNews.imgNews.setOnClickListener {
            val intetn = Intent(Intent.ACTION_VIEW, Uri.parse(dataNews[randomNews].second))
            startActivity(intetn)
        }
    }

    override fun onCoinItemClicked(dataCoin: CoinsInfo.Data) {

        val intent = Intent(this, CoinActivity::class.java)
        val bundle = Bundle()
        bundle.putParcelable("bundle1" , dataCoin)
        bundle.putParcelable("bundle2" , aboutDataMap[dataCoin.coinInfo.name])
        intent.putExtra("bundle" , bundle)
        startActivity(intent)
    }

    private fun cleanData(data: List<CoinsInfo.Data>): List<CoinsInfo.Data> {

        val newData = mutableListOf<CoinsInfo.Data>()
        data.forEach {
            if (it.dISPLAY != null) {
                newData.add(it)
            }
        }
        return newData
    }

    private fun getDataAboutCoin() {

        val fileString = applicationContext.assets
            .open("currencyinfo.json")
            .bufferedReader()
            .use { it.readText() }

        val json = Gson()
        val dataAbout = json.fromJson(fileString, CoinAboutData::class.java)
        aboutDataMap = mutableMapOf()
        dataAbout.forEach {

            aboutDataMap[it.currencyName] = CoinAboutItem(
                it.info.web,
                it.info.twt,
                it.info.reddit,
                it.info.github,
                it.info.desc
            )
        }
    }
}