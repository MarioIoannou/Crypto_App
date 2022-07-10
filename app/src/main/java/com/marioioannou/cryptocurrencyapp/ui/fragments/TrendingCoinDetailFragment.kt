package com.marioioannou.cryptocurrencyapp.ui.fragments

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import com.marioioannou.cryptocurrencyapp.databinding.FragmentCoinBinding
import com.marioioannou.cryptocurrencyapp.ui.MainViewModel
import kotlinx.coroutines.Job

class TrendingCoinDetailFragment: Fragment() {
    private lateinit var binding: FragmentCoinBinding

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
    }
}