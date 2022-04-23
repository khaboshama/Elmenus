package com.khaled.elmenus.feature.home.module

import com.khaled.elmenus.feature.home.module.domain.TagFoodItem
import com.khaled.elmenus.feature.home.module.domain.TagItem
import com.khaled.elmenus.feature.home.module.view.TagFoodItemView
import com.khaled.elmenus.feature.home.module.view.TagItemView
import com.khaled.elmenus.feature.home.module.view.TagsListView

object Mapper {

    fun TagItem.toTagItemView() = TagItemView(
        tagName = tagName,
        photoURL = photoURL
    )
    fun List<TagItemView>.toTagListView() = TagsListView(this.toMutableList())

    fun TagFoodItem.toTagFoodItemView() = TagFoodItemView(
        id = id,
        name = name,
        photoUrl = photoUrl,
        description = description
    )
}
