package com.khaled.elmenus.feature.home.screen

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import com.khaled.elmenus.common.BaseFragment
import com.khaled.elmenus.databinding.FragmentHomeBinding
import kotlinx.android.synthetic.main.fragment_home.*

class HomeFragment : BaseFragment<HomeViewModel>() {

    private var binding: FragmentHomeBinding? = null

    override val loadingView: View?
        get() = loadingProgressBar

    private val homeAdapter = HomeAdapter(
        onTagItemClick = { viewModel.onTagItemClicked(it) },
        onTagRecyclerViewReachedEnd = { viewModel.onTagReachedEnd() }
    )

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ) = FragmentHomeBinding.inflate(inflater, container, false).also { binding = it }.root

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        setListeners()
        setupTagsRecyclerView()
        setupObserver()
    }

    private fun setupTagsRecyclerView() {
        parentRecyclerView.adapter = homeAdapter
    }

    private fun setListeners() {
        tagsSwipeRefresh.setOnRefreshListener {
            viewModel.refreshTags()
            tagsSwipeRefresh?.isRefreshing = false
        }
    }

    private fun setupObserver() {
        with(viewModel) {
            baseHomeViewList.observe(viewLifecycleOwner) { list ->
                homeAdapter.submitList(list?.toList())
            }
        }
    }
}