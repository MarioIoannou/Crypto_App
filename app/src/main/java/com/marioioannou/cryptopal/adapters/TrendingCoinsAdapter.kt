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
        val nameSlug = trend.item?.slug
        val nameId = trend.item?.id
        CoroutineScope(Dispatchers.Main).launch {
            val responseSlug = try {
                CoinApiService.coinApi.getCoinById(nameSlug.toString(), "EUR")
            } catch (e: IOException) {
                return@launch
            }
            val responseId = try {
                CoinApiService.coinApi.getCoinById(nameId.toString(), "EUR")
            } catch (e: IOException) {
                return@launch
            }
            if (responseSlug.isSuccessful) {
                val itemSlug = responseSlug.body()?.coin
                val trendingPriceChangeSlug = itemSlug?.priceChange1d.toString()
                if (itemSlug == null) {
                    val itemId = responseId.body()?.coin
                    val trendingPriceChangeId = itemId?.priceChange1d.toString()
                    if (itemId == null) {
                        holder.binding.btc.isVisible = false
                        holder.binding.tvTrendingPrice.filters =
                            arrayOf(InputFilter.LengthFilter(15))
                        holder.binding.tvTrendingPrice.text = "Price not Found"
                        holder.binding.tvTrendingChange.visibility = View.GONE
                    } else {
                        if (trendingPriceChangeId.contains("-")) {
                            holder.binding.tvTrendingChange.text =
                                "$trendingPriceChangeId%"
                            holder.binding.tvTrendingChange.setTextColor(Color.RED)
                        } else {
                            holder.binding.tvTrendingChange.text =
                                "$trendingPriceChangeId%"
                            holder.binding.tvTrendingChange.setTextColor(Color.GREEN)
                        }
                        holder.binding.tvTrendingPrice.filters =
                            arrayOf(InputFilter.LengthFilter(8))
                        holder.binding.tvTrendingPrice.text = itemId.price.toString()
                        holder.itemView.setOnClickListener {
                            onItemClickListener?.let { it(trend) }
                        }
                    }
                } else {
                    if (trendingPriceChangeSlug.contains("-")) {
                        holder.binding.tvTrendingChange.text =
                            "$trendingPriceChangeSlug%"
                        holder.binding.tvTrendingChange.setTextColor(Color.RED)
                    } else {
                        holder.binding.tvTrendingChange.text =
                            "$trendingPriceChangeSlug%"
                        holder.binding.tvTrendingChange.setTextColor(Color.GREEN)
                    }
                    holder.binding.tvTrendingPrice.filters = arrayOf(InputFilter.LengthFilter(8))
                    holder.binding.tvTrendingPrice.text = itemSlug.price.toString()
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