package com.example.myapplication

import android.content.Context
import android.util.Log
import com.amap.api.location.AMapLocation
import com.amap.api.location.AMapLocationClient
import com.amap.api.location.AMapLocationClientOption
import com.amap.api.location.AMapLocationListener

/**
 * @author kevin
 * @time 2023/2/28 19:58
 * @description
 */

class AMapLocationProvider(context: Context) : AMapLocationListener {

    companion object {
        val MAP_SUCCESS_TYPE = listOf(
            AMapLocation.LOCATION_TYPE_GPS,
            AMapLocation.LOCATION_TYPE_CELL,
            AMapLocation.LOCATION_TYPE_FIX_CACHE,
            AMapLocation.LOCATION_TYPE_WIFI,
            AMapLocation.LOCATION_TYPE_SAME_REQ,
            AMapLocation.LOCATION_TYPE_COARSE_LOCATION,
        )
    }

    private val client: AMapLocationClient by lazy {
        AMapLocationClient(context)
    }

    private val mLocationOptions: AMapLocationClientOption by lazy {
        AMapLocationClientOption().apply {
            locationMode = AMapLocationClientOption.AMapLocationMode.Hight_Accuracy
            // AMapLocationClientOption.AMapLocationMode.Battery_Saving
            isNeedAddress = false
            isGpsFirst = false
            isOnceLocation = false
            isMockEnable = false
            isWifiScan = true
            isWifiActiveScan = true
            AMapLocationClientOption.setScanWifiInterval(/* p0 = */ 10 * 60 * 1000) /*定位精确扫描提高精度，5分钟扫描一次*/
            isLocationCacheEnable = false // 启用缓存策略，SDK将在设备位置未改变时返回之前相同位置的缓存结果。
            locationPurpose = AMapLocationClientOption.AMapLocationPurpose.Sport
            isOnceLocationLatest = false
            interval = 1000
        }
    }

    private var callback: OnLocationCallback? = null


     fun startLocation(callback: OnLocationCallback) {
        this.callback = callback
//        val networkType = NetworkUtils.getNetworkType()
        mLocationOptions.apply {
            locationMode = AMapLocationClientOption.AMapLocationMode.Device_Sensors
//                if (networkType != null && networkType != NetworkUtils.NetworkType.NETWORK_NO) {
//                    // 高精度定位模式：会同时使用网络定位和GPS定位，优先返回最高精度的定位结果，以及对应的地址描述信息。
//                    // 无网络将会报错
//                    AMapLocationClientOption.AMapLocationMode.Hight_Accuracy
//                } else {
//                    // 仅用设备定位模式：不需要连接网络，只使用GPS进行定位，
//                    // 这种模式下不支持室内环境的定位，需要在室外环境下才可以成功定位。
//                    AMapLocationClientOption.AMapLocationMode.Device_Sensors
//                }
            isSensorEnable = true
        }
//        MLog.w(LoggerService.TAG, "LOCATION_FAILED  = ${mLocationOptions.toString()}")
        client.setLocationOption(mLocationOptions)
        client.setLocationListener(this)
        client.startLocation()
    }

    override fun onLocationChanged(loc: AMapLocation?) {
        loc ?: return
        if (loc.errorCode != AMapLocation.LOCATION_SUCCESS
            || !MAP_SUCCESS_TYPE.contains(loc.locationType)
        ) return Unit.also {
            Log.w("LoggerService.TAG", "LOCATION_FAILED  = ${loc.toStr()}")
            // callback?.onNoProvider()
        }
        if (loc.errorCode == AMapLocation.ERROR_CODE_FAILURE_CONNECTION) {
            return Unit.also {
                Log.w("LoggerService.TAG", "ERROR_CODE_FAILURE_CONNECTION")
                // callback?.onNoProvider()
            }
        }
        callback?.onRequesting()
//        val convertLocation = loc.clone().apply {
//            // 定位SDK在国内返回高德类型坐标，海外定位将返回GPS坐标（海外收费，这里统一转换先）。
//            if (coordType == AMapLocation.COORD_TYPE_GCJ02) {
//                val (cLng, cLat) = CoordinateUtils.gcj02ToWGS84(loc.longitude, loc.latitude)
//                latitude = cLat
//                longitude = cLng
//                coordType = AMapLocation.COORD_TYPE_WGS84
//            }
//        }
//        MLog.w(
//            LoggerService.TAG,
//            "loc= lat:${convertLocation.latitude},long:${convertLocation.longitude},acc:${convertLocation.accuracy}"
//        )
        callback?.onFetchedOne(loc)
    }

     fun stopLocation() {
//        MLog.w(
//            LoggerService.TAG,
//            "amap stopLocation"
//        )
        client.unRegisterLocationListener(this)
        if (client.isStarted) {
            client.stopLocation()
        }
    }
}