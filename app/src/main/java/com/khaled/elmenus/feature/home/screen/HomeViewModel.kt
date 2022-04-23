package com.khaled.elmenus.feature.home.screen

import androidx.lifecycle.MutableLiveData
import com.khaled.elmenus.common.BaseViewModel
import com.khaled.elmenus.feature.home.module.Mapper.toTagItemView
import com.khaled.elmenus.feature.home.module.Mapper.toTagListView
import com.khaled.elmenus.feature.home.module.useCase.GetTagsUseCase
import com.khaled.elmenus.feature.home.module.view.BaseHomeItemView
import com.khaled.elmenus.feature.home.module.view.TagItemView
import com.khaled.elmenus.feature.home.module.view.TagsListView
import kotlinx.coroutines.Job

class HomeViewModel(
    private val getTagsUseCase: GetTagsUseCase
) : BaseViewModel() {
    private var job: Job? = null
    private var pageNumber = 1
    val baseHomeViewList = MutableLiveData<MutableList<BaseHomeItemView>?>()
    var isTagsRequestFinished = true

    fun onTagItemClicked(tagItemView: TagItemView) {

    }

    fun refreshTags() {
        pageNumber = 1
        isTagsRequestFinished = true
        job?.cancel()
        getTags(forceRefresh = true)
    }

    init {
        getTags()
    }

    private fun getTags(forceRefresh: Boolean = false) {
        if (isTagsRequestFinished.not()) return
        isTagsRequestFinished = false
        job = wrapBlockingOperation {
            handleResult(getTagsUseCase(pageNumber = pageNumber), onSuccess = { it ->
                if (forceRefresh) baseHomeViewList.value = null
                if (baseHomeViewList.value.isNullOrEmpty()) {
                    baseHomeViewList.value = mutableListOf(it.data.map { it.toTagItemView() }.toTagListView())
                } else {
                    (baseHomeViewList.value?.get(0) as TagsListView).list.addAll(it.data.map { it.toTagItemView() })
                    baseHomeViewList.value = baseHomeViewList.value
                }
                pageNumber++
                isTagsRequestFinished = true
            }, onError = {
                error.value = getErrorMessage(it)!!
                isTagsRequestFinished = true
            })
        }
    }

    fun onTagReachedEnd() {
        getTags()
    }
}