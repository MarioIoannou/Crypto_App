package com.marioioannou.cryptocurrencyapp.ui.fragments

import android.content.DialogInterface
import android.os.Bundle
import android.util.Log
import android.view.*
import androidx.appcompat.app.AlertDialog
import androidx.core.view.isVisible
import androidx.fragment.app.Fragment
import androidx.lifecycle.Observer
import androidx.recyclerview.widget.LinearLayoutManager
import com.marioioannou.cryptocurrencyapp.R
import com.marioioannou.cryptocurrencyapp.databinding.FragmentCoinBinding
import com.marioioannou.cryptocurrencyapp.ui.MainViewModel
import com.marioioannou.cryptocurrencyapp.adapters.CoinAdapter
import com.marioioannou.cryptocurrencyapp.ui.MainActivity
import com.marioioannou.cryptocurrencyapp.utils.ScreenState

class CoinFragment : Fragment() {

    private lateinit var binding: FragmentCoinBinding
    private lateinit var coinAdapter: CoinAdapter
    lateinit var viewModel: MainViewModel
    private var TAG = "CoinFragment"

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?,
    ): View {
        binding = FragmentCoinBinding.inflate(inflater, container, false)
        setHasOptionsMenu(true)
        setupRecyclerView()
        viewModel = (activity as MainActivity).viewModel //Each fragment
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
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

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

    private fun setupRecyclerView() {
        coinAdapter = CoinAdapter()
        binding.rvCoinRecyclerview.apply {
            adapter = coinAdapter
            layoutManager = LinearLayoutManager(activity)
            setHasFixedSize(true)
        }
    }

    override fun onCreateOptionsMenu(menu: Menu, inflater: MenuInflater) {
        inflater.inflate(R.menu.toolbar_coin_fragment, menu)
    }

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


}