package com.khaled.elmenus.feature.home.module.domain

import com.google.gson.annotations.SerializedName

data class TagsResponse(@SerializedName("tags") val tagList: List<TagItem>)
