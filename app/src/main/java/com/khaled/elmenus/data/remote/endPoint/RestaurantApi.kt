package com.khaled.elmenus.data.remote.endPoint

import com.khaled.elmenus.feature.home.module.domain.TagFoodListResponse
import com.khaled.elmenus.feature.home.module.domain.TagsResponse
import retrofit2.Response
import retrofit2.http.GET
import retrofit2.http.Path

interface RestaurantApi {
    @GET("tags/{page}")
    suspend fun getTags(
        @Path("page") pageNumber: Int,
    ): Response<TagsResponse>

    @GET("items/{tagName}")
    suspend fun getTagFoodList(
        @Path("tagName") tagName: String,
    ): Response<TagFoodListResponse>
}