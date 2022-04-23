package com.khaled.elmenus.feature.home.screen

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.ListAdapter
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import com.khaled.elmenus.R
import com.khaled.elmenus.feature.home.module.view.TagItemView
import kotlinx.android.synthetic.main.list_item_tag.view.*

class TagsAdapter(
    private val onTagItemClick: (TagItemView) -> Unit,
    private val onTagRecyclerViewReachedEnd: () -> Unit
) :
    ListAdapter<TagItemView, TagsAdapter.BaseTagViewHolder>(diffCallback) {
    companion object {
        val diffCallback = object : DiffUtil.ItemCallback<TagItemView>() {
            override fun areItemsTheSame(
                oldItem: TagItemView,
                newItem: TagItemView
            ): Boolean = oldItem == newItem

            override fun areContentsTheSame(
                oldItem: TagItemView,
                newItem: TagItemView
            ): Boolean = oldItem.tagName == newItem.tagName
        }
    }

    override fun onBindViewHolder(holder: BaseTagViewHolder, position: Int) {
        holder.bind(getItem(position))
        if (position == itemCount - 1) {
            onTagRecyclerViewReachedEnd()
        }
    }


    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): BaseTagViewHolder {
        return TagsViewHolder(LayoutInflater.from(parent.context).inflate(viewType, parent, false))
    }

    override fun getItemViewType(position: Int): Int {
        return if (getItem(position) == null)
            R.layout.list_item_tag_place_holder else R.layout.list_item_tag
    }

    abstract class BaseTagViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {
        abstract fun bind(item: TagItemView?)
    }

    inner class TagsViewHolder(itemView: View) : BaseTagViewHolder(itemView) {
        override fun bind(item: TagItemView?) {
            item?.let { tagItem ->
                with(itemView) {
                    nameTextView.text = tagItem.tagName
                    Glide.with(context).load(tagItem.photoURL).fitCenter().into(tagImageView)
                    setOnClickListener { onTagItemClick(tagItem) }
                }
            }
        }
    }
}