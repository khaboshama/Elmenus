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
import com.khaled.elmenus.R
import com.khaled.elmenus.feature.home.module.view.BaseHomeItemView
import com.khaled.elmenus.feature.home.module.view.TagItemView
import com.khaled.elmenus.feature.home.module.view.TagsListView
import kotlinx.android.synthetic.main.list_item_child.view.*

class HomeAdapter(
    private val onTagItemClick: (TagItemView) -> Unit,
    private val onHomeRecyclerViewReachedEnd: () -> Unit,
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
            ): Boolean = oldItem == newItem

        }
    }

    var scrollStateRecyclerViewHashMap = HashMap<Int, Int>()
    override fun onBindViewHolder(holder: BaseHomeViewHolder, position: Int) {
        holder.bind(getItem(position))
        if (position == itemCount - 1) onHomeRecyclerViewReachedEnd()
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): BaseHomeViewHolder {
        return TagsViewHolder(LayoutInflater.from(parent.context).inflate(viewType, parent, false))
    }

    override fun getItemViewType(position: Int): Int {
        return if (getItem(position) == null)
            R.layout.list_item_child_place_holder else R.layout.list_item_child
    }

    abstract class BaseHomeViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {
        abstract fun bind(item: BaseHomeItemView?)
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
                        val view: View? = pagerSnapHelper.findSnapView(recyclerView.layoutManager)
                        if (view != null && recyclerView.layoutManager != null) {
                            saveScrollStateAndNotifyLastAttachmentItem(view, recyclerView)
                        }
                    }
                }
            })
        }

        private fun saveScrollStateAndNotifyLastAttachmentItem(
            view: View,
            recyclerView: RecyclerView
        ) {
            val currentItemPosition: Int = recyclerView.layoutManager!!.getPosition(view)
            val lastItemPosition = scrollStateRecyclerViewHashMap[recyclerView.tag as Int]
            if (lastItemPosition != null && lastItemPosition != currentItemPosition) {
                (recyclerView.adapter)?.notifyItemChanged(lastItemPosition)
            }
            scrollStateRecyclerViewHashMap[recyclerView.tag as Int] = currentItemPosition
        }

        private fun getItemCachedPosition(adapterPosition: Int) =
            if (scrollStateRecyclerViewHashMap[adapterPosition] == null) 0 else scrollStateRecyclerViewHashMap[adapterPosition]!!
    }
}