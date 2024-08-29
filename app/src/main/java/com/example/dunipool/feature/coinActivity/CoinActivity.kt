package com.example.dunipool.feature.coinActivity

import android.content.Intent
import android.net.Uri
import android.os.Bundle
import androidx.activity.enableEdgeToEdge
import androidx.appcompat.app.AppCompatActivity
import androidx.core.content.ContextCompat
import com.example.dunipool.R
import com.example.dunipool.apiManager.ApiManager
import com.example.dunipool.apiManager.model.CoinsInfo
import com.example.dunipool.apiManager.model.CoinAboutItem
import com.example.dunipool.databinding.ActivityCoinBinding
import com.example.dunipool.util.ALL
import com.example.dunipool.util.BASE_URL_TWITTER
import com.example.dunipool.util.HOUR
import com.example.dunipool.util.HOURS24
import com.example.dunipool.util.MONTH
import com.example.dunipool.util.MONTH3
import com.example.dunipool.util.WEEK
import com.example.dunipool.util.YEAR
import com.example.dunipool.util.showToast
import ir.dunijet.dunipool.apiManager.model.ChartData

class CoinActivity : AppCompatActivity() {
    private lateinit var binding: ActivityCoinBinding
    private lateinit var dataThisCoin: CoinsInfo.Data
    lateinit var dataThisCoinAbout: CoinAboutItem
    val apiManager = ApiManager()


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityCoinBinding.inflate(layoutInflater)
        enableEdgeToEdge()
        setContentView(binding.root)

        val fromIntent = intent.getBundleExtra("bundle")!!
        dataThisCoin = fromIntent.getParcelable("bundle1")!!

        if (fromIntent.getParcelable<CoinAboutItem> ("bundle2") != null) {
            dataThisCoinAbout = fromIntent.getParcelable("bundle2")!!
        }else {
            dataThisCoinAbout = CoinAboutItem()
        }

        binding.layoutToobar.Toolbar.title = dataThisCoin.coinInfo.fullName

