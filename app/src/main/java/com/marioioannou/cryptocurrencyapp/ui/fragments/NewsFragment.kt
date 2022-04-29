package com.marioioannou.cryptocurrencyapp.ui.fragments

import android.os.Bundle
import android.util.Log
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.lifecycle.Observer
import androidx.recyclerview.widget.LinearLayoutManager
import com.marioioannou.cryptocurrencyapp.R
import com.marioioannou.cryptocurrencyapp.adapters.NewsAdapter
import com.marioioannou.cryptocurrencyapp.databinding.FragmentNewsBinding
import com.marioioannou.cryptocurrencyapp.ui.MainActivity
import com.marioioannou.cryptocurrencyapp.ui.MainViewModel
import com.marioioannou.cryptocurrencyapp.utils.ScreenState

class NewsFragment : Fragment() {

    private lateinit var binding: FragmentNewsBinding
    lateinit var viewModel: MainViewModel
    lateinit var newsAdapter: NewsAdapter

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?,
    ): View? {
        binding = FragmentNewsBinding.inflate(inflater,container,false)
        viewModel = (activity as MainActivity).viewModel
        setUpRecyclerView()
        viewModel.news.observe(viewLifecycleOwner, Observer { response ->
            when(response) {
                is ScreenState.Success -> {
                    response.data?.let { newsResponse ->
                        newsAdapter.differ.submitList(newsResponse.articles)
                    }
                }
                is ScreenState.Error -> {
                    response.message?.let { message ->
                        Log.e("BreakingNewsFrag",message)
                    }
                }
                is ScreenState.Loading -> {
                }
            }
        })

        return  binding.root
    }

    private fun setUpRecyclerView(){
        newsAdapter = NewsAdapter()
        binding.rvNewsRecyclerview.apply {
            adapter = newsAdapter
            layoutManager = LinearLayoutManager(activity)
        }
    }
}