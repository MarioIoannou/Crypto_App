package com.marioioannou.cryptocurrencyapp.adapters

import android.text.InputFilter
import android.util.Log
import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.core.view.isVisible
import androidx.recyclerview.widget.AsyncListDiffer
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.RecyclerView
import coil.load
import com.marioioannou.cryptocurrencyapp.coin_data.api.CoinApiService
import com.marioioannou.cryptocurrencyapp.coin_data.model.coin_data.CryptoCoin
import com.marioioannou.cryptocurrencyapp.coin_data.model.coin_search.CoinSearch
import com.marioioannou.cryptocurrencyapp.coin_data.model.trending_coins.Coin
import com.marioioannou.cryptocurrencyapp.coin_data.model.trending_coins.Item
import com.marioioannou.cryptocurrencyapp.databinding.TrendingCoinRecyclerviewRowBinding
import com.marioioannou.cryptocurrencyapp.ui.MainViewModel
import kotlinx.coroutines.DelicateCoroutinesApi
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.GlobalScope
import kotlinx.coroutines.launch
import java.io.IOException

@OptIn(DelicateCoroutinesApi::class)
class TrendingCoinsAdapter(
    //private val onItemClicked: (Coin) -> Unit,
) : RecyclerView.Adapter<TrendingCoinsAdapter.TrendsViewHolder>() {

    //var searchedList = mutableListOf<com.marioioannou.cryptocurrencyapp.coin_data.model.coin_search.Coin>()
    lateinit var viewModel: MainViewModel

    inner class TrendsViewHolder(val binding: TrendingCoinRecyclerviewRowBinding) :
        RecyclerView.ViewHolder(binding.root) {
//        fun bind(trend: Coin) {
//            binding.apply {
//                val search = searchedList[itemCount]
//                val coinLogo = trend.item.large
//                tvTrendingName.text = trend.item.name?.uppercase()
//                //tvTrendingPrice.text = trend.item.price_btc.toString()
//                tvTrendingPrice.text = search.price.toString()
//                imgTrending.load(coinLogo)
////                val coinLogo = trend.coins[itemCount].item.thumb
////                    tvTrendingName.text = trend.coins[itemCount].item.name?.uppercase()
////                tvTrendingPrice.text = trend.coins[itemCount].item.price_btc?.toString()
////                imgTrending.load(coinLogo)
//            }
//        }
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
            TrendingCoinRecyclerviewRowBinding.inflate(LayoutInflater.from(parent.context),
                parent,
                false)
        )
    }

    override fun onBindViewHolder(holder: TrendsViewHolder, position: Int) {
        val trend = differ.currentList[position]
        val name = trend.item.id
        GlobalScope.launch(Dispatchers.Main) {
            val response = try {
                CoinApiService.coinApi.getCoinById(name.toString(), "EUR")
            }catch (e: IOException) {
                return@launch
            }
            if (response.isSuccessful){
                val item = response.body()?.coin
                if (item == null){
                    holder.binding.btc.isVisible = false
                    holder.binding.tvTrendingPrice.filters = arrayOf(InputFilter.LengthFilter(15))
                    holder.binding.tvTrendingPrice.text = "Price not Found"
                }else{
                    holder.binding.tvTrendingPrice.filters = arrayOf(InputFilter.LengthFilter(5))
                    holder.binding.tvTrendingPrice.text = item.price.toString()
                }

            }
        }
        //val search = searchedList[position]
        //Log.d("Trending", "searchedList $searchedList")
        holder.binding.apply {
            val coinLogo = trend.item.large
            tvTrendingName.text = trend.item.name?.uppercase()
                //tvTrendingPrice.text = trend.item.price_btc.toString()
            //tvTrendingPrice.text = search.price.toString()
            imgTrending.load(coinLogo)
//                val coinLogo = trend.coins[itemCount].item.thumb
//                    tvTrendingName.text = trend.coins[itemCount].item.name?.uppercase()
//                tvTrendingPrice.text = trend.coins[itemCount].item.price_btc?.toString()
//                imgTrending.load(coinLogo)
        }

    }

    override fun getItemCount(): Int {
        //Log.e("CoinAdapter", differ.currentList.size.toString())
        return differ.currentList.size
    }

    private var onItemClickListener: ((Coin) -> Unit)? = null
    fun setOnClickListener(listener: (Coin) -> Unit) {
        onItemClickListener = listener
    }
}