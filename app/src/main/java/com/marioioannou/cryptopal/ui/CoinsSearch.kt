package com.marioioannou.cryptopal.ui

import android.os.Bundle
import android.util.Log
import androidx.appcompat.app.AppCompatActivity
import androidx.core.view.isVisible
import androidx.core.widget.addTextChangedListener
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProvider
import androidx.recyclerview.widget.LinearLayoutManager
import com.marioioannou.cryptopal.adapters.SearchingAdapter
import com.marioioannou.cryptopal.coin_data.repository.CoinRepository
import com.marioioannou.cryptopal.databinding.ActivityCoinsSearchBinding
import com.marioioannou.cryptopal.utils.Constants.SEARCH_NEWS_DELAY
import com.marioioannou.cryptopal.utils.ScreenState
import kotlinx.coroutines.Job
import kotlinx.coroutines.MainScope
import kotlinx.coroutines.delay
import kotlinx.coroutines.launch

class CoinsSearch : AppCompatActivity() {

    private lateinit var binding: ActivityCoinsSearchBinding
    lateinit var viewModel: MainViewModel
    lateinit var searchingAdapter: SearchingAdapter

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        binding = ActivityCoinsSearchBinding.inflate(layoutInflater)
        setContentView(binding.root)

        binding.imgBackArrowSearchFragment.setOnClickListener {
            onBackPressed()
        }

        val repository = CoinRepository()
        val factory = MainViewModelFactory(application as com.marioioannou.cryptopal.CryptoWatchApplication, repository)
        viewModel = ViewModelProvider(this, factory)[MainViewModel::class.java]

        setUpRecyclerView()

        var job: Job? = null
        binding.etSearch.addTextChangedListener { editable ->
            job?.cancel()
            job = MainScope().launch {
                delay(SEARCH_NEWS_DELAY)
                editable?.let {
                    if (editable.toString().isNotEmpty()) {
                        viewModel.getSearchedCoinData(editable.toString(), "EUR")
                        viewModel.coinSearch.observe(this@CoinsSearch, Observer { response ->
                            when (response) {
                                is ScreenState.Success -> {
                                    if (response.data?.coin == null) {
                                        binding.progressBarSearch.isVisible = true
                                    } else {
                                        binding.progressBarSearch.isVisible = false
                                        response.data?.coin.let { newsResponse ->
                                            searchingAdapter.differ.submitList(mutableListOf(
                                                newsResponse))
                                        }
                                    }
                                }
                                //binding.progressBar2.isVisible = false
                                is ScreenState.Error -> {
                                    //binding.progressBar2.isVisible = false
                                    response.message?.let { message ->
                                        Log.e("BreakingNewsFrag", message)
                                    }
                                }
                                is ScreenState.Loading -> {
                                    //binding.progressBar2.isVisible = true
                                }
                            }
                        })
                    }
                }
            }
        }


    }

    private fun setUpRecyclerView() {
        searchingAdapter = SearchingAdapter()
        binding.rvSearchedCoins.apply {
            adapter = searchingAdapter
            layoutManager = LinearLayoutManager(this@CoinsSearch)
        }
    }

    override fun onBackPressed() {
        super.onBackPressed()
    }
}