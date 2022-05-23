package com.marioioannou.cryptocurrencyapp.ui.fragments

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.navigation.fragment.navArgs
import coil.load
import com.marioioannou.cryptocurrencyapp.adapters.CoinAdapter
import com.marioioannou.cryptocurrencyapp.databinding.FragmentCoinDetailBinding
import com.marioioannou.cryptocurrencyapp.ui.MainActivity
import com.marioioannou.cryptocurrencyapp.ui.MainViewModel

class CoinDetailFragment : Fragment() {

    private lateinit var binding: FragmentCoinDetailBinding
    private lateinit var coinAdapter: CoinAdapter
    lateinit var viewModel: MainViewModel
    val args: CoinDetailFragmentArgs by navArgs()

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?,
    ): View {
        binding = FragmentCoinDetailBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        val mainActivity = activity as MainActivity
        viewModel = mainActivity.viewModel
        val coin = args.coin
        binding.detailImgCoin.load(coin.image)
        binding.detailTvCoinName.text = coin.name
        binding.detailTvCoinPrice.text = coin.current_price.toString()
        binding.tvChange24hPrice.text = coin.price_change_percentage_24h.toString() + "%"
        binding.tvHigh24hPrice.text = "€" + coin.high_24h.toString()
        binding.tvLow24hPrice.text = "€" + coin.low_24h.toString()

    }
}