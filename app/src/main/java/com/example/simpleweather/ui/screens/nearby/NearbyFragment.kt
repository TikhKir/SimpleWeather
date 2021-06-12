package com.example.simpleweather.ui.screens.nearby

import android.Manifest
import android.annotation.SuppressLint
import android.os.Build
import android.os.Bundle
import android.os.Looper
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.core.view.isVisible
import androidx.fragment.app.Fragment
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModelProvider
import androidx.navigation.fragment.findNavController
import androidx.recyclerview.widget.LinearLayoutManager
import com.example.simpleweather.R
import com.example.simpleweather.databinding.NearbyFragmentBinding
import com.example.simpleweather.domain.model.Location
import com.example.simpleweather.utils.REQUEST_CODE_LOCATION_PERMISSIONS
import com.example.simpleweather.utils.State
import com.example.simpleweather.utils.easypermissions.LocationUtility
import com.google.android.gms.location.FusedLocationProviderClient
import com.google.android.gms.location.LocationCallback
import com.google.android.gms.location.LocationRequest
import com.google.android.gms.location.LocationRequest.PRIORITY_HIGH_ACCURACY
import com.google.android.gms.location.LocationResult
import dagger.hilt.android.AndroidEntryPoint
import pub.devrel.easypermissions.AppSettingsDialog
import pub.devrel.easypermissions.EasyPermissions

@AndroidEntryPoint
class NearbyFragment : Fragment(), EasyPermissions.PermissionCallbacks, NearbyLocationsAdapter.OnItemClickListener {

    companion object {
        fun newInstance() = NearbyFragment()
    }

    private var _binding: NearbyFragmentBinding? = null
    private val binding get() = _binding!!
    private var isTracking = MutableLiveData<Boolean>()
    private val nearbyLocationsAdapter = NearbyLocationsAdapter(this)
    private lateinit var viewModel: NearbyViewModel
    private lateinit var fusedLocationProviderClient: FusedLocationProviderClient

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        _binding = NearbyFragmentBinding.inflate(inflater, container, false)
        return binding.root
    }


    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        doIfGpsAvailableOrShowErrorMessage {
            fusedLocationProviderClient = FusedLocationProviderClient(requireContext())
        }

        observeViewModel()
        requestPermissions()
        initRecycler()
    }

    private fun observeViewModel() {
        viewModel = ViewModelProvider(this).get(NearbyViewModel::class.java)
        viewModel.state.observe(viewLifecycleOwner, { state ->
            setLoadingState(state)
            doIfGpsAvailableOrShowErrorMessage {
                updateLocationTracking(state is State.Loading || state is State.Default)
            }
        })
        viewModel.locations.observe(viewLifecycleOwner, {
            nearbyLocationsAdapter.submitList(it.toList())
        })
    }

    @SuppressLint("MissingPermission")
    private fun updateLocationTracking(isTracking: Boolean) {
        if (isTracking) {
            if (LocationUtility.hasLocationPermissions(requireContext())) {
                setLoadingState(State.Loading())
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
                setLoadingState(State.Error(getString(R.string.need_location_permissions_message)))
            }
        } else {
            fusedLocationProviderClient.removeLocationUpdates(locationCallback)
        }
    }

    private val locationCallback = object : LocationCallback() {
        override fun onLocationResult(result: LocationResult?) {
            super.onLocationResult(result)
                result?.locations?.let { locations ->
                    for (location in locations) {
                        Log.e("COORDS RECEIVED:", "${location.latitude}, ${location.longitude}")
                        viewModel.loadLocationsByCoords(location.latitude.toFloat(), location.longitude.toFloat())
                        isTracking.postValue(false)
                    }
                }
        }
    }



    private fun setLoadingState(state: State) {
        when (state) {
            is State.Default -> setLoading(false)
            is State.Loading -> setLoading(true)
            is State.Error -> showErrorMessage(state.errorMessage)
            is State.Success -> setLoading(false)
        }
    }

    private fun setLoading(isLoading: Boolean) {
        binding.progressBarNearby.isVisible = isLoading
        binding.textViewNearbyMessage.isVisible = false
    }

    private fun showErrorMessage(message: String) {
        binding.progressBarNearby.isVisible = false
        binding.textViewNearbyMessage.text = message
        binding.textViewNearbyMessage.isVisible = true
    }

    private fun doIfGpsAvailableOrShowErrorMessage(doWork: () -> Unit) {
        if (LocationUtility.isLocationAvailable(requireContext())) {
            doWork()
        } else {
            setLoadingState(State.Error(getString(R.string.turn_on_geolocation)))
        }
    }



    private fun requestPermissions() {
        if (LocationUtility.hasLocationPermissions(requireContext())) return
        if (Build.VERSION.SDK_INT < Build.VERSION_CODES.Q) {
            EasyPermissions.requestPermissions(
                this,
                getString(R.string.need_location_permissions_message),
                REQUEST_CODE_LOCATION_PERMISSIONS,
                Manifest.permission.ACCESS_FINE_LOCATION
            )
        } else {
            EasyPermissions.requestPermissions(
                this,
                getString(R.string.need_location_permissions_message),
                REQUEST_CODE_LOCATION_PERMISSIONS,
                Manifest.permission.ACCESS_FINE_LOCATION
            )
        }
    }

    override fun onPermissionsDenied(requestCode: Int, perms: MutableList<String>) {
        if (EasyPermissions.somePermissionPermanentlyDenied(this, perms)) {
            AppSettingsDialog.Builder(this).build().show()
        } else requestPermissions()
    }

    override fun onPermissionsGranted(requestCode: Int, perms: MutableList<String>) {
        //start tracking manually if permissions granted just now
        doIfGpsAvailableOrShowErrorMessage { updateLocationTracking(true) }
    }

    override fun onRequestPermissionsResult(requestCode: Int, permissions: Array<out String>, grantResults: IntArray) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults)
        EasyPermissions.onRequestPermissionsResult(requestCode, permissions, grantResults, this)
    }



    private fun initRecycler() {
        binding.recycleViewNearby.layoutManager =
            LinearLayoutManager(context, LinearLayoutManager.VERTICAL, false)
        binding.recycleViewNearby.adapter = nearbyLocationsAdapter
    }

    override fun onItemClick(location: Location) {
        val action = NearbyFragmentDirections
            .actionNearbyFragmentToConditionDetailsFragment(location)
        findNavController().navigate(action)
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }
}