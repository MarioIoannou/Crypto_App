package com.marioioannou.cryptocurrencyapp.ui

import android.util.Log
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.marioioannou.cryptocurrencyapp.coin_data.model.crypto.CryptoCoin
import com.marioioannou.cryptocurrencyapp.coin_data.repository.CoinRepository
import com.marioioannou.cryptocurrencyapp.utils.ScreenState
import com.marioioannou.newsapp.news_data.model.News
import kotlinx.coroutines.launch
import retrofit2.Response
import retrofit2.http.Query

class MainViewModel(private val repository: CoinRepository):ViewModel() {

    private val _coinData = MutableLiveData<ScreenState<MutableList<CryptoCoin>>>()
    val coinData: LiveData<ScreenState<MutableList<CryptoCoin>>> = _coinData

    val news: MutableLiveData<ScreenState<News>> = MutableLiveData()
    var countryCode: String = "us"
    val pages: Int = 100
    val categoryNews: String = "business"

    private val TAG = "CoinFragmentViewModel"

    init {
        Log.e(TAG,"init")
        getCoinData("eur")
        getNews()
    }

    private fun getCoinData(currency : String) {
        viewModelScope.launch {
            _coinData.postValue(ScreenState.Loading())
            val response = repository.getCoinRepository(currency)
            _coinData.postValue(handleCoinDataResponse(response))
        }
    }

    private fun getNews() {
        viewModelScope.launch {
            news.postValue(ScreenState.Loading())
            val response = repository.getNewsRepository(countryCode, pages, categoryNews)
            news.postValue(handleBreakingNewsResponse(response))
        }
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

    private fun handleCoinDataResponse(response: Response<MutableList<CryptoCoin>>): ScreenState<MutableList<CryptoCoin>> {
        if (response.isSuccessful){
            response.body()?.let{ coinData ->
                return ScreenState.Success(coinData)
            }
        }
        return ScreenState.Error(null,"An Error occurred ${response.message()}")
    }

    private fun handleBreakingNewsResponse(response: Response<News>): ScreenState<News>{
        if(response.isSuccessful){
            response.body()?.let { result ->
                return ScreenState.Success(result)
            }
        }
        return  ScreenState.Error(null,response.message())
    }
}