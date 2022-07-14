package com.marioioannou.cryptopal.ui.fragments

import android.graphics.Color
import android.os.Bundle
import android.util.Log
import android.view.*
import androidx.activity.OnBackPressedCallback
import androidx.core.content.ContextCompat
import androidx.fragment.app.Fragment
import androidx.lifecycle.Observer
import androidx.navigation.fragment.navArgs
import coil.load
import com.github.mikephil.charting.components.XAxis
import com.github.mikephil.charting.data.*
import com.marioioannou.cryptopal.R
import com.marioioannou.cryptopal.adapters.CoinAdapter
import com.marioioannou.cryptopal.databinding.FragmentCoinDetailBinding
import com.marioioannou.cryptopal.ui.MainActivity
import com.marioioannou.cryptopal.ui.MainViewModel
import com.marioioannou.cryptopal.utils.ScreenState

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
        val coinName = coin.name

        binding.detailImgCoin.load(coin.icon)
        binding.detailTvCoinName.text = coinName
        binding.detailTvCoinPrice.text = coin.price.toString()

        if (coin.priceChange1h.toString().contains("-")) {
            binding.tvPriceChange1h.text = coin.priceChange1h.toString() + " %"
            binding.tvPriceChange1h.setTextColor(Color.RED)
        } else {
            binding.tvPriceChange1h.text = coin.priceChange1h.toString() + " %"
            binding.tvPriceChange1h.setTextColor(Color.GREEN)
        }

        if (coin.priceChange1d.toString().contains("-")) {
            binding.tvChangeIn1d.text = coin.priceChange1d.toString() + " %"
            binding.tvChangeIn1d.setTextColor(Color.RED)
        } else {
            binding.tvChangeIn1d.text = coin.priceChange1d.toString() + " %"
            binding.tvChangeIn1d.setTextColor(Color.GREEN)
        }

        if (coin.priceChange1w.toString().contains("-")) {
            binding.tvChangeIn1w.text = coin.priceChange1w.toString() + " %"
            binding.tvChangeIn1w.setTextColor(Color.RED)
        } else {
            binding.tvChangeIn1w.text = coin.priceChange1w.toString() + " %"
            binding.tvChangeIn1w.setTextColor(Color.GREEN)
        }
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
                    val dataSet = ArrayList<Entry>()
                    val dataSetResponse = response.data?.chart
                    dataSetResponse?.map {value ->
                        dataSet.add(Entry(value[0].toFloat(), value[1].toFloat()))
                    }
                    Log.e(TAG, "dataSet: $dataSet")
                    val chartItems = LineDataSet(dataSet,"$coinName data")
                    chartItems.apply {
                        mode = LineDataSet.Mode.CUBIC_BEZIER
                        color = ContextCompat.getColor(requireContext(),R.color.teal)
                        highLightColor = ContextCompat.getColor(requireContext(),R.color.teal)
                        setDrawValues(false)
                        setDrawFilled(true)
                        setDrawCircles(false)
                        lineWidth = 2f
                        //fillColor = ContextCompat.getColor(requireContext(),R.drawable.chart_background)
                        setDrawCircleHole(false)
                        fillDrawable = requireContext().getDrawable(R.drawable.chart_background)
                    }

                    binding.priceChart.apply {
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
                    //setupLineChart()
//                    binding.priceChart.invalidate()
//                    setupLineChart()
                    /*Log.e(TAG, " coinData Response Success")
                    val dataSet = response.data?.chart?.map { value ->
                         BarEntry(value[0].toFloat(),value[1].toFloat())
                    }
                    val barDataSet = BarDataSet(dataSet, " ${binding.detailTvCoinName} price chart").apply {
                        valueTextColor = Color.MAGENTA
                        color = ContextCompat.getColor(requireContext(), R.color.teal)
                    }
                    binding.priceChart.data = BarData(barDataSet)
                    binding.priceChart.invalidate()
                    setupLineChart()
//                    viewModel.coinChart.map { value ->
//                        for (i in value){
//                            dataSet.add(Entry(value[0],value[1]))
//                        }
//                    }
                    //binding.progressBar.isVisible = false
//                    response.data?.let { coindata ->
//                        coinAdapter.differ.submitList(coindata.coins)
//                    }*/
                }
                is ScreenState.Error -> {
                    Log.e(TAG, "coinData Response Error")
                    //binding.progressBar.isVisible = false
                    //notConnected()
                }
            }
        })

    }

    override fun onCreateOptionsMenu(menu: Menu, inflater: MenuInflater) {
        inflater.inflate(R.menu.toolbar_coin_detail_fragment, menu)
    }

    override fun onOptionsItemSelected(item: MenuItem): Boolean {
//        when (item.itemId) {
//            R.id.menu_search -> {
//                findNavController().navigate(R.id.coinsSearch)
//            }
//        }
        return super.onOptionsItemSelected(item)
    }

    private fun setupBarChart(){
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