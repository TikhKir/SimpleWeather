package com.example.simpleweather.ui.screens.search

import android.app.SearchManager
import android.content.Context
import android.os.Bundle
import android.view.*
import androidx.appcompat.widget.SearchView
import androidx.core.view.isVisible
import androidx.fragment.app.Fragment
import androidx.lifecycle.ViewModelProvider
import androidx.lifecycle.lifecycleScope
import androidx.navigation.fragment.findNavController
import androidx.recyclerview.widget.LinearLayoutManager
import com.example.simpleweather.R
import com.example.simpleweather.databinding.SearchFragmentBinding
import com.example.simpleweather.repository.model.LocationWithCoords
import com.example.simpleweather.utils.datawrappers.State
import com.example.simpleweather.utils.searchWatcherFlow
import dagger.hilt.android.AndroidEntryPoint
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.FlowPreview
import kotlinx.coroutines.channels.Channel.Factory.CONFLATED
import kotlinx.coroutines.flow.buffer
import kotlinx.coroutines.flow.collect
import kotlinx.coroutines.flow.debounce
import kotlinx.coroutines.flow.filter
import kotlinx.coroutines.launch

@ExperimentalCoroutinesApi
@FlowPreview
@AndroidEntryPoint
class SearchFragment : Fragment(), SearchLocationsAdapter.OnItemClickListener {

    companion object {
        fun newInstance() = SearchFragment()
    }

    private var _binding: SearchFragmentBinding? = null
    private val binding get() = _binding!!
    private var searchAdapter = SearchLocationsAdapter(this)
    private lateinit var searchView: SearchView
    private lateinit var viewModel: SearchViewModel

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        setHasOptionsMenu(true)
        _binding = SearchFragmentBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onActivityCreated(savedInstanceState: Bundle?) {
        super.onActivityCreated(savedInstanceState)

        initRecycler()
        initViewModel()
    }

    private fun initViewModel() {
        viewModel = ViewModelProvider(this).get(SearchViewModel::class.java)
        viewModel.locationsLiveData.observe(viewLifecycleOwner, {
            searchAdapter.submitList(it.toList())
        })
        viewModel.stateLiveData.observe(viewLifecycleOwner, {
            setLoadingState(it)
        })
    }

    private fun initRecycler() {
        binding.recyclerViewSearch.layoutManager =
            LinearLayoutManager(context, LinearLayoutManager.VERTICAL, false)
        binding.recyclerViewSearch.adapter = searchAdapter
    }

    private fun initReactiveViews() {
        lifecycleScope.launch {
            searchView.searchWatcherFlow()
                .filter { it.length > 2 }
                .debounce(1000)
                .buffer(CONFLATED)
                .collect {
                    viewModel.searchLocations(it)
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
        binding.searchProgressBar.isVisible = isLoading
        binding.textViewSearchError.isVisible = false
    }

    private fun showErrorMessage(message: String) {
        binding.searchProgressBar.isVisible = false
        binding.textViewSearchError.text = message
        binding.textViewSearchError.isVisible = true
    }

    override fun onCreateOptionsMenu(menu: Menu, inflater: MenuInflater) {
        inflater.inflate(R.menu.search_menu, menu)

        val searchManager =
            requireActivity().getSystemService(Context.SEARCH_SERVICE) as SearchManager
        searchView = menu.findItem(R.id.action_search)?.actionView as SearchView
        searchView.setSearchableInfo(searchManager.getSearchableInfo(requireActivity().componentName))
        searchView.maxWidth = Int.MAX_VALUE
        searchView.isIconified = false

        initReactiveViews()
        super.onCreateOptionsMenu(menu, inflater)
    }

    override fun onItemClick(location: LocationWithCoords) {
        val action = SearchFragmentDirections
            .actionSearchFragmentToConditionDetailsFragment(location)
        findNavController().navigate(action)
    }

    override fun onDestroy() {
        super.onDestroy()
        _binding = null
    }
}