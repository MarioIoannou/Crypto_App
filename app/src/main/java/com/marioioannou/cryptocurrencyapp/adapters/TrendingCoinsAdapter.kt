package com.marioioannou.cryptocurrencyapp.adapters

import android.util.Log
import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.AsyncListDiffer
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.RecyclerView
import coil.load
import com.marioioannou.cryptocurrencyapp.coin_data.model.trending_coins.Coin
import com.marioioannou.cryptocurrencyapp.coin_data.model.trending_coins.Item
import com.marioioannou.cryptocurrencyapp.databinding.TrendingCoinRecyclerviewRowBinding

class TrendingCoinsAdapter(
    private val onItemClicked: (Coin) -> Unit,
) : RecyclerView.Adapter<TrendingCoinsAdapter.TrendsViewHolder>()  {

    inner class TrendsViewHolder(val binding: TrendingCoinRecyclerviewRowBinding) :
        RecyclerView.ViewHolder(binding.root) {
        fun bind(trend: Coin) {
            binding.apply {
                val coinLogo = trend.item.thumb
                tvTrendingName.text = trend.item.name?.uppercase()
                tvTrendingPrice.text = trend.item.price_btc.toString()
                imgTrending.load(coinLogo)
//                val coinLogo = trend.coins[itemCount].item.thumb
//                    tvTrendingName.text = trend.coins[itemCount].item.name?.uppercase()
//                tvTrendingPrice.text = trend.coins[itemCount].item.price_btc?.toString()
//                imgTrending.load(coinLogo)
            }
        }
    }

    private val differCallback = object : DiffUtil.ItemCallback<Coin>() {
        override fun areItemsTheSame(oldItem: Coin, newItem: Coin): Boolean {
            //return oldItem.coins[itemCount].item.name == newItem.coins[itemCount].item.name
            return oldItem.item.name == newItem.item.name
        }

        override fun areContentsTheSame(oldItem: Coin, newItem: Coin): Boolean {
            return oldItem == newItem
        }
    }

    val differ = AsyncListDiffer(this, differCallback)

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): TrendsViewHolder {
        return TrendsViewHolder(
            TrendingCoinRecyclerviewRowBinding.inflate(LayoutInflater.from(parent.context), parent, false)
        )
    }

    override fun onBindViewHolder(holder: TrendsViewHolder, position: Int) {
        val trend = differ.currentList[position]
        holder.apply {
            bind(trend)
        }
        holder.itemView.setOnClickListener {
            onItemClicked(trend)
        }
    }

    override fun getItemCount(): Int {
        Log.e("CoinAdapter", differ.currentList.size.toString())
        return differ.currentList.size
    }
}