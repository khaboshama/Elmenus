package com.khaled.elmenus.common

import android.app.Activity
import android.os.Bundle
import android.view.View
import androidx.fragment.app.Fragment
import androidx.navigation.fragment.findNavController
import org.koin.android.ext.android.getKoin
import org.koin.androidx.viewmodel.ViewModelOwner
import org.koin.androidx.viewmodel.koin.getViewModel

abstract class BaseFragment<ViewModel : BaseViewModel> : Fragment(), BaseView<ViewModel> {
    override val viewModel by lazy {
        getKoin().getViewModel(
            owner = { ViewModelOwner.from(this) },
            clazz = viewModelClass()
        )
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        viewModel.error.observe(viewLifecycleOwner) { it?.let { showErrorMessage(it) } }
        viewModel.showLoading.observe(viewLifecycleOwner) { it?.let { if (it) showLoading() else hideLoading() } }
    }

    override fun getCurrentActivity(): Activity = requireActivity()

    protected fun isDestinationFound(currentFragmentId: Int) = findNavController().currentDestination?.id == currentFragmentId
}