package com.example.simpleweather

import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import androidx.lifecycle.LiveData
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProvider
import androidx.navigation.NavController
import androidx.navigation.ui.setupActionBarWithNavController
import com.example.simpleweather.utils.setupWithNavController
import com.google.android.material.bottomnavigation.BottomNavigationView
import dagger.hilt.android.AndroidEntryPoint

@AndroidEntryPoint
class MainActivity : AppCompatActivity() {

    private lateinit var viewModel: MainViewModel
    private var currentNavController: LiveData<NavController>? = null

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        if (savedInstanceState == null) setupBottomNavigationBar()
        // Else, need to wait for onRestoreInstanceState

        viewModel = ViewModelProvider(this).get(MainViewModel::class.java)
    }

    override fun onRestoreInstanceState(savedInstanceState: Bundle?) {
        super.onRestoreInstanceState(savedInstanceState)
        // Now that BottomNavigationBar has restored its instance state
        // and its selectedItemId, we can proceed with setting up the
        // BottomNavigationBar with Navigation
        setupBottomNavigationBar()
    }

    private fun setupBottomNavigationBar() {
        val bottomNavigationView = findViewById<BottomNavigationView>(R.id.bottomNavigationView)

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
        controller.observe(this, Observer {
            setupActionBarWithNavController(it)
        })
        currentNavController = controller

    }

    override fun onSupportNavigateUp(): Boolean {
        return currentNavController?.value?.navigateUp() ?: false
    }

//    private fun navigateToNearbyFragmentIfNeeded(intent: Intent?) {
//        if(intent?.action == ACTION_SHOW_NEARBY_FRAGMENT) {
//            //currentNavController?.value?.navigate(R.id.action_global_nearby_fragment)
//            fragment_container.findNavController().navigate(R.id.action_global_nearby_fragment)
//            //todo: разобраться как работать с навигацией с йоба-расширением
//        }
//    }

//    override fun onNewIntent(intent: Intent?) {
//        super.onNewIntent(intent)
//        navigateToNearbyFragmentIfNeeded(intent)
//    }
}