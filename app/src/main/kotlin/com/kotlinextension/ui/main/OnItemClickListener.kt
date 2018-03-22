package com.kotlinextension.ui.main

import com.kotlinextension.networking.model.success.Item

interface OnItemClickListener {
	fun onItemClick(item: Item)
}
