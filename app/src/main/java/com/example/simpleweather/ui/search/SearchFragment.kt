package com.example.simpleweather.ui.search

import android.app.SearchManager
import android.content.Context
import android.os.Bundle
import android.view.*
import androidx.appcompat.widget.SearchView
import androidx.core.view.isVisible
import androidx.fragment.app.Fragment
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProvider
import androidx.lifecycle.lifecycleScope
import androidx.navigation.fragment.findNavController
import androidx.recyclerview.widget.LinearLayoutManager
import com.example.simpleweather.R
import com.example.simpleweather.repository.model.LocationWithCoords
import com.example.simpleweather.utils.datawrappers.State
import com.example.simpleweather.utils.reactview.ReactiveViewUtil.Companion.searchWatcherFlow
import dagger.hilt.android.AndroidEntryPoint
import kotlinx.android.synthetic.main.search_fragment.*
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

    private var searchAdapter = SearchLocationsAdapter(this)
    private lateinit var searchView: SearchView
    private lateinit var viewModel: SearchViewModel

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        setHasOptionsMenu(true)
        return inflater.inflate(R.layout.search_fragment, container, false)
    }

    override fun onActivityCreated(savedInstanceState: Bundle?) {
        super.onActivityCreated(savedInstanceState)

        initRecycler()
        initViewModel()
    }

    private fun initViewModel() {
        viewModel = ViewModelProvider(this).get(SearchViewModel::class.java)
        viewModel.locationsLiveData.observe(viewLifecycleOwner, Observer {
            searchAdapter.submitList(it.toList())
        })
        viewModel.stateLiveData.observe(viewLifecycleOwner, Observer {
            setState(it)
        })
    }

    private fun initRecycler() {
        recyclerView_search.layoutManager =
            LinearLayoutManager(context, LinearLayoutManager.VERTICAL, false)
        recyclerView_search.adapter = searchAdapter
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

    private fun setState(state: State) {
        when (state) {
            is State.Default -> {
                setLoadingState(false)
            }

            is State.Loading -> {
                setLoadingState(true)
                setErrorVisibility(false)
            }

            is State.Error -> {
                setLoadingState(false)
                setErrorVisibility(true)
            }

            is State.Success -> {
                setLoadingState(false)
            }
        }
    }

    private fun setLoadingState(isLoading: Boolean) {
        search_progress_bar.isVisible = isLoading
    }

    private fun setErrorVisibility(isVisible: Boolean) {
        text_view_search_error.isVisible = isVisible
    }

    override fun onCreateOptionsMenu(menu: Menu, inflater: MenuInflater) {
        inflater.inflate(R.menu.search_menu, menu)

        val searchManager =
            requireActivity().getSystemService(Context.SEARCH_SERVICE) as SearchManager
        searchView = menu.findItem(R.id.action_search)?.actionView as SearchView
        searchView.setSearchableInfo(searchManager.getSearchableInfo(requireActivity().componentName))
        searchView.maxWidth = Int.MAX_VALUE

//        if (searchQuery != null) searchView.setQuery(searchQuery, true)
        searchView.isIconified = false

        initReactiveViews()
        super.onCreateOptionsMenu(menu, inflater)
    }

    override fun onItemClick(location: LocationWithCoords) {
        val action = SearchFragmentDirections
            .actionSearchFragmentToConditionDetailsFragment(location)
        findNavController().navigate(action)
    }


}