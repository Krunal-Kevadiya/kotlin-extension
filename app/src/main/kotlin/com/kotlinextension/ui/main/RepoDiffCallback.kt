package com.kotlinextension.ui.main

import android.support.v7.util.DiffUtil
import com.kotlinextension.networking.model.success.Item

class RepoDiffCallback(private var newPersons: List<Item>, private var oldPersons: List<Item>) : DiffUtil.Callback() {
	override fun getOldListSize(): Int {
		return oldPersons.size
	}

	override fun getNewListSize(): Int {
		return newPersons.size
	}

	override fun areItemsTheSame(oldItemPosition: Int, newItemPosition: Int): Boolean {
		return oldPersons[oldItemPosition].id == newPersons[newItemPosition].id
	}

	override fun areContentsTheSame(oldItemPosition: Int, newItemPosition: Int): Boolean {
		return oldPersons[oldItemPosition] == newPersons[newItemPosition]
	}

	override fun getChangePayload(oldItemPosition: Int, newItemPosition: Int): Any? {
		return super.getChangePayload(oldItemPosition, newItemPosition)
	}
}
