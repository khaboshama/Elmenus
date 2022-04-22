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

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ) = FragmentHomeBinding.inflate(inflater, container, false).also { binding = it }.root

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        setListeners()
        setupObserver()
    }

    private fun setListeners() {
    }

    private fun setupObserver() {
    }
}