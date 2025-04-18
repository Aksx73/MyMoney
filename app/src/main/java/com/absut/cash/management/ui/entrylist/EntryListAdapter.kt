package com.absut.cash.management.ui.entrylist

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.ListAdapter
import androidx.recyclerview.widget.RecyclerView
import com.absut.cash.management.R
import com.absut.cash.management.data.model.Entry
import com.absut.cash.management.databinding.EntryListItemBinding

class EntryListAdapter(
    private val listener: OnItemClickListener
) : ListAdapter<Entry, EntryListAdapter.EntryViewHolder>(DiffCallback()) {

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): EntryListAdapter.EntryViewHolder {
        val binding = EntryListItemBinding.inflate(LayoutInflater.from(parent.context), parent, false)
        return EntryViewHolder(binding)
    }

    override fun onBindViewHolder(holder: EntryListAdapter.EntryViewHolder, position: Int) {
        val currentItem =getItem(position)
        holder.bind(currentItem)
    }

    inner class EntryViewHolder(private val binding: EntryListItemBinding) : RecyclerView.ViewHolder
        (binding.root) {
        init {
            binding.apply {
//                root.setOnClickListener {
//                    val position = adapterPosition
//                    if(position!= RecyclerView.NO_POSITION) {
//                        val entry = getItem(position)
//                        listener.onItemClick(entry)
//                    }
//                }
                binding.imgOptions.setOnClickListener{
                    val entry = getItem(adapterPosition)
                    listener.onOptionsClick(imgOptions, entry)
                }
            }
        }
        fun bind(entry: Entry) {
            binding.apply {
                if(entry.category!= null) {
                    tvCategory.text = entry.category
                } else {
                    tvCategory.text = "---"
                }
                tvAmount.text = entry.entry_amount.toString()
                tvDesc.text = entry.description
                tvUpdatedAt.text = entry.updatedDate
                if (entry.entry_type == 1) {
                    tvAmount.setTextColor(tvAmount.context.getColor(R.color.holo_green_dark))
                } else {
                    tvAmount.setTextColor(tvAmount.context.getColor(R.color.holo_red_dark))
                }
             }
        }
    }

    interface OnItemClickListener {
        fun onItemClick(entry: Entry)
        fun onOptionsClick(view: View, entry: Entry)
    }

 class DiffCallback : DiffUtil.ItemCallback<Entry>() {
  override fun areItemsTheSame(oldItem: Entry, newItem: Entry) =
   oldItem._id == newItem._id

  override fun areContentsTheSame(oldItem: Entry, newItem: Entry) =
   oldItem == newItem
 }

}