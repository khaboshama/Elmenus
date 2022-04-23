package com.khaled.elmenus.feature.home.screen

import android.annotation.SuppressLint
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.ListAdapter
import androidx.recyclerview.widget.RecyclerView
import androidx.recyclerview.widget.RecyclerView.SCROLL_STATE_IDLE
import com.bumptech.glide.Glide
import com.khaled.elmenus.R
import com.khaled.elmenus.feature.home.module.view.BaseHomeItemView
import com.khaled.elmenus.feature.home.module.view.TagFoodItemView
import com.khaled.elmenus.feature.home.module.view.TagItemView
import com.khaled.elmenus.feature.home.module.view.TagsListView
import kotlinx.android.synthetic.main.list_item_child.view.*
import kotlinx.android.synthetic.main.list_item_tag_food.view.*

class HomeAdapter(
    private val onTagItemClick: (TagItemView) -> Unit,
    private val onTagRecyclerViewReachedEnd: () -> Unit
) :
    ListAdapter<BaseHomeItemView, HomeAdapter.BaseHomeViewHolder>(diffCallback) {
    companion object {
        val diffCallback = object : DiffUtil.ItemCallback<BaseHomeItemView>() {
            override fun areItemsTheSame(
                oldItem: BaseHomeItemView,
                newItem: BaseHomeItemView
            ): Boolean = oldItem == newItem

            @SuppressLint("DiffUtilEquals")
            override fun areContentsTheSame(
                oldItem: BaseHomeItemView,
                newItem: BaseHomeItemView
            ): Boolean = oldItem === newItem
        }
    }

    var scrollStateRecyclerViewHashMap = HashMap<Int, Int>()
    override fun onBindViewHolder(holder: BaseHomeViewHolder, position: Int) {
        holder.bind(getItem(position))
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int) = if (viewType == R.layout.list_item_child) {
        TagsViewHolder(LayoutInflater.from(parent.context).inflate(viewType, parent, false))
    } else {
        TagFoodViewHolder(LayoutInflater.from(parent.context).inflate(viewType, parent, false))
    }

    override fun getItemViewType(position: Int): Int {
        return when {
            getItem(position) == null -> R.layout.list_item_child_place_holder
            getItem(position) is TagsListView -> R.layout.list_item_child
            else -> R.layout.list_item_tag_food
        }
    }

    abstract class BaseHomeViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {
        abstract fun bind(item: BaseHomeItemView?)
    }

    inner class TagFoodViewHolder(itemView: View) : BaseHomeViewHolder(itemView) {

        override fun bind(item: BaseHomeItemView?) {
            (item as? TagFoodItemView)?.let { tagFoodItemView ->
                with(itemView) {
                    Glide.with(context).load(tagFoodItemView.photoUrl).fitCenter().into(tagFoodImageView)
                    titleTextView.text = tagFoodItemView.name
                }
            }
        }
    }

    inner class TagsViewHolder(itemView: View) : BaseHomeViewHolder(itemView) {
        val pagerSnapHelper = androidx.recyclerview.widget.PagerSnapHelper()

        override fun bind(item: BaseHomeItemView?) {
            item?.let { tagItemView ->
                setupRecyclerView(tagItemView as TagsListView)
            }
        }

        private fun setupRecyclerView(tagsListView: TagsListView) {
            with(itemView.childRecyclerView) {
                layoutManager = LinearLayoutManager(context, RecyclerView.HORIZONTAL, false)
                setHasFixedSize(true)
                adapter = TagsAdapter(onTagItemClick, onTagRecyclerViewReachedEnd)
                (adapter as TagsAdapter).submitList(tagsListView.list)

                if (onFlingListener == null) pagerSnapHelper.attachToRecyclerView(this)
                val itemPosition = getItemCachedPosition(adapterPosition)
                scrollToPosition(itemPosition)
                tag = adapterPosition
                setTagsOnScrollListener()
            }
        }

        private fun setTagsOnScrollListener() {
            itemView.childRecyclerView.addOnScrollListener(object : RecyclerView.OnScrollListener() {

                override fun onScrollStateChanged(recyclerView: RecyclerView, newState: Int) {
                    super.onScrollStateChanged(recyclerView, newState)
                    if (newState == SCROLL_STATE_IDLE) {
                        if (recyclerView.getChildAt(0) == null) return
                        val view = pagerSnapHelper.findSnapView(recyclerView.layoutManager)
                        if (view != null && recyclerView.layoutManager != null) {
                            saveScrollStateAndNotifyLastItem(view, recyclerView)
                        }
                    }
                }
            })
        }

        private fun saveScrollStateAndNotifyLastItem(
            view: View,
            recyclerView: RecyclerView
        ) {
            val currentItemPosition = recyclerView.layoutManager?.getPosition(view)
            val lastItemPosition = scrollStateRecyclerViewHashMap[recyclerView.tag as Int]
            if (lastItemPosition != null && lastItemPosition != currentItemPosition) {
                (recyclerView.adapter)?.notifyItemChanged(lastItemPosition)
            }
            currentItemPosition?.let { scrollStateRecyclerViewHashMap[recyclerView.tag as Int] = currentItemPosition }
        }

        private fun getItemCachedPosition(adapterPosition: Int) =
            if (scrollStateRecyclerViewHashMap[adapterPosition] == null) 0 else scrollStateRecyclerViewHashMap[adapterPosition]!!
    }
}