package com.khaled.elmenus.feature.home.module.data

import com.khaled.elmenus.common.data.AppResult
import com.khaled.elmenus.common.data.HttpUtils
import com.khaled.elmenus.data.remote.RetrofitClient
import com.khaled.elmenus.feature.home.module.domain.TagItem

class HomeRepository : IHomeRepository {

    override suspend fun getTags(pageNumber: Int): AppResult<List<TagItem>> {
        val errorAppResult: AppResult.Error?
        return when (val result =
            HttpUtils.safeApiCall {
                RetrofitClient.homeApi.getTags(
                    pageNumber = pageNumber
                )
            }) {
            is AppResult.Success -> AppResult.Success(result.data.tagList)
            else -> {
                errorAppResult = result as AppResult.Error
                getErrorAppResult(errorAppResult.errorMessage, errorAppResult.errorMessageRes)
            }
        }
    }

    private fun getErrorAppResult(errorMessage: String?, errorMessageRes: Int?) = AppResult.Error(
        errorMessage = errorMessage,
        errorMessageRes = errorMessageRes
    )
}