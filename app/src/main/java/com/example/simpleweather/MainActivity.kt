package com.example.simpleweather

import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import androidx.lifecycle.LiveData
import androidx.navigation.NavController
import androidx.navigation.ui.setupActionBarWithNavController
import androidx.preference.PreferenceManager
import androidx.work.*
import com.example.simpleweather.databinding.ActivityMainBinding
import com.example.simpleweather.utils.setupWithNavController
import com.example.simpleweather.utils.worker.BACKGROUND_REFRESH_WORK
import com.example.simpleweather.utils.worker.BackgroundUpdateWorker
import com.example.simpleweather.utils.worker.INTERVAL_PREFERENCE_KEY
import com.example.simpleweather.utils.worker.RefreshPrefValues
import com.google.android.material.bottomnavigation.BottomNavigationView
import dagger.hilt.android.AndroidEntryPoint
import java.util.concurrent.TimeUnit

@AndroidEntryPoint
class MainActivity : AppCompatActivity() {

    private var currentNavController: LiveData<NavController>? = null
    lateinit var binding: ActivityMainBinding
    private lateinit var bottomNavigationView: BottomNavigationView
    private lateinit var workManager : WorkManager

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityMainBinding.inflate(layoutInflater)
        setContentView(binding.root)
        bottomNavigationView = binding.bottomNavigationView

        if (savedInstanceState == null) {
            setupBottomNavigationBar()
            bottomNavigationView.selectedItemId = R.id.nav_graph_home //set default fragment at start
            setupBackgroundRefreshWorker()
        }
    }

    override fun onRestoreInstanceState(savedInstanceState: Bundle) {
        super.onRestoreInstanceState(savedInstanceState)
        setupBottomNavigationBar()
    }

    private fun setupBottomNavigationBar() {
        val navGraphIds = listOf(
            R.navigation.nav_graph_nearby,
            R.navigation.nav_graph_home,
            R.navigation.nav_graph_settings
        )

        val controller = bottomNavigationView.setupWithNavController(
            navGraphIds,
            supportFragmentManager,
            R.id.fragment_container,
            intent
        )

        // Whenever the selected controller changes, setup the action bar.
        controller.observe(this, {
            setupActionBarWithNavController(it)
        })
        currentNavController = controller
    }

    override fun onSupportNavigateUp(): Boolean {
        return currentNavController?.value?.navigateUp() ?: false
    }

    fun setActionBarTitle(title: String) {
        supportActionBar?.title = title
    }


    private fun setupBackgroundRefreshWorker() {
        workManager = WorkManager.getInstance(applicationContext)

        val constraints = Constraints.Builder()
            .setRequiresBatteryNotLow(true)
            .setRequiredNetworkType(NetworkType.CONNECTED)
            .build()

        val periodicRequest = PeriodicWorkRequest
            .Builder(BackgroundUpdateWorker::class.java, getIntervalFromPreferences(), TimeUnit.HOURS)
            .setConstraints(constraints)
            .build()

        workManager.enqueueUniquePeriodicWork(
            BACKGROUND_REFRESH_WORK,
            ExistingPeriodicWorkPolicy.REPLACE,
            periodicRequest
        )
    }

    private fun getIntervalFromPreferences(): Long {
        val refreshValue = PreferenceManager.getDefaultSharedPreferences(applicationContext)
            .getString(INTERVAL_PREFERENCE_KEY, RefreshPrefValues.INTERVAL_24_HOURS.key)

        return when (refreshValue) {
            RefreshPrefValues.INTERVAL_12_HOURS.key -> RefreshPrefValues.INTERVAL_12_HOURS.value
            RefreshPrefValues.INTERVAL_6_HOURS.key -> RefreshPrefValues.INTERVAL_6_HOURS.value
            RefreshPrefValues.INTERVAL_3_HOURS.key -> RefreshPrefValues.INTERVAL_3_HOURS.value
            else -> RefreshPrefValues.INTERVAL_24_HOURS.value
        }


    }


}