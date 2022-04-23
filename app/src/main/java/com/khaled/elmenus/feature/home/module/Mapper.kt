package com.khaled.elmenus.feature.home.module

import com.khaled.elmenus.feature.home.module.domain.TagItem
import com.khaled.elmenus.feature.home.module.view.TagItemView
import com.khaled.elmenus.feature.home.module.view.TagsListView

object Mapper {

    fun TagItem.toTagItemView() = TagItemView(
        tagName = tagName,
        photoURL = photoURL
    )
    fun List<TagItemView>.toTagListView() = TagsListView(this.toMutableList())

}
