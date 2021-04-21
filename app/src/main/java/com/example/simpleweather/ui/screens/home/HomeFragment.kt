package com.example.simpleweather.ui.screens.home

import android.os.Bundle
import android.view.*
import androidx.fragment.app.Fragment
import androidx.lifecycle.ViewModelProvider
import androidx.navigation.fragment.findNavController
import androidx.recyclerview.widget.LinearLayoutManager
import com.example.simpleweather.R
import com.example.simpleweather.databinding.HomeFragmentBinding
import com.example.simpleweather.repository.model.LocationWithCoords
import dagger.hilt.android.AndroidEntryPoint

@AndroidEntryPoint
class HomeFragment : Fragment(), SavedLocationsAdapter.OnItemClickListener {

    companion object {
        fun newInstance() = HomeFragment()
    }

    private var _binding: HomeFragmentBinding? = null
    private val binding get() = _binding!!

    private lateinit var viewModel: HomeViewModel
    private val savedLocationsAdapter = SavedLocationsAdapter(this)

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        setHasOptionsMenu(true)
        _binding = HomeFragmentBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        initViewModel()
        initRecycler()
    }

    private fun initViewModel() {
        viewModel = ViewModelProvider(this).get(HomeViewModel::class.java)
        viewModel.locationLiveData.observe(this.viewLifecycleOwner, {
            savedLocationsAdapter.submitList(it.toList())
        })
    }

    private fun initRecycler() {
        binding.recycleViewHome.layoutManager =
            LinearLayoutManager(context, LinearLayoutManager.VERTICAL, false)
        binding.recycleViewHome.adapter = savedLocationsAdapter
    }


    override fun onCreateOptionsMenu(menu: Menu, inflater: MenuInflater) {
        inflater.inflate(R.menu.home_top_menu, menu)
        return super.onCreateOptionsMenu(menu, inflater)
    }

    override fun onOptionsItemSelected(item: MenuItem): Boolean {
        when(item.itemId) {
            R.id.menu_item_home_add_location -> {
                val action = HomeFragmentDirections.actionHomeFragmentToSearchFragment()
                findNavController().navigate(action)
            }
        }
        return super.onOptionsItemSelected(item)
    }

    override fun onItemClicked(location: LocationWithCoords) {
        val action = HomeFragmentDirections
            .actionHomeFragmentToConditionDetailsFragment(location)
        findNavController().navigate(action) //if use R.id.* for navigate it will not runtime safety
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }

}