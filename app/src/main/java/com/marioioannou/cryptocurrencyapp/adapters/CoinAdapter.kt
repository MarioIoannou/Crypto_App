package com.marioioannou.cryptocurrencyapp.adapters

import android.annotation.SuppressLint
import android.util.Log
import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.AsyncListDiffer
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.RecyclerView
import coil.load
import com.marioioannou.cryptocurrencyapp.coin_data.model.crypto.CryptoCoin
import com.marioioannou.cryptocurrencyapp.databinding.CoinRecyclerviewRowBinding

class CoinAdapter(
) : RecyclerView.Adapter<CoinAdapter.ViewHolder>() {

    //private val coinData = coinsData?.body() as MutableList
    //val coinData = mutableListOf<Coin>()
    inner class ViewHolder(val binding: CoinRecyclerviewRowBinding) :
        RecyclerView.ViewHolder(binding.root)
    // - //
    private val differCallback = object : DiffUtil.ItemCallback<CryptoCoin>() {
        override fun areItemsTheSame(oldItem: CryptoCoin, newItem: CryptoCoin): Boolean {
            return oldItem.name == newItem.name
        }
        override fun areContentsTheSame(oldItem: CryptoCoin, newItem: CryptoCoin): Boolean {
            return oldItem == newItem
        }
    }
    // Compute the difference of two lists in the background
    val differ = AsyncListDiffer(this, differCallback)
    // - //

    override fun onCreateViewHolder(
        parent: ViewGroup,
        viewType: Int,
    ): ViewHolder {
        return ViewHolder(
            CoinRecyclerviewRowBinding.inflate(LayoutInflater.from(parent.context), parent, false)
        )
    }

    @SuppressLint("SetTextI18n")
    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        val coin = differ.currentList[position]
        holder.binding.apply {
            val coinLogo = coin.image
            tvCoinName.text = coin.name
            tvCoinSymbol.text = coin.symbol.uppercase()
            tvCoinPrice.text = "€" + coin.current_price.toString()
            tvCoinPriceChange.text =
                coin.price_change_percentage_24h.toString() + "%"
            imageCoinLogo.load(coinLogo)
            setOnItemClickListener {
                onItemClickListener?.let {
                    it(coin)
                }
            }
        }
    }

    override fun getItemCount(): Int {
        //return coinData.size
        Log.e("CoinAdapter",differ.currentList.size.toString())
        return differ.currentList.size
    }

    private var onItemClickListener: ((CryptoCoin) -> Unit)? = null

    private fun setOnItemClickListener(listener: (CryptoCoin) -> Unit){
        onItemClickListener = listener
    }
}