package com.example.simpleweather.ui.nearby

import android.Manifest
import android.annotation.SuppressLint
import android.os.Build
import android.os.Bundle
import android.os.Looper
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProvider
import androidx.recyclerview.widget.LinearLayoutManager
import com.example.simpleweather.R
import com.example.simpleweather.utils.Constants.REQUEST_CODE_LOCATION_PERMISSIONS
import com.example.simpleweather.utils.easypermissions.LocationUtility
import com.google.android.gms.location.FusedLocationProviderClient
import com.google.android.gms.location.LocationCallback
import com.google.android.gms.location.LocationRequest
import com.google.android.gms.location.LocationRequest.PRIORITY_HIGH_ACCURACY
import com.google.android.gms.location.LocationResult
import dagger.hilt.android.AndroidEntryPoint
import kotlinx.android.synthetic.main.nearby_fragment.*
import pub.devrel.easypermissions.AppSettingsDialog
import pub.devrel.easypermissions.EasyPermissions

@AndroidEntryPoint
class NearbyFragment : Fragment(), EasyPermissions.PermissionCallbacks {

    companion object {
        fun newInstance() = NearbyFragment()
    }

    private var isTracking = MutableLiveData<Boolean>()
    private val nearbyLocationsAdapter = NearbyLocationsAdapter()
    private lateinit var viewModel: NearbyViewModel
    private lateinit var fusedLocationProviderClient: FusedLocationProviderClient

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        return inflater.inflate(R.layout.nearby_fragment, container, false)
    }

    override fun onActivityCreated(savedInstanceState: Bundle?) {
        super.onActivityCreated(savedInstanceState)

        initViewModel()
        requestPermissions()
        initLocationChecker()
        initRecycler()
    }

    private fun initViewModel() {
        viewModel = ViewModelProvider(this).get(NearbyViewModel::class.java)
        viewModel.locationsLiveData.observe(viewLifecycleOwner, Observer {
            nearbyLocationsAdapter.submitList(it.toList())
        })
    }

    private fun initRecycler() {
        recycleView_nearby.layoutManager =
            LinearLayoutManager(context, LinearLayoutManager.VERTICAL, false)
        recycleView_nearby.adapter = nearbyLocationsAdapter
    }






    @SuppressLint("MissingPermission")
    private fun updateLocationTracking(isTracking: Boolean) {
        if (isTracking) {
            if (LocationUtility.hasLocationPermissions(requireContext())) {
                val request = LocationRequest().apply {
                    interval = 5000
                    fastestInterval = 2000
                    priority = PRIORITY_HIGH_ACCURACY
                }
                fusedLocationProviderClient.requestLocationUpdates(
                    request,
                    locationCallback,
                    Looper.getMainLooper()
                )
            } else {
                Log.e("PERMISSION", "DENIED")
            }
        } else {
            fusedLocationProviderClient.removeLocationUpdates(locationCallback)
        }
    }

    private val locationCallback = object : LocationCallback() {
        override fun onLocationResult(result: LocationResult?) {
            super.onLocationResult(result)
            if (isTracking.value!!) {
                result?.locations?.let { locations ->
                    for (location in locations) {
                        Log.e("LOCATION CHECK GPS", "${location.latitude}, ${location.longitude}")
                        viewModel.loadLocationsByCoords(location.latitude.toFloat(), location.longitude.toFloat())
                        isTracking.postValue(false)
                    }
                }
            }
        }
    }

    private fun initLocationChecker() {
        fusedLocationProviderClient = FusedLocationProviderClient(requireContext())
        isTracking.observe(viewLifecycleOwner, Observer {
            updateLocationTracking(it)
        })
    }

    private fun requestPermissions() {
        if (LocationUtility.hasLocationPermissions(requireContext())) return
        if (Build.VERSION.SDK_INT < Build.VERSION_CODES.Q) {
            EasyPermissions.requestPermissions(
                this,
                getString(R.string.need_location_permissions_message),
                REQUEST_CODE_LOCATION_PERMISSIONS,
                Manifest.permission.ACCESS_COARSE_LOCATION,
                Manifest.permission.ACCESS_FINE_LOCATION
            )
        } else {
            EasyPermissions.requestPermissions(
                this,
                getString(R.string.need_location_permissions_message),
                REQUEST_CODE_LOCATION_PERMISSIONS,
                Manifest.permission.ACCESS_COARSE_LOCATION,
                Manifest.permission.ACCESS_FINE_LOCATION,
                Manifest.permission.ACCESS_BACKGROUND_LOCATION
            )
        }
    }

    override fun onPermissionsDenied(requestCode: Int, perms: MutableList<String>) {
        if (EasyPermissions.somePermissionPermanentlyDenied(this, perms)) {
            AppSettingsDialog.Builder(this).build().show()
        } else requestPermissions()
    }

    override fun onPermissionsGranted(requestCode: Int, perms: MutableList<String>) {
        //not need implement
    }

    override fun onRequestPermissionsResult(requestCode: Int, permissions: Array<out String>, grantResults: IntArray) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults)
        EasyPermissions.onRequestPermissionsResult(requestCode, permissions, grantResults, this)
    }

    override fun onResume() {
        super.onResume()
        isTracking.postValue(true)
    }

    override fun onPause() {
        super.onPause()
        isTracking.postValue(false)
    }

//    private fun sendCommandToService(action: String) {
//        Intent(requireContext(), LocationService::class.java).also {
//            it.action = action
//            requireContext().startService(it)
//        }
//    }
}