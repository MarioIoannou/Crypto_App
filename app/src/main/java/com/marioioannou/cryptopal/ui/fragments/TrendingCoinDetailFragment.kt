package com.marioioannou.cryptopal.ui.fragments

import android.graphics.Color
import android.os.Bundle
import android.text.InputFilter
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.activity.OnBackPressedCallback
import androidx.core.content.ContextCompat
import androidx.core.view.isVisible
import androidx.fragment.app.Fragment
import androidx.lifecycle.Lifecycle
import androidx.lifecycle.Observer
import androidx.lifecycle.lifecycleScope
import androidx.lifecycle.repeatOnLifecycle
import androidx.navigation.fragment.navArgs
import coil.load
import com.github.mikephil.charting.data.Entry
import com.github.mikephil.charting.data.LineData
import com.github.mikephil.charting.data.LineDataSet
import com.marioioannou.cryptopal.R
import com.marioioannou.cryptopal.adapters.TrendingCoinsAdapter
import com.marioioannou.cryptopal.coin_data.api.CoinApiService
import com.marioioannou.cryptopal.databinding.FragmentTrendingCoinDetailBinding
import com.marioioannou.cryptopal.ui.MainActivity
import com.marioioannou.cryptopal.ui.MainViewModel
import com.marioioannou.cryptopal.utils.ScreenState
import kotlinx.coroutines.launch
import java.io.IOException

class TrendingCoinDetailFragment : Fragment() {

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

        mainActivity.onBackPressedDispatcher.addCallback(viewLifecycleOwner, object :
            OnBackPressedCallback(false) {
            override fun handleOnBackPressed() {
                TODO("Not yet implemented")
            }
        })

        viewModel = mainActivity.viewModel

