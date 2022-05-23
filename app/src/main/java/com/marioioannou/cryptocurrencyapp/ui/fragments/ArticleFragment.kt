package com.marioioannou.cryptocurrencyapp.ui.fragments

import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import com.marioioannou.cryptocurrencyapp.R
import com.marioioannou.cryptocurrencyapp.adapters.CoinAdapter
import com.marioioannou.cryptocurrencyapp.databinding.FragmentArticleBinding
import com.marioioannou.cryptocurrencyapp.databinding.FragmentCoinBinding
import com.marioioannou.cryptocurrencyapp.ui.MainViewModel


class ArticleFragment : Fragment() {

    private lateinit var binding: FragmentArticleBinding
    private lateinit var coinAdapter: CoinAdapter
    lateinit var viewModel: MainViewModel

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?,
    ): View? {
        binding = FragmentArticleBinding.inflate(inflater, container, false)
        return binding.root
    }

}