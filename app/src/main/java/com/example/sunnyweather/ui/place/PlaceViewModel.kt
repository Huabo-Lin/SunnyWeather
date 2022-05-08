package com.example.sunnyweather.ui.place

import android.util.Log
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.Transformations
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.sunnyweather.logic.Repository
import com.example.sunnyweather.logic.model.Place
import com.example.sunnyweather.logic.model.PlaceResponse
import com.example.sunnyweather.logic.network.PlaceService
import com.example.sunnyweather.logic.network.ServiceCreator
import com.example.sunnyweather.logic.network.SunnyWeatherNetwork
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.*
import kotlinx.coroutines.flow.SharingStarted.Companion.WhileSubscribed
import kotlinx.coroutines.launch

class PlaceViewModel : ViewModel() {

    private val placeService by lazy { ServiceCreator.create(PlaceService::class.java) }

    private val searchData = MutableSharedFlow<String>()

    val placeList = ArrayList<Place>()


    fun searchPlaces(query: String) {
        viewModelScope.launch {
            Log.e("lhb", "query ${Thread.currentThread().name}")
            searchData.emit(query)
        }
    }

    val placeFlow = searchData.mapLatest { query ->
        Log.e("lhb", "searchPlacesF ${Thread.currentThread().name}")
        placeService.searchPlacesF(query)
    }.flowOn(Dispatchers.IO).shareIn(
        scope = viewModelScope,
        started = WhileSubscribed(5000)
    )

}