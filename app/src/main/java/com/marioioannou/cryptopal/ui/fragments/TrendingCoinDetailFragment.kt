package com.marioioannou.cryptopal.ui.fragments

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.activity.OnBackPressedCallback
import androidx.fragment.app.Fragment
import androidx.lifecycle.lifecycleScope
import androidx.navigation.fragment.navArgs
import coil.load
import com.marioioannou.cryptopal.adapters.TrendingCoinsAdapter
import com.marioioannou.cryptopal.databinding.FragmentTrendingCoinDetailBinding
import com.marioioannou.cryptopal.ui.MainActivity
import com.marioioannou.cryptopal.ui.MainViewModel

class TrendingCoinDetailFragment: Fragment() {

    private lateinit var binding: FragmentTrendingCoinDetailBinding
    private lateinit var trendAdapter: TrendingCoinsAdapter
    lateinit var viewModel: MainViewModel
    val args: TrendingCoinDetailFragmentArgs by navArgs()

    private var TAG = "CoinDetailFragment"

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?,
    ): View {
        binding = FragmentTrendingCoinDetailBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        val mainActivity = activity as MainActivity

        mainActivity.onBackPressedDispatcher.addCallback(viewLifecycleOwner,object:
            OnBackPressedCallback(false){
            override fun handleOnBackPressed() {
                TODO("Not yet implemented")
            }
        })

        viewModel = mainActivity.viewModel

        val trend = args.trend
        val coinName = trend.item?.name
        val coinSlug = trend.item?.slug
        val coinId = trend.item?.id

        binding.trendingDetailImgCoin.load(trend.item?.large)
        binding.trendingDetailTvCoinName.text = coinName
    }
}