        initUi()
        initAbout()
        
    }

    private fun initUi() {
        initChartData()
        initStatistics()
    }

    private fun initChartData() {

        var peroid: String = HOUR
        requestAndShowData(peroid)
        binding.layoutChart.radioGroupMain.setOnCheckedChangeListener { _, checkedId ->

            when (checkedId) {

                R.id.radio_12H -> { peroid = HOUR }

                R.id.radio_1D -> { peroid = HOURS24 }

                R.id.radio_1W -> { peroid = WEEK }

                R.id.radio_1M -> { peroid = MONTH }

                R.id.radio_3M -> { peroid = MONTH3 }

                R.id.radio_1Y -> { peroid = YEAR }

                R.id.radio_ALL -> { peroid = ALL }

            }

            requestAndShowData(peroid)

        }

        binding.layoutChart.txtPriceChart.text = dataThisCoin.dISPLAY.uSD.pRICE
        binding.layoutChart.txtChangePCT.text = dataThisCoin.dISPLAY.uSD.cHANGEPCT24HOUR
        binding.layoutChart.txtChangePrice.text = dataThisCoin.dISPLAY.uSD.cHANGE24HOUR

        val change = dataThisCoin.dISPLAY.uSD.cHANGEPCT24HOUR
        if (change > 0.toString()) {
            binding.layoutChart.txtUpDownChart.text = "▲"
            binding.layoutChart.txtUpDownChart.setTextColor(resources.getColor(R.color.colorGain))
            binding.layoutChart.Chart.lineColor = resources.getColor(R.color.colorGain)
            binding.layoutChart.txtChangePrice.setTextColor(resources.getColor(R.color.colorGain))
        } else if (change < 0.toString()) {
            binding.layoutChart.txtUpDownChart.text = "▼"
            binding.layoutChart.txtUpDownChart.setTextColor(resources.getColor(R.color.colorLoss))
            binding.layoutChart.Chart.lineColor = resources.getColor(R.color.colorLoss)
            binding.layoutChart.txtChangePrice.setTextColor(resources.getColor(R.color.colorLoss))
        } else {
            binding.layoutChart.txtUpDownChart.text = "—"
            binding.layoutChart.txtUpDownChart.setTextColor(resources.getColor(R.color.white))
            binding.layoutChart.Chart.lineColor = resources.getColor(R.color.white)
            binding.layoutChart.txtChangePrice.setTextColor(resources.getColor(R.color.white))
        }

        binding.layoutChart.Chart.setScrubListener {
            if (it == null) {
                binding.layoutChart.txtPriceChart.text = dataThisCoin.dISPLAY.uSD.pRICE
            }else {
                //show price this dot
                binding.layoutChart.txtPriceChart.text = "$" + (it as ChartData.Data).close
            }
        }


    }

   private fun requestAndShowData(peroid: String) {
        apiManager.getChartData(
            dataThisCoin.coinInfo.name,
            peroid,
            object : ApiManager.ApiCallback<Pair<List<ChartData.Data>, ChartData.Data?>> {
                override fun onSuccess(data: Pair<List<ChartData.Data>, ChartData.Data?>) {

                    val chartAdapter = ChartAdapter(data.first, data.second?.open.toString())

                    binding.layoutChart.Chart.adapter = chartAdapter


                }

                override fun onError(errorMessenger: String) {
                    showToast(errorMessenger)
                }

            })
    }

    private fun initAbout() {

        binding.layoutAbout.txtOpenWebsite.text = dataThisCoinAbout.coinWebsite
        binding.layoutAbout.txtOpenTwitter.text = dataThisCoinAbout.coinTwitter
        binding.layoutAbout.txtOpenReddit.text = dataThisCoinAbout.coinReddit
        binding.layoutAbout.txtOpenGithub.text = dataThisCoinAbout.coinGithub
        binding.layoutAbout.someData.text = dataThisCoinAbout.coinDescription

        binding.layoutAbout.txtOpenWebsite.setOnClickListener {
            openWebsite(dataThisCoinAbout.coinWebsite!!)
        }

        binding.layoutAbout.txtOpenTwitter.setOnClickListener {
            openWebsite(BASE_URL_TWITTER + dataThisCoinAbout.coinTwitter!!)
        }

        binding.layoutAbout.txtOpenReddit.setOnClickListener {
            openWebsite(dataThisCoinAbout.coinReddit!!)
        }

        binding.layoutAbout.txtOpenGithub.setOnClickListener {
            openWebsite(dataThisCoinAbout.coinGithub!!)
        }

    }

    private fun initStatistics() {

        binding.layoutStatistics.txtOpenAmount.text = dataThisCoin!!.dISPLAY.uSD.oPEN24HOUR
        binding.layoutStatistics.txtTodayHighAmount.text = dataThisCoin!!.dISPLAY.uSD.hIGH24HOUR
        binding.layoutStatistics.txtTodayLowAmount.text = dataThisCoin!!.dISPLAY.uSD.lOW24HOUR
        binding.layoutStatistics.txtChangeTodayAmount.text =
            dataThisCoin!!.dISPLAY.uSD.cHANGE24HOUR
        binding.layoutStatistics.txtAlgorithm.text = dataThisCoin!!.coinInfo.algorithm
        binding.layoutStatistics.txtTotalVolume.text = dataThisCoin!!.dISPLAY.uSD.tOTALVOLUME24H
        binding.layoutStatistics.txtMarketCapAmount.text = dataThisCoin.dISPLAY.uSD.mKTCAP
        binding.layoutStatistics.txtSupplyAmount.text = dataThisCoin.dISPLAY.uSD.sUPPLY

    }

    private fun openWebsite(url: String) {
        val intent = Intent(Intent.ACTION_VIEW, Uri.parse(url))
        startActivity(intent)
    }

}