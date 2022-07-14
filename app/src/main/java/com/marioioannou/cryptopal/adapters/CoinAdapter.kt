package com.marioioannou.cryptopal.adapters

import android.annotation.SuppressLint
import android.graphics.Color
import android.util.Log
import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.AsyncListDiffer
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.RecyclerView
import coil.load
import com.marioioannou.cryptopal.R
import com.marioioannou.cryptopal.coin_data.model.coin_data.CryptoCoin
import com.marioioannou.cryptopal.databinding.CoinRecyclerviewRowBinding

class CoinAdapter : RecyclerView.Adapter<CoinAdapter.ViewHolder>() {

    inner class ViewHolder(val binding: CoinRecyclerviewRowBinding) :
        RecyclerView.ViewHolder(binding.root)

    private val differCallback = object : DiffUtil.ItemCallback<CryptoCoin>() {
        override fun areItemsTheSame(oldItem: CryptoCoin, newItem: CryptoCoin): Boolean {
            return oldItem.name == newItem.name
        }

        override fun areContentsTheSame(oldItem: CryptoCoin, newItem: CryptoCoin): Boolean {
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
            val priceChange = coin.priceChange1h.toString()
            if(coinLogo == null){
                imageCoinLogo.load(R.drawable.no_image_available)
            }else{
                imageCoinLogo.load(coinLogo)
            }
            tvCoinName.text = coin.name
            tvCoinSymbol.text = coin.symbol?.uppercase()
            tvCoinPrice.text = "€ " + coin.price.toString()

            if(priceChange.contains("-")){
                tvCoinPriceChange.text =
                    coin.priceChange1h.toString() + "%"
                tvCoinPriceChange.setTextColor(Color.RED)
            }else{
                tvCoinPriceChange.text =
                    coin.priceChange1h.toString() + "%"
                tvCoinPriceChange.setTextColor(Color.GREEN)
            }

            //tvCoinPriceChange.text = coin.rank.toString()

        }
        holder.itemView.setOnClickListener {
            onItemClickListener?.let { it(coin) }
        }
    }

    override fun getItemCount(): Int {

        return differ.currentList.size
    }

    private var onItemClickListener: ((CryptoCoin) -> Unit)? = null
    fun setOnItemClickListener(listener: (CryptoCoin) -> Unit){
        onItemClickListener = listener
    }

}
