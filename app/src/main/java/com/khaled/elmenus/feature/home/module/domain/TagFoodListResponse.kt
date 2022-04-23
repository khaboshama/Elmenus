package com.khaled.elmenus.feature.home.module.domain

import com.google.gson.annotations.SerializedName

data class TagFoodListResponse(@SerializedName("items") val tagFoodList: List<TagFoodItem>)
