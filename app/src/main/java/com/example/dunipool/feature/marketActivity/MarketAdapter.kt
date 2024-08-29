package com.example.dunipool.feature.marketActivity

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.core.content.ContextCompat
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import com.bumptech.glide.load.engine.DiskCacheStrategy
import com.example.dunipool.R
import com.example.dunipool.apiManager.model.CoinsInfo
import com.example.dunipool.databinding.ItemRecyclerMarketBinding
import com.example.dunipool.util.BASE_URL_Image

class MarketAdapter(private var data: ArrayList<CoinsInfo.Data> , private val recyclerCallBack: RecyclerCallBack) :
    RecyclerView.Adapter<MarketAdapter.MarketViewHolder>() {

    lateinit var binding: ItemRecyclerMarketBinding

    inner class MarketViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {

        fun bindViews(dataCoin : CoinsInfo.Data) {

            if (dataCoin.dISPLAY != null ) {

                binding.txtCoinName.text = dataCoin.coinInfo.fullName
                binding.txtPrice.text = dataCoin.dISPLAY.uSD.pRICE

                val txtChange = dataCoin.dISPLAY.uSD.cHANGEPCT24HOUR
                if (txtChange > 0.toString()) {
                    binding.txtChange.setTextColor(ContextCompat.getColor(binding.root.context ,R.color.colorGain))
                    binding.txtChange.text = "$" + txtChange
                } else if (txtChange < 0.toString()){
                    binding.txtChange.setTextColor(ContextCompat.getColor(binding.root.context ,R.color.colorLoss))
                    binding.txtChange.text = "$" + txtChange
                }else {
                    binding.txtChange.text = "$" + txtChange
                }

                binding.txtMarketCap.text = dataCoin.dISPLAY.uSD.mKTCAP

                Glide
                    .with(itemView)
                    .load(BASE_URL_Image + dataCoin.coinInfo.imageUrl)
                    .skipMemoryCache(false)
                    .diskCacheStrategy(DiskCacheStrategy.AUTOMATIC)
                    .into(binding.imgItemMarket)

                itemView.setOnClickListener {
                    recyclerCallBack.onCoinItemClicked(dataCoin)
                }

            }

        }
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): MarketViewHolder {
        val infalter = LayoutInflater.from(parent.context)
        binding = ItemRecyclerMarketBinding.inflate(infalter , parent , false)
        return MarketViewHolder(binding.root)
    }

    override fun getItemCount(): Int = data.size

    override fun onBindViewHolder(holder: MarketViewHolder, position: Int) {
        holder.bindViews(data[position])

    }

    interface RecyclerCallBack {
        fun onCoinItemClicked(dataCoin: CoinsInfo.Data)
    }
}