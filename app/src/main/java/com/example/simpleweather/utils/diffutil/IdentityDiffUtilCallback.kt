package com.example.simpleweather.utils.diffutil

import android.annotation.SuppressLint
import androidx.recyclerview.widget.DiffUtil

class IdentityDiffUtilCallback<T : Identified> : DiffUtil.ItemCallback<T>() {
    override fun areItemsTheSame(oldItem: T, newItem: T): Boolean {
        return oldItem.identifier == newItem.identifier
    }

    @SuppressLint("DiffUtilEquals")
    override fun areContentsTheSame(oldItem: T, newItem: T): Boolean {
        return oldItem == newItem
    }


}