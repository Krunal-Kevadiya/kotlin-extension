package com.kotlinextension.ui.main

import android.databinding.ViewDataBinding
import android.support.v7.util.DiffUtil
import android.support.v7.widget.RecyclerView
import android.view.ViewGroup
import com.extensions.layout.inflateCustomBindView
import com.kotlinextension.BR
import com.kotlinextension.R
import com.kotlinextension.networking.model.success.Item
import java.util.ArrayList

class RepoRecyclerAdapter : RecyclerView.Adapter<RepoRecyclerAdapter.ViewHolder>() {
	private val items = ArrayList<Item>()
	lateinit var listener: OnItemClickListener

	override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
		val bindView: ViewDataBinding = parent.inflateCustomBindView(R.layout.recycler_item_repo)
		return ViewHolder(bindView)
	}

	override fun onBindViewHolder(holder: ViewHolder, position: Int) {
		holder.bind(items[position])
	}

	override fun getItemCount(): Int {
		return items.size
	}

	inner class ViewHolder(private val bindView: ViewDataBinding) : RecyclerView.ViewHolder(bindView.root) {
		fun bind(item: Item) {
			bindView.setVariable(BR.data, item)
			bindView.executePendingBindings()

			if (::listener.isInitialized) {
				itemView.setOnClickListener {
					listener.onItemClick(item)
				}
			}
		}
	}

	fun setItemClickListener(listener: OnItemClickListener) {
		this.listener = listener
	}

	fun updateAll(item: List<Item>) {
		if (items.size <= 0) {
			items.addAll(item)
			notifyDataSetChanged()
		} else {
			val diffResult = DiffUtil.calculateDiff(RepoDiffCallback(this.items, item))
			diffResult.dispatchUpdatesTo(this)
		}
	}

	fun add(item: Item, position: Int) {
		items.add(position, item)
		notifyItemInserted(position)
	}

	fun remove(item: Item) {
		val position = items.indexOf(item)
		items.removeAt(position)
		notifyItemRemoved(position)
	}
}
