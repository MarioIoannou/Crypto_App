package com.marioioannou.cryptocurrencyapp.ui.fragments

import android.content.DialogInterface
import android.os.Bundle
import android.util.Log
import android.view.*
import androidx.appcompat.app.AlertDialog
import androidx.appcompat.widget.SearchView
import androidx.core.content.ContentProviderCompat.requireContext
import androidx.core.view.isVisible
import androidx.fragment.app.Fragment
import androidx.lifecycle.Observer
import androidx.navigation.fragment.findNavController
import androidx.recyclerview.widget.LinearLayoutManager
import com.marioioannou.cryptocurrencyapp.R
import com.marioioannou.cryptocurrencyapp.databinding.FragmentCoinBinding
import com.marioioannou.cryptocurrencyapp.ui.MainViewModel
import com.marioioannou.cryptocurrencyapp.adapters.CoinAdapter
import com.marioioannou.cryptocurrencyapp.adapters.TrendingCoinsAdapter
import com.marioioannou.cryptocurrencyapp.ui.MainActivity
import com.marioioannou.cryptocurrencyapp.utils.Constants.Companion.SEARCH_NEWS_DELAY
import com.marioioannou.cryptocurrencyapp.utils.ScreenState
import kotlinx.coroutines.Job
import kotlinx.coroutines.MainScope
import kotlinx.coroutines.delay
import kotlinx.coroutines.launch

class CoinFragment : Fragment()/*, SearchView.OnQueryTextListener*/ {

    private lateinit var binding: FragmentCoinBinding
    private lateinit var coinAdapter: CoinAdapter
    private lateinit var trendAdapter: TrendingCoinsAdapter
    lateinit var viewModel: MainViewModel
    lateinit var query: String
    var job: Job? = null
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

        coinAdapter = CoinAdapter{ coin ->
            val action = CoinFragmentDirections.actionCoinFragmentToCoinDetailFragment(coin)
            findNavController().navigate(action)
        }

        setupRecyclerView(coinAdapter)
        setupTrendingRecyclerView()
        viewModel.coinData.observe(viewLifecycleOwner, Observer { response ->
            Log.e(TAG, "viewModel.observe")
            //processResponse(response)
            when (response) {
                is ScreenState.Loading -> {
                    Log.e(TAG, "processResponse Loading")
                    binding.progressBar.isVisible = false
                }
                is ScreenState.Success -> {
                    Log.e(TAG, "processResponse Success")
                    binding.progressBar.isVisible = false
                    response.data?.let { coindata ->
                        coinAdapter.differ.submitList(coindata)
                    }
                }
                is ScreenState.Error -> {
                    Log.e(TAG, "processResponse Error")
                    binding.progressBar.isVisible = false
                    notConnected()
                }
            }
        })

        viewModel.trendingCoins.observe(viewLifecycleOwner, Observer { response ->
            Log.e(TAG, "viewModel.observe")
            //processResponse(response)
            when (response) {
                is ScreenState.Loading -> {
                    Log.e(TAG, "processResponse Loading")
                    binding.progressBar.isVisible = false
                }
                is ScreenState.Success -> {
                    Log.e(TAG, "processResponse Success")
                    binding.progressBar.isVisible = false
                    response.data?.let { coindata ->
                        trendAdapter.differ.submitList(coindata.coins)
                    }
                }
                is ScreenState.Error -> {
                    Log.e(TAG, "processResponse Error")
                    binding.progressBar.isVisible = false
                    notConnected()
                }
            }
        })
    }

//    private fun processResponse(state: ScreenState<Response<MutableList<Coin>>>) {
//        Log.e(TAG, "processResponse")
//        when (state) {
//            is ScreenState.Loading -> {
//                Log.e(TAG, "processResponse Loading")
//                binding.progressBar.isVisible = false
//            }
//            is ScreenState.Success -> {
//                Log.e(TAG, "processResponse Success")
//                if (state.data != null) {
//                    binding.rvCoinRecyclerview.apply {
//                        recyclerViewAdapter = CoinAdapter(requireActivity(), state.data)
//                        adapter = recyclerViewAdapter
//                        layoutManager = LinearLayoutManager(requireContext())
//                        binding.progressBar.isVisible = false
//                    }
//                }
//            }
//            is ScreenState.Error -> {
//                Log.e(TAG, "processResponse Error")
//                binding.progressBar.isVisible = false
//                notConnected()
//            }
//        }
//    }

    private fun setupRecyclerView(Adapter: CoinAdapter) {
        coinAdapter = Adapter
        binding.rvCoinRecyclerview.apply {
            adapter = Adapter
            layoutManager = LinearLayoutManager(activity)
            setHasFixedSize(true)
        }
    }

    private fun setupTrendingRecyclerView() {
        trendAdapter = TrendingCoinsAdapter {}
        binding.rvTrending.apply {
            adapter = trendAdapter
            layoutManager = LinearLayoutManager(activity,LinearLayoutManager.HORIZONTAL,false)
            setHasFixedSize(true)
        }
    }

    override fun onCreateOptionsMenu(menu: Menu, inflater: MenuInflater) {
        inflater.inflate(R.menu.toolbar_coin_fragment, menu)
        val searchItem = menu.findItem(R.id.menu_search)
        val searchView = searchItem.actionView as? SearchView
        searchView?.isSubmitButtonEnabled = true
        searchView?.setOnQueryTextListener(object : SearchView.OnQueryTextListener {

            override fun onQueryTextChange(newText: String): Boolean {
                return false
            }

            override fun onQueryTextSubmit(query: String): Boolean {
                viewModel.searchCoin.observe(viewLifecycleOwner, Observer { response ->
                    Log.e(TAG, "viewModel.observe")
                    //processResponse(response)
//                    when(response) {
//                        is ScreenState.Success -> {
//                            response.data?.let { searchResponse ->
//                                coinAdapter.differ2.submitList(searchResponse.coins.toMutableList())
//                            }
//                        }
//                        is ScreenState.Error -> {
//                            response.message?.let { message ->
//                                Log.e("BreakingNewsFrag",message)
//                            }
//                        }
//                        is ScreenState.Loading -> {
//                        }
//                    }
                })
                return false
            }

//            override fun onQueryTextSubmit(query: String): Boolean {
//                viewModel.getSearchCoin(query)
//                return false
//            }

        })
    }

//    private fun searchCoins(query: String) {
//        var searchCoin = query
//        searchCoin = "%$searchCoin%"
//        job?.cancel()
//        job = MainScope().launch {
//            delay(SEARCH_NEWS_DELAY)
//            query.let {
//                if (query.isNotEmpty()){
//                    viewModel.getSearchCoin(query)
//                }
//            }
//
//        }
//    }

    private fun notConnected() {
        val dialogBuilder = AlertDialog.Builder(requireContext(), R.style.AlertDialogTheme)
        dialogBuilder
            .setMessage("Please check your connection and try again.")
            .setTitle("Connection")
            .setCancelable(false)
            .setNegativeButton("Exit", DialogInterface.OnClickListener { _, _ -> onDestroy() })
            .setIcon(R.drawable.no_wifi)
            .show()
    }

//    override fun onQueryTextSubmit(query: String?): Boolean {
//        if (query != null) {
//            viewModel.getSearchCoin(query)
//        }
//        return false
//    }
//
//    override fun onQueryTextChange(newText: String?): Boolean {
//        return false
//    }


}