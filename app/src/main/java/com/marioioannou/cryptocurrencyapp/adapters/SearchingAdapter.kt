package com.marioioannou.cryptocurrencyapp.adapters

import android.annotation.SuppressLint
import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.AsyncListDiffer
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.RecyclerView
import coil.load
import com.marioioannou.cryptocurrencyapp.R
import com.marioioannou.cryptocurrencyapp.coin_data.model.coin_data.CryptoCoin
import com.marioioannou.cryptocurrencyapp.coin_data.model.coin_search.Coin
import com.marioioannou.cryptocurrencyapp.databinding.CoinRecyclerviewRowBinding

class SearchingAdapter: RecyclerView.Adapter<SearchingAdapter.ViewHolder>()  {

    inner class ViewHolder(val binding: CoinRecyclerviewRowBinding) :
        RecyclerView.ViewHolder(binding.root)

    private val differCallback = object : DiffUtil.ItemCallback<Coin>() {
        override fun areItemsTheSame(oldItem: Coin, newItem: Coin): Boolean {
            return oldItem.name == newItem.name
        }

        override fun areContentsTheSame(oldItem: Coin, newItem: Coin): Boolean {
            return oldItem == newItem
        }
    }

    val differ = AsyncListDiffer(this, differCallback)

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
            val coinLogo = coin.icon
            tvCoinName.text = coin.name
            tvCoinSymbol.text = coin.symbol?.uppercase()
            tvCoinPrice.text = "€ " + coin.price.toString()
            //            tvCoinPriceChange.text =
//                coin.priceChange1h.toString() + "%"
            tvCoinPriceChange.text = coin.rank.toString()
            if(coinLogo == null){
                imageCoinLogo.load(R.drawable.no_image_available)
            }else{
                imageCoinLogo.load(coinLogo)
            }
        }
        holder.itemView.setOnClickListener {
            onItemClickListener?.let { it(coin) }
        }
    }

    override fun getItemCount(): Int {

        return differ.currentList.size
    }

    private var onItemClickListener: ((Coin) -> Unit)? = null
    fun setOnItemClickListener(listener: (Coin) -> Unit){
        onItemClickListener = listener
    }

}