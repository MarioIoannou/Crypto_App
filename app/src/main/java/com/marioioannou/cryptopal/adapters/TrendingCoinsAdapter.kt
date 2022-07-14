package com.marioioannou.cryptopal.adapters

import android.graphics.Color
import android.text.InputFilter
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.core.view.isVisible
import androidx.recyclerview.widget.AsyncListDiffer
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.RecyclerView
import coil.load
import com.marioioannou.cryptopal.coin_data.api.CoinApiService
import com.marioioannou.cryptopal.coin_data.model.trending_coins.Coin
import com.marioioannou.cryptopal.databinding.TrendingCoinRecyclerviewRowBinding
import com.marioioannou.cryptopal.ui.MainViewModel
import kotlinx.coroutines.*
import java.io.IOException

class TrendingCoinsAdapter : RecyclerView.Adapter<TrendingCoinsAdapter.TrendsViewHolder>() {

    lateinit var viewModel: MainViewModel

    inner class TrendsViewHolder(val binding: TrendingCoinRecyclerviewRowBinding) :
        RecyclerView.ViewHolder(binding.root) {
    }

    private val differCallback = object : DiffUtil.ItemCallback<Coin>() {
        override fun areItemsTheSame(oldItem: Coin, newItem: Coin): Boolean {
            return oldItem.item?.name == newItem.item?.name
        }

        override fun areContentsTheSame(oldItem: Coin, newItem: Coin): Boolean {
            return oldItem == newItem
        }
    }

    val differ = AsyncListDiffer(this, differCallback)

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): TrendsViewHolder {
        return TrendsViewHolder(
            TrendingCoinRecyclerviewRowBinding.inflate(LayoutInflater.from(parent.context),
                parent,
                false)
        )
    }

    override fun onBindViewHolder(holder: TrendsViewHolder, position: Int) {
        val trend = differ.currentList[position]
        val name1 = trend.item?.slug
        val name2 = trend.item?.id
        CoroutineScope(Dispatchers.Main).launch {
            val response1 = try {
                CoinApiService.coinApi.getCoinById(name1.toString(), "EUR")
            } catch (e: IOException) {
                return@launch
            }
            val response2 = try {
                CoinApiService.coinApi.getCoinById(name2.toString(), "EUR")
            } catch (e: IOException) {
                return@launch
            }
            if (response1.isSuccessful) {
                val item1 = response1.body()?.coin
                val trendingPriceChange1 = item1?.priceChange1d.toString()
                if (item1 == null) {
                    val item2 = response2.body()?.coin
                    val trendingPriceChange2 = item2?.priceChange1d.toString()
                    if (item2 == null) {
                        holder.binding.btc.isVisible = false
                        holder.binding.tvTrendingPrice.filters =
                            arrayOf(InputFilter.LengthFilter(15))
                        holder.binding.tvTrendingPrice.text = "Price not Found"
                        holder.binding.tvTrendingChange.visibility = View.GONE
                    } else {
                        if (trendingPriceChange2.contains("-")) {
                            holder.binding.tvTrendingChange.text =
                                "$trendingPriceChange2%"
                            holder.binding.tvTrendingChange.setTextColor(Color.RED)
                        } else {
                            holder.binding.tvTrendingChange.text =
                                "$trendingPriceChange2%"
                            holder.binding.tvTrendingChange.setTextColor(Color.GREEN)
                        }
                        holder.binding.tvTrendingPrice.filters =
                            arrayOf(InputFilter.LengthFilter(8))
                        holder.binding.tvTrendingPrice.text = item2.price.toString()
                        holder.itemView.setOnClickListener {
                            onItemClickListener?.let { it(trend) }
                        }
                    }
                } else {
                    if (trendingPriceChange1.contains("-")) {
                        holder.binding.tvTrendingChange.text =
                            "$trendingPriceChange1%"
                        holder.binding.tvTrendingChange.setTextColor(Color.RED)
                    } else {
                        holder.binding.tvTrendingChange.text =
                            "$trendingPriceChange1%"
                        holder.binding.tvTrendingChange.setTextColor(Color.GREEN)
                    }
                    holder.binding.tvTrendingPrice.filters = arrayOf(InputFilter.LengthFilter(8))
                    holder.binding.tvTrendingPrice.text = item1.price.toString()
                    holder.itemView.setOnClickListener {
                        onItemClickListener?.let { it(trend) }
                    }
                }
            }
        }
        holder.binding.apply {
            val coinLogo = trend.item?.large
            tvTrendingSymbol.text = trend.item?.name?.uppercase()
            tvTrendingName.text = trend.item?.symbol
            imgTrending.load(coinLogo)
        }

    }

    override fun getItemCount(): Int {
        return differ.currentList.size
    }

    private var onItemClickListener: ((Coin) -> Unit)? = null
    fun setOnItemClickListener(listener: (Coin) -> Unit) {
        onItemClickListener = listener
    }
}