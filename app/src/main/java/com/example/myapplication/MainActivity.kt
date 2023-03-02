package com.example.myapplication

import android.location.Location
import android.os.Bundle
import android.util.Log
import androidx.appcompat.app.AppCompatActivity
import com.amap.api.location.AMapLocationClient

class MainActivity : AppCompatActivity() {

    lateinit var provider: AMapLocationProvider
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)
        AMapLocationClient.updatePrivacyShow(applicationContext, true, true)
        AMapLocationClient.updatePrivacyAgree(applicationContext, true)
        provider = AMapLocationProvider(this)


    }

    override fun onStart() {
        super.onStart()
        provider.startLocation(object : OnLocationCallback {
            override fun onFetchedOne(location: Location) {
                Log.i("Tag", "loc :${location.latitude},${location.longitude}")
            }

            override fun onRequesting() {

            }
        })
    }

    override fun onPause() {
        super.onPause()
        provider.stopLocation()
    }
}