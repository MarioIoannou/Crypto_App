package com.marioioannou.cryptocurrencyapp.ui.fragments

import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.activity.OnBackPressedCallback
import androidx.navigation.fragment.navArgs
import coil.load
import com.marioioannou.cryptocurrencyapp.R
import com.marioioannou.cryptocurrencyapp.adapters.CoinAdapter
import com.marioioannou.cryptocurrencyapp.databinding.FragmentArticleBinding
import com.marioioannou.cryptocurrencyapp.databinding.FragmentCoinBinding
import com.marioioannou.cryptocurrencyapp.ui.MainActivity
import com.marioioannou.cryptocurrencyapp.ui.MainViewModel


class ArticleFragment : Fragment() {

    private lateinit var binding: FragmentArticleBinding
    private lateinit var coinAdapter: CoinAdapter
    lateinit var viewModel: MainViewModel
    val args: ArticleFragmentArgs by navArgs()

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?,
    ): View? {
        binding = FragmentArticleBinding.inflate(inflater, container, false)
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

        val article = args.article

        binding.imgArticle.load(article.imgURL)
        binding.tvArticleTitle.text = article.title
        binding.tvSource.text = article.source
        binding.tvArticleText.text = article.description

    }

}