        val trend = args.trend
        val coinName = trend.item?.name
        val coinSlug = trend.item?.slug
        val coinId = trend.item?.id
        viewLifecycleOwner.lifecycleScope.launch {
            viewLifecycleOwner.repeatOnLifecycle(Lifecycle.State.STARTED) {
                val responseSlug = try {
                    CoinApiService.coinApi.getCoinById(coinSlug.toString(), "EUR")
                } catch (e: IOException) {
                    return@repeatOnLifecycle
                }
                val responseId = try {
                    CoinApiService.coinApi.getCoinById(coinId.toString(), "EUR")
                } catch (e: IOException) {
                    return@repeatOnLifecycle
                }
                if (responseSlug.isSuccessful) {
                    val itemSlug = responseSlug.body()?.coin

                    if(itemSlug == null){
                        val itemId = responseId.body()?.coin
                        binding.trendingDetailTvCoinPrice.text = itemId?.price.toString()
                        if (itemId?.priceChange1h.toString().contains("-")) {
                            binding.trendingTvPriceChange1h.text = itemId?.priceChange1h.toString() + " %"
                            binding.trendingTvPriceChange1h.setTextColor(Color.RED)
                        } else {
                            binding.trendingTvPriceChange1h.text = itemId?.priceChange1h.toString() + " %"
                            binding.trendingTvPriceChange1h.setTextColor(Color.GREEN)
                        }

                        if (itemId?.priceChange1d.toString().contains("-")) {
                            binding.trendingTvChangeIn1d.text = itemId?.priceChange1d.toString() + " %"
                            binding.trendingTvChangeIn1d.setTextColor(Color.RED)
                        } else {
                            binding.trendingTvChangeIn1d.text = itemId?.priceChange1d.toString() + " %"
                            binding.trendingTvChangeIn1d.setTextColor(Color.GREEN)
                        }

                        if (itemId?.priceChange1w.toString().contains("-")) {
                            binding.trendingTvChangeIn1w.text = itemId?.priceChange1w.toString() + " %"
                            binding.trendingTvChangeIn1w.setTextColor(Color.RED)
                        } else {
                            binding.trendingTvChangeIn1w.text = itemId?.priceChange1w.toString() + " %"
                            binding.trendingTvChangeIn1w.setTextColor(Color.GREEN)
                        }
                        viewModel.getCoinChartData(itemId?.id.toString(),"24h")
                        viewModel.coinChart.observe(viewLifecycleOwner, Observer { response ->
                            Log.e(TAG, "viewModel.coinData.observe")
                            when (response) {
                                is ScreenState.Loading -> {
                                    Log.e(TAG, "coinData Response Loading")
                                }
                                is ScreenState.Success -> {
                                    val dataSet = ArrayList<Entry>()
                                    val dataSetResponse = response.data?.chart
                                    dataSetResponse?.map {value ->
                                        dataSet.add(Entry(value[0].toFloat(), value[1].toFloat()))
                                    }
                                    Log.e(TAG, "dataSet: $dataSet")
                                    val chartItems = LineDataSet(dataSet,"$coinName data")
                                    chartItems.apply {
                                        mode = LineDataSet.Mode.CUBIC_BEZIER
                                        color = ContextCompat.getColor(requireContext(), R.color.teal)
                                        highLightColor = ContextCompat.getColor(requireContext(), R.color.teal)
                                        setDrawValues(false)
                                        setDrawFilled(true)
                                        setDrawCircles(false)
                                        lineWidth = 2f
                                        setDrawCircleHole(false)
                                        fillDrawable = requireContext().getDrawable(R.drawable.chart_background)
                                    }

                                    binding.trendingPriceChart.apply {
                                        data = LineData(chartItems)
                                        description.isEnabled = false
                                        isDragEnabled = false
                                        xAxis.isEnabled = false
                                        axisLeft.setDrawAxisLine(false)
                                        axisLeft.textColor = Color.WHITE
                                        axisRight.isEnabled = false
                                        legend.isEnabled = false
                                        setTouchEnabled(false)
                                        setScaleEnabled(false)
                                        setDrawGridBackground(false)
                                        setDrawBorders(false)
                                        invalidate()
                                    }
                                }
                                is ScreenState.Error -> {
                                    Log.e(TAG, "coinData Response Error")
                                }
                            }
                        })
                    }else{
                        binding.trendingDetailTvCoinPrice.text = itemSlug.price.toString()
                        if (itemSlug.priceChange1h.toString().contains("-")) {
                            binding.trendingTvPriceChange1h.text = itemSlug.priceChange1h.toString() + " %"
                            binding.trendingTvPriceChange1h.setTextColor(Color.RED)
                        } else {
                            binding.trendingTvPriceChange1h.text = itemSlug.priceChange1h.toString() + " %"
                            binding.trendingTvPriceChange1h.setTextColor(Color.GREEN)
                        }

                        if (itemSlug.priceChange1d.toString().contains("-")) {
                            binding.trendingTvChangeIn1d.text = itemSlug?.priceChange1d.toString() + " %"
                            binding.trendingTvChangeIn1d.setTextColor(Color.RED)
                        } else {
                            binding.trendingTvChangeIn1d.text = itemSlug?.priceChange1d.toString() + " %"
                            binding.trendingTvChangeIn1d.setTextColor(Color.GREEN)
                        }

                        if (itemSlug?.priceChange1w.toString().contains("-")) {
                            binding.trendingTvChangeIn1w.text = itemSlug?.priceChange1w.toString() + " %"
                            binding.trendingTvChangeIn1w.setTextColor(Color.RED)
                        } else {
                            binding.trendingTvChangeIn1w.text = itemSlug?.priceChange1w.toString() + " %"
                            binding.trendingTvChangeIn1w.setTextColor(Color.GREEN)
                        }
                        viewModel.getCoinChartData(itemSlug?.id.toString(),"24h")
                        viewModel.coinChart.observe(viewLifecycleOwner, Observer { response ->
                            Log.e(TAG, "viewModel.coinData.observe")
                            when (response) {
                                is ScreenState.Loading -> {
                                    Log.e(TAG, "coinData Response Loading")
                                }
                                is ScreenState.Success -> {
                                    val dataSet = ArrayList<Entry>()
                                    val dataSetResponse = response.data?.chart
                                    dataSetResponse?.map {value ->
                                        dataSet.add(Entry(value[0].toFloat(), value[1].toFloat()))
                                    }
                                    Log.e(TAG, "dataSet: $dataSet")
                                    val chartItems = LineDataSet(dataSet,"$coinName data")
                                    chartItems.apply {
                                        mode = LineDataSet.Mode.CUBIC_BEZIER
                                        color = ContextCompat.getColor(requireContext(), R.color.teal)
                                        highLightColor = ContextCompat.getColor(requireContext(), R.color.teal)
                                        setDrawValues(false)
                                        setDrawFilled(true)
                                        setDrawCircles(false)
                                        lineWidth = 2f
                                        setDrawCircleHole(false)
                                        fillDrawable = requireContext().getDrawable(R.drawable.chart_background)
                                    }

                                    binding.trendingPriceChart.apply {
                                        data = LineData(chartItems)
                                        description.isEnabled = false
                                        isDragEnabled = false
                                        xAxis.isEnabled = false
                                        axisLeft.setDrawAxisLine(false)
                                        axisLeft.textColor = Color.WHITE
                                        axisRight.isEnabled = false
                                        legend.isEnabled = false
                                        setTouchEnabled(false)
                                        setScaleEnabled(false)
                                        setDrawGridBackground(false)
                                        setDrawBorders(false)
                                        invalidate()
                                    }
                                }
                                is ScreenState.Error -> {
                                    Log.e(TAG, "coinData Response Error")
                                }
                            }
                        })
                    }
                }
            }
        }

        binding.trendingDetailImgCoin.load(trend.item?.large)
        binding.trendingDetailTvCoinName.text = coinName
    }
}