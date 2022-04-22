package com.khaled.elmenus.feature.home.module

import com.khaled.elmenus.feature.home.module.domain.TagItem
import com.khaled.elmenus.feature.home.module.domain.TagItemView

object Mapper {

    fun TagItem.toTagItemView() = TagItemView(
        tagName = tagName,
        photoURL = photoURL
    )
}
