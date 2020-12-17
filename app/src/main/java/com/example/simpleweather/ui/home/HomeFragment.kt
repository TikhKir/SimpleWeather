package com.example.simpleweather.ui.home

import android.os.Bundle
import android.view.*
import androidx.fragment.app.Fragment
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProvider
import androidx.navigation.findNavController
import androidx.navigation.fragment.findNavController
import androidx.recyclerview.widget.LinearLayoutManager
import com.example.simpleweather.R
import com.example.simpleweather.repository.model.LocationWithCoords
import dagger.hilt.android.AndroidEntryPoint
import kotlinx.android.synthetic.main.activity_main.*
import kotlinx.android.synthetic.main.home_fragment.*

@AndroidEntryPoint
class HomeFragment : Fragment(), SavedLocationsAdapter.OnItemClickListener {

    companion object {
        fun newInstance() = HomeFragment()
    }

    private lateinit var viewModel: HomeViewModel
    private val savedLocationsAdapter = SavedLocationsAdapter(this)

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        return inflater.inflate(R.layout.home_fragment, container, false)
    }

    override fun onActivityCreated(savedInstanceState: Bundle?) {
        super.onActivityCreated(savedInstanceState)
        setHasOptionsMenu(true)
        initViewModel()
        initRecycler()
    }

    private fun initViewModel() {
        viewModel = ViewModelProvider(this).get(HomeViewModel::class.java)
        viewModel.locationLiveData.observe(this.viewLifecycleOwner, Observer {
            savedLocationsAdapter.submitList(it.toList())
        })
    }

    private fun initRecycler() {
        recycleView_home.layoutManager =
            LinearLayoutManager(context, LinearLayoutManager.VERTICAL, false)
        recycleView_home.adapter = savedLocationsAdapter
    }


    override fun onCreateOptionsMenu(menu: Menu, inflater: MenuInflater) {
        inflater.inflate(R.menu.home_top_menu, menu)
        return super.onCreateOptionsMenu(menu, inflater)
    }

    override fun onOptionsItemSelected(item: MenuItem): Boolean {
        when(item.itemId) {
            R.id.menu_item_home_add_location -> {
            requireActivity().fragment_container.findNavController()
                .navigate(R.id.action_homeFragment_to_searchFragment)
            }
        }
        return super.onOptionsItemSelected(item)
    }

    override fun onItemClicked(location: LocationWithCoords) {
        val action = HomeFragmentDirections
            .actionHomeFragmentToConditionDetailsFragment(location)
        findNavController().navigate(action) //if use R.id.* for navigate it will not runtime safety

        //requireActivity().fragment_container.findNavController().navigate(R.id.action_homeFragment_to_conditionDetailsFragment)
        //Toast.makeText(requireContext(), location.addressCity, Toast.LENGTH_SHORT).show()
    }

}