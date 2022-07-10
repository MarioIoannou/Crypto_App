package com.marioioannou.cryptocurrencyapp.ui.fragments

import android.graphics.Color
import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.activity.OnBackPressedCallback
import androidx.core.content.ContextCompat
import androidx.core.view.isVisible
import androidx.fragment.app.Fragment
import androidx.lifecycle.Observer
import androidx.lifecycle.map
import androidx.navigation.fragment.navArgs
import coil.load
import com.github.mikephil.charting.components.XAxis
import com.github.mikephil.charting.data.BarData
import com.github.mikephil.charting.data.BarDataSet
import com.github.mikephil.charting.data.BarEntry
import com.github.mikephil.charting.data.Entry
import com.marioioannou.cryptocurrencyapp.R
import com.marioioannou.cryptocurrencyapp.adapters.CoinAdapter
import com.marioioannou.cryptocurrencyapp.databinding.FragmentCoinDetailBinding
import com.marioioannou.cryptocurrencyapp.ui.MainActivity
import com.marioioannou.cryptocurrencyapp.ui.MainViewModel
import com.marioioannou.cryptocurrencyapp.utils.ScreenState

class CoinDetailFragment : Fragment() {

    private lateinit var binding: FragmentCoinDetailBinding
    private lateinit var coinAdapter: CoinAdapter
    lateinit var viewModel: MainViewModel
    val args: CoinDetailFragmentArgs by navArgs()

    private var TAG = "CoinDetailFragment"

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

        mainActivity.onBackPressedDispatcher.addCallback(viewLifecycleOwner,object:OnBackPressedCallback(false){
            override fun handleOnBackPressed() {
                TODO("Not yet implemented")
            }
        })

        viewModel = mainActivity.viewModel

        val coin = args.coin

        binding.detailImgCoin.load(coin.icon)
        binding.detailTvCoinName.text = coin.name
        binding.detailTvCoinPrice.text = coin.price.toString()
        binding.tvPriceChange1h.text = coin.priceChange1h.toString() + " %"
        binding.tvChangeIn1d.text = coin.priceChange1d.toString() + " %"
        binding.tvChangeIn1w.text = coin.priceChange1w.toString() + " %"
        //binding.tvChangeIn1w.text = "€ " + coin.priceChange1w.toString()

        viewModel.getCoinChartData(coin.id.toString(),"24h")
        viewModel.coinChart.observe(viewLifecycleOwner, Observer { response ->
            Log.e(TAG, "viewModel.coinData.observe")
            when (response) {
                is ScreenState.Loading -> {
                    Log.e(TAG, "coinData Response Loading")
                    //binding.progressBar.isVisible = false
                }
                is ScreenState.Success -> {
                    Log.e(TAG, " coinData Response Success")
                    val dataSet = response.data?.chart?.map { value ->
                         BarEntry(value[0].toFloat(),value[1].toFloat())
                    }
                    val barDataSet = BarDataSet(dataSet, " ${binding.detailTvCoinName} price chart").apply {
                        valueTextColor = Color.MAGENTA
                        color = ContextCompat.getColor(requireContext(), R.color.teal)
                    }
                    binding.priceChart.data = BarData(barDataSet)
                    binding.priceChart.invalidate()
//                    viewModel.coinChart.map { value ->
//                        for (i in value){
//                            dataSet.add(Entry(value[0],value[1]))
//                        }
//                    }
                    //binding.progressBar.isVisible = false
//                    response.data?.let { coindata ->
//                        coinAdapter.differ.submitList(coindata.coins)
//                    }
                }
                is ScreenState.Error -> {
                    Log.e(TAG, "coinData Response Error")
                    //binding.progressBar.isVisible = false
                    //notConnected()
                }
            }
        })

    }

    private fun setupLineChart(){
        binding.priceChart.xAxis.apply {
            position = XAxis.XAxisPosition.BOTTOM
            setDrawLabels(false)
            axisLineColor = Color.YELLOW
            textColor = Color.RED
            setDrawGridLines(false)
        }
        binding.priceChart.axisLeft.apply {
            axisLineColor = Color.YELLOW
            textColor = Color.GREEN
            setDrawGridLines(false)
        }
        binding.priceChart.axisRight.apply {
            axisLineColor = Color.YELLOW
            textColor = Color.GREEN
            setDrawGridLines(false)
        }
        binding.priceChart.apply {
            description.text = " ${binding.detailTvCoinName} price over time"
            legend.isEnabled = false
        }
    }
}