package com.marioioannou.cryptopal.ui.fragments

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
import com.marioioannou.cryptopal.adapters.SearchingAdapter
import com.marioioannou.cryptopal.databinding.FragmentSearchingBinding
import com.marioioannou.cryptopal.ui.MainActivity
import com.marioioannou.cryptopal.ui.MainViewModel
import com.marioioannou.cryptopal.utils.Constants
import com.marioioannou.cryptopal.utils.ScreenState
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

        binding.imgBackArrowSearchFragment.setOnClickListener {
            mainActivity.onBackPressed()
        }

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
                                        response.data.coin.let { newsResponse ->
                                            searchingAdapter.differ.submitList(mutableListOf(
                                                newsResponse))
                                        }
                                        binding.progressBarSearch.isVisible = false
                                    }
                                }
                                is ScreenState.Error -> {
                                    binding.progressBarSearch.isVisible = true
                                    response.message?.let { message ->
                                        Log.e("BreakingNewsFrag", message)
                                    }
                                }
                                is ScreenState.Loading -> {
                                    binding.progressBarSearch.isVisible = true
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