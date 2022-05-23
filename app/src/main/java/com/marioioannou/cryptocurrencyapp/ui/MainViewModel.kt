package com.marioioannou.cryptocurrencyapp.ui

import android.util.Log
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.marioioannou.cryptocurrencyapp.coin_data.model.crypto.CryptoCoin
import com.marioioannou.cryptocurrencyapp.coin_data.model.cryptosearch.CoinSearch
import com.marioioannou.cryptocurrencyapp.coin_data.model.trending_coins.TrendingCoins
import com.marioioannou.cryptocurrencyapp.coin_data.repository.CoinRepository
import com.marioioannou.cryptocurrencyapp.utils.ScreenState
import com.marioioannou.newsapp.news_data.model.News
import kotlinx.coroutines.launch
import retrofit2.Response

class MainViewModel(private val repository: CoinRepository):ViewModel() {

    private val _trendingCoins = MutableLiveData<ScreenState<TrendingCoins>>()
    val trendingCoins: LiveData<ScreenState<TrendingCoins>> = _trendingCoins

    private val _coinData = MutableLiveData<ScreenState<MutableList<CryptoCoin>>>()
    val coinData: LiveData<ScreenState<MutableList<CryptoCoin>>> = _coinData
    var page = 1

    private val _searchCoin = MutableLiveData<ScreenState<CoinSearch>>()
    val searchCoin: LiveData<ScreenState<CoinSearch>> = _searchCoin

    private val _news = MutableLiveData<ScreenState<News>>()
    val news: LiveData<ScreenState<News>> = _news

    var countryCode: String = "us"
    val newsPages: Int = 100
    val categoryNews: String = "business"

    private val TAG = "CoinFragmentViewModel"

    init {
        Log.e(TAG,"init")
        getCoinData("eur")
        getNews()
        getTrendingCoins()
    }

    private fun getTrendingCoins() {
        viewModelScope.launch {
            _trendingCoins.postValue(ScreenState.Loading())
            val response = repository.getTrendingCoinsRepository()
            _trendingCoins.postValue(handleTrendingCoinsResponse(response))
        }
    }

    private fun getCoinData(currency : String) {
        viewModelScope.launch {
            _coinData.postValue(ScreenState.Loading())
            val response = repository.getCoinRepository(currency,1)
            _coinData.postValue(handleCoinDataResponse(response))
        }
    }

    fun getSearchCoin(searchQuery: String) {
        viewModelScope.launch {
            _searchCoin.postValue(ScreenState.Loading())
            val response = repository.getSearchedCoinsRepository(searchQuery)
            _searchCoin.postValue(handleSearchCoinResponse(response))
        }
    }

    private fun getNews() {
        viewModelScope.launch {
            _news.postValue(ScreenState.Loading())
            val response = repository.getNewsRepository(countryCode, newsPages, categoryNews)
            _news.postValue(handleNewsResponse(response))
        }
    }

    //Handles
    private fun handleTrendingCoinsResponse(response: Response<TrendingCoins>): ScreenState<TrendingCoins> {
        if (response.isSuccessful){
            response.body()?.let{ trendingCoin ->
                return ScreenState.Success(trendingCoin)
            }
        }
        return ScreenState.Error(null,"An Error occurred ${response.message()}")
    }

    private fun handleCoinDataResponse(response: Response<MutableList<CryptoCoin>>): ScreenState<MutableList<CryptoCoin>> {
        if (response.isSuccessful){
            response.body()?.let{ coinData ->
                return ScreenState.Success(coinData)
            }
        }
        return ScreenState.Error(null,"An Error occurred ${response.message()}")
    }

    private fun handleNewsResponse(response: Response<News>): ScreenState<News>{
        if(response.isSuccessful){
            response.body()?.let { result ->
                return ScreenState.Success(result)
            }
        }
        return  ScreenState.Error(null,response.message())
    }

    private fun handleSearchCoinResponse(response: Response<CoinSearch>): ScreenState<CoinSearch> {
        if (response.isSuccessful){
            response.body()?.let{ search ->
                return ScreenState.Success(search)
            }
        }
        return ScreenState.Error(null,"An Error occurred ${response.message()}")
    }

    //        Log.e(TAG,"getCoinData()")
//        _coinData.postValue(ScreenState.Loading())
//        viewModelScope.launch {
//            try{
//                Log.e(TAG,"getCoinData() try")
//                val response = repository.getCoinRepository()
//                Log.e(TAG,"getCoinData() try repo")
//                if(response.isSuccessful){
//                    Log.e(TAG,"getCoinData() if success")
//                    _coinData.postValue(ScreenState.Success(response))
//                }else{
//                    Log.e(TAG,"getCoinData() else success")
//                    _coinData.postValue(ScreenState.Error(null,response.message().toString()))
//                }
//            }catch (e:Exception){
//                Log.e(TAG,"getCoinData() catch")
//                _coinData.postValue(ScreenState.Error(null,"An error occurred"))
//            }
//        }
//    }
}