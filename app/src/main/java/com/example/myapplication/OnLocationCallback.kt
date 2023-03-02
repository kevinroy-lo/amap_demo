package com.example.myapplication

import android.location.Location
import androidx.core.location.LocationListenerCompat

/**
 * @author kevin
 * @time 2023/2/27 18:07
 * @description
 */

interface OnLocationCallback : LocationListenerCompat {

    fun onFetchedOne(location: Location)
    fun onNoProvider(){}
    fun onRequesting()

    override fun onLocationChanged(location: Location) {
        onFetchedOne(location)
    }
}