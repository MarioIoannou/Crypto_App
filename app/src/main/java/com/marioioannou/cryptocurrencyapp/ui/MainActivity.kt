package com.marioioannou.cryptocurrencyapp.ui

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import androidx.lifecycle.ViewModelProvider
import androidx.navigation.fragment.NavHostFragment
import androidx.navigation.fragment.findNavController
import androidx.navigation.ui.AppBarConfiguration
import androidx.navigation.ui.setupActionBarWithNavController
import androidx.navigation.ui.setupWithNavController
import com.marioioannou.cryptocurrencyapp.R
import com.marioioannou.cryptocurrencyapp.databinding.ActivityMainBinding
import com.marioioannou.cryptocurrencyapp.coin_data.repository.CoinRepository


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
        val factory = MainViewModelFactory(application,repository)
        viewModel = ViewModelProvider(this,factory)[MainViewModel::class.java]
    }

    private fun fragmentNavigation() {
        val navHostFragment =
            supportFragmentManager.findFragmentById(R.id.nav_host_fragment) as NavHostFragment
        val navController = navHostFragment.findNavController()
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