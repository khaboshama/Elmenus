package com.khaled.elmenus.feature.home.screen

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import com.khaled.elmenus.common.BaseViewModel
import com.khaled.elmenus.feature.home.module.Mapper.toTagFoodItemView
import com.khaled.elmenus.feature.home.module.Mapper.toTagItemView
import com.khaled.elmenus.feature.home.module.Mapper.toTagListView
import com.khaled.elmenus.feature.home.module.useCase.GetTagFoodListUseCase
import com.khaled.elmenus.feature.home.module.useCase.GetTagsUseCase
import com.khaled.elmenus.feature.home.module.view.BaseHomeItemView
import com.khaled.elmenus.feature.home.module.view.TagItemView
import com.khaled.elmenus.feature.home.module.view.TagsListView
import kotlinx.coroutines.Job

class HomeViewModel(
    private val getTagsUseCase: GetTagsUseCase,
    private val getTagFoodListUseCase: GetTagFoodListUseCase
) : BaseViewModel() {
    private var job: Job? = null
    private var foodJob: Job? = null
    private var pageNumber = 1
    private val _baseHomeViewList = MutableLiveData<MutableList<BaseHomeItemView>?>()
    val baseHomeViewList: LiveData<MutableList<BaseHomeItemView>?> = _baseHomeViewList
    private var isTagsRequestFinished = true
    private var isTagFoodListRequestFinished = true

    init {
        getTags()
    }

    private fun getTags(forceRefresh: Boolean = false) {
        if (isTagsRequestFinished.not()) return
        isTagsRequestFinished = false
        job = wrapBlockingOperation {
            handleResult(getTagsUseCase(pageNumber = pageNumber), onSuccess = { it ->
                if (forceRefresh) _baseHomeViewList.value = null
                if (_baseHomeViewList.value.isNullOrEmpty()) {
                    _baseHomeViewList.value = mutableListOf(it.data.map { it.toTagItemView() }.toTagListView())
                } else {
                    (_baseHomeViewList.value?.get(0) as TagsListView).list.addAll(it.data.map { it.toTagItemView() })
                    _baseHomeViewList.value = _baseHomeViewList.value
                }
                pageNumber++
                isTagsRequestFinished = true
            }, onError = {
                error.value = getErrorMessage(it)!!
                isTagsRequestFinished = true
            })
        }
    }

    fun onTagItemClicked(tagItemView: TagItemView) {
        getFoodList(tagItemView)
    }

    private fun getFoodList(tagItemView: TagItemView) {
        if (isTagFoodListRequestFinished.not()) return
        isTagFoodListRequestFinished = false
        foodJob?.cancel()
        foodJob = wrapBlockingOperation {
            handleResult(getTagFoodListUseCase(tagName = tagItemView.tagName), onSuccess = { it ->
                if (_baseHomeViewList.value.isNullOrEmpty()) return@handleResult
                if (_baseHomeViewList.value?.filterIsInstance<TagsListView>().isNullOrEmpty()) return@handleResult
                _baseHomeViewList.value = _baseHomeViewList.value?.subList(0,1)
                _baseHomeViewList.value?.addAll(it.data.map { it.toTagFoodItemView() })
                _baseHomeViewList.value = _baseHomeViewList.value
                isTagFoodListRequestFinished = true
            }, onError = {
                error.value = getErrorMessage(it)!!
                isTagFoodListRequestFinished = true
            })
        }
    }

    fun refreshTags() {
        pageNumber = 1
        isTagsRequestFinished = true
        job?.cancel()
        getTags(forceRefresh = true)
    }

    fun onTagReachedEnd() {
        getTags()
    }
}