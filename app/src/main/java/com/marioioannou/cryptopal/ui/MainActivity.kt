package com.marioioannou.cryptopal.ui

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import androidx.core.view.isVisible
import androidx.lifecycle.ViewModelProvider
import androidx.navigation.NavOptions
import androidx.navigation.fragment.NavHostFragment
import androidx.navigation.fragment.findNavController
import androidx.navigation.ui.AppBarConfiguration
import androidx.navigation.ui.setupActionBarWithNavController
import androidx.navigation.ui.setupWithNavController
import com.marioioannou.cryptopal.R
import com.marioioannou.cryptopal.databinding.ActivityMainBinding
import com.marioioannou.cryptopal.coin_data.repository.CoinRepository
import com.marioioannou.cryptopal.ui.MainViewModel
import com.marioioannou.cryptopal.ui.MainViewModelFactory


class MainActivity : AppCompatActivity() {

    private lateinit var binding: ActivityMainBinding
    lateinit var viewModel: MainViewModel

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityMainBinding.inflate(layoutInflater)
        setContentView(binding.root)

        setSupportActionBar(binding.myToolbar)
        fragmentNavigation()

        binding.myToolbar.setNavigationOnClickListener {
            onBackPressed()
        }

        val repository = CoinRepository()
        val factory = MainViewModelFactory(application, repository)
        viewModel = ViewModelProvider(this, factory)[MainViewModel::class.java]
        val navHostFragment =
            supportFragmentManager.findFragmentById(R.id.nav_host_fragment) as NavHostFragment
        val navController = navHostFragment.findNavController()
        val options = NavOptions.Builder()
            .setLaunchSingleTop(true)
            .setEnterAnim(R.anim.from_left)
            .setExitAnim(R.anim.to_right)
            .setPopEnterAnim(R.anim.from_right)
            .setPopExitAnim(R.anim.to_left)
            .setPopUpTo(navController.graph.startDestinationId, false)
            .build()
        navController.addOnDestinationChangedListener { _, destination, _ ->
            when (destination.id) {

                R.id.coinFragment ->{
                    binding.bottomNavigationView.isVisible = true
                    binding.myToolbar.isVisible = true
                }

                R.id.coinDetailFragment ->{
                    binding.bottomNavigationView.isVisible = false
                    binding.myToolbar.isVisible = true
                }

                R.id.trendingCoinDetailFragment->{
                    binding.bottomNavigationView.isVisible = false
                    binding.myToolbar.isVisible = true
                }

                R.id.watchlistFragment ->{
                    binding.bottomNavigationView.isVisible = true
                    binding.myToolbar.isVisible = false
                }

                R.id.newsFragment ->{
                    binding.bottomNavigationView.isVisible = true
                    binding.myToolbar.isVisible = false
                }

                R.id.articleFragment->{
                    binding.bottomNavigationView.isVisible = false
                    binding.myToolbar.isVisible = true
                }

                R.id.moreFragment ->{
                    binding.bottomNavigationView.isVisible = true
                    binding.myToolbar.isVisible = false
                }

                else -> {

                    binding.bottomNavigationView.isVisible = false
                    binding.myToolbar.isVisible = false

                }
            }
        }
    }

    private fun fragmentNavigation() {
        val navHostFragment =
            supportFragmentManager.findFragmentById(R.id.nav_host_fragment) as NavHostFragment
        val navController = navHostFragment.findNavController()
        val options = NavOptions.Builder()
            .setLaunchSingleTop(true)
            .setEnterAnim(R.anim.from_left)
            .setExitAnim(R.anim.to_right)
            .setPopEnterAnim(R.anim.from_right)
            .setPopExitAnim(R.anim.to_left)
            .setPopUpTo(navController.graph.startDestinationId, false)
            .build()
        val appBarConfiguration = AppBarConfiguration(
            setOf(
                R.id.coinFragment,
                R.id.watchlistFragment,
                R.id.newsFragment,
                R.id.moreFragment
            )
        )
        setupActionBarWithNavController(navController, appBarConfiguration)
        binding.bottomNavigationView.setupWithNavController(navController)
    }
}