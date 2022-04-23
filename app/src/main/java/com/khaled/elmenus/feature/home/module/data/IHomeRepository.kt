package com.khaled.elmenus.feature.home.module.data

import com.khaled.elmenus.common.data.AppResult
import com.khaled.elmenus.common.data.IBaseRepository
import com.khaled.elmenus.feature.home.module.domain.TagFoodItem
import com.khaled.elmenus.feature.home.module.domain.TagItem

interface IHomeRepository : IBaseRepository {
    suspend fun getTags(pageNumber: Int): AppResult<List<TagItem>>
    suspend fun getTagFoodList(tagName: String): AppResult<List<TagFoodItem>>
}