package com.absut.cash.management.ui.booklist

import android.content.Context
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.ListAdapter
import androidx.recyclerview.widget.RecyclerView
import com.absut.cash.management.data.model.Book
import com.absut.cash.management.R
import com.absut.cash.management.databinding.BookListItemBinding

class BookListAdapter(
    private val listener: OnItemClickListener,
    private val activityContext: Context?
) : ListAdapter<Book, BookListAdapter.BookViewHolder>(DiffCallback()) {

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): BookViewHolder {
        val binding = BookListItemBinding.inflate(LayoutInflater.from(parent.context), parent, false)
        return BookViewHolder(binding)
    }

    override fun onBindViewHolder(holder: BookViewHolder, position: Int) {
        val currentItem = getItem(position)
        holder.bind(currentItem)
    }

    inner class BookViewHolder(private val binding: BookListItemBinding): RecyclerView.ViewHolder(
        binding.root) {
        init {
            binding.apply {
                root.setOnClickListener {
                    val position = adapterPosition
                    if(position != RecyclerView.NO_POSITION) {
                        val book = getItem(position)
                        listener.onItemClick(book)
                    }
                }
                imgOptions.setOnClickListener {
                    val book = getItem(adapterPosition)
                    listener.onOptionClick(imgOptions, book)
                }
            }
        }

        fun bind(book: Book) {
            binding.txtTitle.text = book.title
            binding.txtAmount.text = book.bookAmount.toString()
            if(book.bookAmount>0) {
                binding.txtAmount.setTextColor(binding.parentCard.resources.getColor(R.color.holo_green_dark))
            } else {
                binding.txtAmount.setTextColor(binding.root.resources.getColor(R.color.holo_red_dark))
            }
        }
    }

    interface OnItemClickListener {
        fun onItemClick(book: Book)
        fun onOptionClick(view: View, book: Book)
    }

    class DiffCallback : DiffUtil.ItemCallback<Book>() {
        override fun areItemsTheSame(oldItem: Book, newItem: Book) =
            oldItem.id == newItem.id

        override fun areContentsTheSame(oldItem: Book, newItem: Book) =
            oldItem == newItem
    }
}