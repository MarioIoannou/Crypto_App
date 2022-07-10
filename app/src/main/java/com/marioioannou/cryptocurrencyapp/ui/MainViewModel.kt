package com.marioioannou.cryptocurrencyapp.ui

import android.app.Application
import android.content.Context
import android.net.ConnectivityManager
import android.net.NetworkCapabilities
import android.os.Build
import android.util.Log
import androidx.lifecycle.*
import com.marioioannou.cryptocurrencyapp.CryptoWatchApplication
import com.marioioannou.cryptocurrencyapp.coin_data.model.coin_charts.CoinChart
import com.marioioannou.cryptocurrencyapp.coin_data.model.coin_data.CoinData
import com.marioioannou.cryptocurrencyapp.coin_data.model.coin_news.CoinNews
import com.marioioannou.cryptocurrencyapp.coin_data.model.coin_search.CoinSearch
import com.marioioannou.cryptocurrencyapp.coin_data.model.trending_coins.TrendingCoins
import com.marioioannou.cryptocurrencyapp.coin_data.repository.CoinRepository
import com.marioioannou.cryptocurrencyapp.utils.ScreenState
import kotlinx.coroutines.launch
import okio.IOException
import retrofit2.Response

class MainViewModel(
    application: Application,
    private val repository: CoinRepository
    ): AndroidViewModel(application) {

//    private val _trendingCoins = MutableLiveData<ScreenState<TrendingCoins>>()
//    val trendingCoins: LiveData<ScreenState<TrendingCoins>> = _trendingCoins

//    private val _coinData = MutableLiveData<ScreenState<MutableList<CryptoCoin>>>()
//    val coinData: LiveData<ScreenState<MutableList<CryptoCoin>>> = _coinData
    private val _trendingCoins: MutableLiveData<ScreenState<TrendingCoins>> = MutableLiveData()
    val trendingCoins = _trendingCoins

    private val _coinData: MutableLiveData<ScreenState<CoinData>> = MutableLiveData()
    val coinData = _coinData

    private val _coinSearch: MutableLiveData<ScreenState<CoinSearch>> = MutableLiveData()
    val coinSearch = _coinSearch

    val _coinChart: MutableLiveData<ScreenState<CoinChart>> = MutableLiveData()
    val coinChart = _coinChart

    private val _coinNews: MutableLiveData<ScreenState<CoinNews>> = MutableLiveData()
    val coinNews = _coinNews


    private val TAG = "MainViewModel"

    init {
        Log.e(TAG,"MainViewModel -> init")
        getCryptoCoinData("EUR",2000,0)
        getTrendingCoins()
        getCoinNewsData("trending",20,0)
    }

    // Get data
    fun getTrendingCoins() = viewModelScope.launch {
        connectedTrendingCryptoCoinData()
    }

    fun getCryptoCoinData(currency: String, limit: Int, skip: Int) = viewModelScope.launch {
            connectedCryptoCoinData(currency,limit,skip)
    }

    fun getSearchedCoinData(coinId: String, currency: String) = viewModelScope.launch {
        connectedSearchedCoinData(coinId,currency)
    }

    fun getCoinChartData(coinId: String, currency: String) = viewModelScope.launch {
        connectedCoinChartData(coinId,currency)
    }

    fun getCoinNewsData(filter: String, limit: Int, skip: Int) = viewModelScope.launch {
        connectedCoinNewsData(filter, limit, skip)
    }

         /* Get data depending the connection */
    //CoinData
    private suspend fun connectedCryptoCoinData(currency: String, limit: Int, skip: Int) {
        coinData.postValue(ScreenState.Loading())
        try {
            if (hasInternetConnection()){
                val response = repository.getCoinData(currency, limit, skip)
                coinData.postValue(responseGetCoinData(response))
            }else{
                coinData.postValue(ScreenState.Error(null,"No Internet Connection"))
            }
        }catch (t: Throwable){
            when(t){
                is IOException -> coinData.postValue(ScreenState.Error(null,"Network Failure"))
                else -> coinData.postValue(ScreenState.Error(null,"Something went wrong"))
            }
        }
    }

    //Trending Coin
    private suspend fun connectedTrendingCryptoCoinData() {
        trendingCoins.postValue(ScreenState.Loading())
        try {
            if (hasInternetConnection()){
                val response = repository.getTrendingCoins()
                trendingCoins.postValue(responseGetTrendingCoinData(response))
            }else{
                trendingCoins.postValue(ScreenState.Error(null,"No Internet Connection"))
            }
        }catch (t: Throwable){
            when(t){
                is IOException -> trendingCoins.postValue(ScreenState.Error(null,"Network Failure"))
                else -> trendingCoins.postValue(ScreenState.Error(null,"Something went wrong"))
            }
        }
    }

    //Searched Coin
    private suspend fun connectedSearchedCoinData(coinId: String, currency: String) {
        coinSearch.postValue(ScreenState.Loading())
        try {
            if (hasInternetConnection()){
                val response = repository.getCoinSearch(coinId, currency)
                coinSearch.postValue(responseGetSearchedCoinData(response))
            }else{
                coinSearch.postValue(ScreenState.Error(null,"No Internet Connection"))
            }
        }catch (t: Throwable){
            when(t){
                is IOException -> coinSearch.postValue(ScreenState.Error(null,"Network Failure"))
                else -> coinSearch.postValue(ScreenState.Error(null,"Something went wrong"))
            }
        }
    }

    //Coin Charts
    private suspend fun connectedCoinChartData(coinId: String, period: String) {
        coinChart.postValue(ScreenState.Loading())
        try {
            if (hasInternetConnection()){
                val response = repository.getCoinChart(coinId, period)
                coinChart.postValue(responseGetCoinChartData(response))
            }else{
                coinChart.postValue(ScreenState.Error(null,"No Internet Connection"))
            }
        }catch (t: Throwable){
            when(t){
                is IOException -> coinChart.postValue(ScreenState.Error(null,"Network Failure"))
                else -> coinChart.postValue(ScreenState.Error(null,"Something went wrong"))
            }
        }
    }

    //News
    private suspend fun connectedCoinNewsData(filter: String, limit: Int, skip: Int) {
        coinNews.postValue(ScreenState.Loading())
        try {
            if (hasInternetConnection()){
                val response = repository.getCoinNews(filter, limit, skip)
                coinNews.postValue(responseGetCoinNewsData(response))
            }else{
                coinNews.postValue(ScreenState.Error(null,"No Internet Connection"))
            }
        }catch (t: Throwable){
            when(t){
                is IOException -> coinNews.postValue(ScreenState.Error(null,"Network Failure"))
                else -> coinNews.postValue(ScreenState.Error(null,"Something went wrong"))
            }
        }
    }


        /* Responses */
    private fun responseGetCoinData(response: Response<CoinData>): ScreenState<CoinData>{
        if (response.isSuccessful){
            response.body().let { result ->
                return ScreenState.Success(result)
            }
        }
        return ScreenState.Error(null,response.message())
    }

    private fun responseGetTrendingCoinData(response: Response<TrendingCoins>): ScreenState<TrendingCoins>{
        if (response.isSuccessful){
            response.body().let { result ->
                return ScreenState.Success(result)
            }
        }
        return ScreenState.Error(null,response.message())
    }

    private fun responseGetSearchedCoinData(response: Response<CoinSearch>): ScreenState<CoinSearch>{
        if (response.isSuccessful){
            response.body().let { result ->
                return ScreenState.Success(result)
            }
        }
        return ScreenState.Error(null,response.message())
    }

    private fun responseGetCoinChartData(response: Response<CoinChart>): ScreenState<CoinChart>{
        if (response.isSuccessful){
            response.body().let { result ->
                return ScreenState.Success(result)
            }
        }
        return ScreenState.Error(null,response.message())
    }

    private fun responseGetCoinNewsData(response: Response<CoinNews>): ScreenState<CoinNews>{
        if (response.isSuccessful){
            response.body().let { result ->
                return ScreenState.Success(result)
            }
        }
        return ScreenState.Error(null,response.message())
    }


    // ------ //
    private fun hasInternetConnection(): Boolean {
        val connectivityManager =
            getApplication<CryptoWatchApplication>().getSystemService(Context.CONNECTIVITY_SERVICE) as ConnectivityManager
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
            val activeNetwork = connectivityManager.activeNetwork ?: return false
            val capabilities =
                connectivityManager.getNetworkCapabilities(activeNetwork) ?: return false
            return when {
                capabilities.hasTransport(NetworkCapabilities.TRANSPORT_WIFI) -> true
                capabilities.hasTransport(NetworkCapabilities.TRANSPORT_CELLULAR) -> true
                capabilities.hasTransport(NetworkCapabilities.TRANSPORT_ETHERNET) -> true
                else -> false
            }
        } else {
            connectivityManager.activeNetworkInfo?.run {
                return when (type) {
                    ConnectivityManager.TYPE_WIFI -> true
                    ConnectivityManager.TYPE_MOBILE -> true
                    ConnectivityManager.TYPE_ETHERNET -> true
                    else -> false
                }
            }
        }
        return false
    }
}