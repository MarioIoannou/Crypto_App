package com.marioioannou.cryptocurrencyapp.ui.fragments

import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.activity.OnBackPressedCallback
import androidx.core.view.isVisible
import androidx.core.widget.addTextChangedListener
import androidx.fragment.app.Fragment
import androidx.lifecycle.Observer
import androidx.recyclerview.widget.LinearLayoutManager
import com.marioioannou.cryptocurrencyapp.adapters.SearchingAdapter
import com.marioioannou.cryptocurrencyapp.databinding.FragmentSearchingBinding
import com.marioioannou.cryptocurrencyapp.ui.MainActivity
import com.marioioannou.cryptocurrencyapp.ui.MainViewModel
import com.marioioannou.cryptocurrencyapp.utils.Constants
import com.marioioannou.cryptocurrencyapp.utils.ScreenState
import kotlinx.coroutines.Job
import kotlinx.coroutines.MainScope
import kotlinx.coroutines.delay
import kotlinx.coroutines.launch

class SearchedCoinFragment:Fragment() {

    private lateinit var binding: FragmentSearchingBinding
    private lateinit var searchingAdapter: SearchingAdapter
    lateinit var viewModel: MainViewModel

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?,
    ): View {
        binding = FragmentSearchingBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        val mainActivity = activity as MainActivity

        viewModel = (activity as MainActivity).viewModel

        mainActivity.onBackPressedDispatcher.addCallback(viewLifecycleOwner,object:
            OnBackPressedCallback(false){
            override fun handleOnBackPressed() {
                TODO("Not yet implemented")
            }
        })
        setUpRecyclerView()

        var job: Job? = null
        binding.etSearch.addTextChangedListener { editable ->
            job?.cancel()
            job = MainScope().launch {
                delay(Constants.SEARCH_NEWS_DELAY)
                editable?.let {
                    if (editable.toString().isNotEmpty()) {
                        viewModel.getSearchedCoinData(editable.toString(), "EUR")
                        viewModel.coinSearch.observe(viewLifecycleOwner, Observer { response ->
                            when (response) {
                                is ScreenState.Success -> {
                                    if (response.data?.coin == null) {
                                        binding.progressBarSearch.isVisible = true
                                    } else {
                                        binding.progressBarSearch.isVisible = false
                                        response.data.coin.let { newsResponse ->
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
            layoutManager = LinearLayoutManager(activity, LinearLayoutManager.HORIZONTAL, false)
        }
    }
}