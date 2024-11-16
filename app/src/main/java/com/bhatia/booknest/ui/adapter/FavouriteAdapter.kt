package com.bhatia.booknest.ui.adapter

import android.annotation.SuppressLint
import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.bhatia.booknest.databinding.LayoutHorizontalScrollBooksBinding
import com.bhatia.booknest.databinding.LayoutVerticalScrollBooksBinding
import com.bhatia.booknest.model.Books
import com.bumptech.glide.Glide


class FavouriteAdapter(
    private var booksList: List<Books>,
    private val onClick: (Books) -> Unit
) :
    RecyclerView.Adapter<FavouriteAdapter.FavouriteViewHolder>() {
    class FavouriteViewHolder(val itemBinding: LayoutVerticalScrollBooksBinding) :
        RecyclerView.ViewHolder(itemBinding.root)

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): FavouriteViewHolder {
        return FavouriteViewHolder(
            LayoutVerticalScrollBooksBinding.inflate(
                LayoutInflater.from(parent.context),
                parent,
                false
            )
        )
    }

    override fun getItemCount() = booksList.size

    @SuppressLint("SetTextI18n", "SuspiciousIndentation")
    override fun onBindViewHolder(holder: FavouriteViewHolder, position: Int) {
        val books = booksList[position]

        holder.itemView.setOnClickListener {
            onClick(books)
        }
        val imageUrl = books.coverImage
        Glide.with(holder.itemView.context)
            .load(imageUrl)
            .into(holder.itemBinding.coverImageView)
        holder.itemBinding.bookNameTextView.text = books.name
        holder.itemBinding.authorNameTextView.text = books.authorName
        holder.itemBinding.languageTxtView.text = books.language
    }
}
