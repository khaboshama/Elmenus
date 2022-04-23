package com.khaled.elmenus.feature.home.module.view

data class TagFoodItemView(
    val id: Int,
    val name: String,
    val photoUrl: String,
    val description: String
): BaseHomeItemView()