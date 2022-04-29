package com.marioioannou.cryptocurrencyapp.ui.fragments

import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import com.marioioannou.cryptocurrencyapp.databinding.FragmentMoreBinding
import com.marioioannou.cryptocurrencyapp.ui.MainViewModel
import com.marioioannou.cryptocurrencyapp.ui.MainActivity


class MoreFragment : Fragment() {

    private lateinit var binding: FragmentMoreBinding
    lateinit var viewModel: MainViewModel

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?,
    ): View? {
        binding = FragmentMoreBinding.inflate(inflater,container,false)
        viewModel = (activity as MainActivity).viewModel

        return  binding.root
    }

}