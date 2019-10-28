package com.study.zhai.playandroid.ui.activity

import android.annotation.SuppressLint
import android.content.Context
import android.location.Geocoder
import android.location.Location
import android.location.LocationListener
import android.location.LocationManager
import android.os.Bundle
import android.text.TextUtils
import android.util.Log
import com.study.zhai.playandroid.MyApplication
import com.study.zhai.playandroid.base.BaseActivity
import com.study.zhai.playandroid.utils.ZToast
import java.io.IOException




class LocationActivity : BaseActivity() {

    override fun getLayoutId(): Int = com.study.zhai.playandroid.R.layout.activity_location

    override fun initView() {
        testLocationManager()
        testGeocoder()
    }

    private fun testGeocoder() {
        val geocoder = Geocoder(this)
        val flag = Geocoder.isPresent()
        Log.e("gzq", "flag：$flag")
        if (flag) {
            try {
                val addresses = geocoder.getFromLocation(40.059241241347, 116.31835290613, 1)
                if (addresses.size > 0) {
                    val address = addresses[0]
                    val sAddress: String
                    if (!TextUtils.isEmpty(address.locality)) {
                        if (!TextUtils.isEmpty(address.featureName)) {
                            //市和周边地址
                            sAddress = address.locality + " " + address.featureName
                        } else {
                            sAddress = address.locality + address.thoroughfare + address.featureName
                        }
                    } else {
                        sAddress = "定位失败"
                    }
                    Log.e("gzq", "sAddress：$sAddress")
                }
            } catch (e: IOException) {
            }
        }

        val addresses = geocoder.getFromLocationName("西北旺博彦科技", 1)
        if (addresses.size > 0) {
            //返回当前位置，精度可调
            val address = addresses[0]
            if (address != null) {
                Log.e("gzq", "sAddress：" + address.latitude)
                Log.e("gzq", "sAddress：" + address.longitude)
            }
        }
    }

    @SuppressLint("MissingPermission")
    private fun testLocationManager() {
        val locationManager = getSystemService(Context.LOCATION_SERVICE) as LocationManager
//        val list = locationManager.getProviders(true)
//        if (list != null) {
//            for (x in list) {
//                Log.e("gzq", "name:$x")
//            }
//        }
//
//        val lpGps = locationManager.getProvider(LocationManager.GPS_PROVIDER)
//        val lpNet = locationManager.getProvider(LocationManager.NETWORK_PROVIDER)
//        val lpPsv = locationManager.getProvider(LocationManager.PASSIVE_PROVIDER)


//        val criteria = Criteria()
//        // Criteria是一组筛选条件
//        criteria.accuracy = Criteria.ACCURACY_FINE
//        //设置定位精准度
//        criteria.isAltitudeRequired = false
//        //是否要求海拔
//        criteria.isBearingRequired = true
//        //是否要求方向
//        criteria.isCostAllowed = true
//        //是否要求收费
//        criteria.isSpeedRequired = true
//        //是否要求速度
//        criteria.powerRequirement = Criteria.NO_REQUIREMENT
//        //设置电池耗电要求
//        criteria.bearingAccuracy = Criteria.ACCURACY_HIGH
//        //设置方向精确度
//        criteria.speedAccuracy = Criteria.ACCURACY_HIGH
//        //设置速度精确度
//        criteria.horizontalAccuracy = Criteria.ACCURACY_HIGH
//        //设置水平方向精确度
//        criteria.verticalAccuracy = Criteria.ACCURACY_HIGH
//        //设置垂直方向精确度
//
//        //返回满足条件的当前设备可用的provider，第二个参数为false时返回当前设备所有provider中最符合条件的那个provider，但是不一定可用
//        val mProvider = locationManager.getBestProvider(criteria, true)
//        if (mProvider != null) {
//            Log.e("gzq", "mProvider:$mProvider")
//        }

        val provider = locationManager.getProvider(LocationManager.NETWORK_PROVIDER)


        if (locationManager.isProviderEnabled(LocationManager.NETWORK_PROVIDER)) {
            Log.e("gzq", "isProviderEnabled LocationManager.NETWORK_PROVIDER")
            locationManager.requestLocationUpdates(LocationManager.NETWORK_PROVIDER, 5, 1f, MyLocationListener())
        }
    }

    override fun initData() {
    }

    private inner class MyLocationListener : LocationListener {

        override fun onLocationChanged(location: Location?) {
            ZToast.showToast(MyApplication.getInstance(), location.toString())
            Log.e("gzq", "onLocationChanged" + location.toString())
        }

        override fun onStatusChanged(provider: String, status: Int, extras: Bundle) {
            Log.e("gzq", "onStatusChanged$status")
        }

        override fun onProviderEnabled(provider: String) {
            Log.e("gzq", "onProviderEnabled")
        }

        override fun onProviderDisabled(provider: String) {
            Log.e("gzq", "onProviderDisabled")
        }

    }

}