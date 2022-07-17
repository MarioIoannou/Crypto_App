package com.marioioannou.cryptopal.ui.fragments

import android.os.Bundle
import android.util.Log
import android.view.*
import androidx.constraintlayout.motion.widget.MotionLayout
import androidx.core.view.isVisible
import androidx.fragment.app.Fragment
import androidx.lifecycle.Observer
import androidx.navigation.fragment.findNavController
import androidx.recyclerview.widget.LinearLayoutManager
import com.marioioannou.cryptopal.R
import com.marioioannou.cryptopal.databinding.FragmentCoinBinding
import com.marioioannou.cryptopal.ui.MainViewModel
import com.marioioannou.cryptopal.adapters.CoinAdapter
import com.marioioannou.cryptopal.adapters.TrendingCoinsAdapter
import com.marioioannou.cryptopal.coin_data.model.coin_data.CryptoCoin
import com.marioioannou.cryptopal.coin_data.model.coin_search.Coin
import com.marioioannou.cryptopal.ui.MainActivity
import com.marioioannou.cryptopal.utils.ScreenState

class CoinFragment : Fragment() {

    private lateinit var binding: FragmentCoinBinding
    private lateinit var coinAdapter: CoinAdapter
    private lateinit var trendAdapter: TrendingCoinsAdapter
    lateinit var viewModel: MainViewModel
    private var TAG = "CoinFragment"

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?,
    ): View {
        binding = FragmentCoinBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        setHasOptionsMenu(true)

        viewModel = (activity as MainActivity).viewModel //Each fragment
        setupRecyclerView()
        setupTrendingRecyclerView()
        //binding.shimmerRvCoin.startShimmer()

//        coinAdapter = CoinAdapter{ coin ->
//            val action = CoinFragmentDirections.actionCoinFragmentToCoinDetailFragment(coin)
//            findNavController().navigate(action)
//        }

//        coinAdapter.setOnItemClickListener { coin ->
//            val bundle = Bundle().apply {
//                putSerializable("details", coin)
//            }
//            findNavController().navigate(R.id.action_coinFragment_to_coinDetailFragment, bundle)
//        }

        trendAdapter.setOnItemClickListener { trend : com.marioioannou.cryptopal.coin_data.model.trending_coins.Coin->
            val action = CoinFragmentDirections.actionCoinFragmentToTrendingCoinDetailFragment(trend)
            findNavController().navigate(action)
        }

        coinAdapter.setOnItemClickListener { coin : CryptoCoin ->
            val action = CoinFragmentDirections.actionCoinFragmentToCoinDetailFragment(coin)
            findNavController().navigate(action)
        }

        val searchList = mutableListOf<Coin>()
        viewModel.coinData.observe(viewLifecycleOwner, Observer { coinResponse ->
            Log.e(TAG, "viewModel.coinData.observe")
            when (coinResponse) {
                is ScreenState.Loading -> {

                    Log.e(TAG, "coinData Response Loading")
                }
                is ScreenState.Success -> {
                    Log.e(TAG, " coinData Response Success")
//                    binding.shimmerRvCoin.stopShimmer()
//                    binding.shimmerRvCoin.visibility = View.GONE
//                    binding.rvCoinRecyclerview.visibility = View.VISIBLE
                    coinResponse.data?.let { coindata ->
                        coinAdapter.differ.submitList(coindata.coins)
                    }
                }
                is ScreenState.Error -> {
                    Log.e(TAG, "coinData Response Error")
                    //notConnected()
                }
            }
        })

        viewModel.trendingCoins.observe(viewLifecycleOwner, Observer { response ->
            Log.e(TAG, "viewModel.trendingCoins.observe")
            //processResponse(response)
            when (response) {
                is ScreenState.Loading -> {
                    Log.e(TAG, "trendingCoins Response Loading")
                }
                is ScreenState.Success -> {
                    Log.e(TAG, " trendingCoins Response Success")
                    //binding.shimmerRvCoin.stopShimmer()
                    response.data?.let { trendingCoinData ->
                        trendAdapter.differ.submitList(trendingCoinData.coins)
                    }
                }
                is ScreenState.Error -> {
                    Log.e(TAG, "trendingCoins Response Error")
                    //notConnected()
                }
            }
        })
    }

    private fun setupRecyclerView() {
        coinAdapter = CoinAdapter()
        binding.rvCoinRecyclerview.apply {
            adapter = coinAdapter
            layoutManager = LinearLayoutManager(activity)
            setHasFixedSize(true)
        }
    }

    private fun setupTrendingRecyclerView() {
        trendAdapter = TrendingCoinsAdapter()
        binding.rvTrending.apply {
            adapter = trendAdapter
            layoutManager = LinearLayoutManager(activity, LinearLayoutManager.HORIZONTAL, false)
            setHasFixedSize(true)
        }
    }

    override fun onCreateOptionsMenu(menu: Menu, inflater: MenuInflater) {
        inflater.inflate(R.menu.toolbar_coin_fragment, menu)
    }

    override fun onOptionsItemSelected(item: MenuItem): Boolean {
        when (item.itemId) {
            R.id.menu_search -> {
                findNavController().navigate(R.id.searchingFragment)
            }
        }
        return super.onOptionsItemSelected(item)
    }


//    private fun notConnected() {
//        val dialogBuilder = AlertDialog.Builder(requireContext(), R.style.AlertDialogTheme)
//        dialogBuilder
//            .setMessage("Please check your connection and try again.")
//            .setTitle("Connection")
//            .setCancelable(false)
//            .setNegativeButton("Exit", DialogInterface.OnClickListener { _, _ -> onDestroy() })
//            .setIcon(R.drawable.no_wifi)
//            .show()
//    }
}