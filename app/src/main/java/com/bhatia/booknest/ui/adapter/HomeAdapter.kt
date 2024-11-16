package com.bhatia.booknest.ui.adapter

import android.annotation.SuppressLint
import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.bhatia.booknest.databinding.LayoutHorizontalScrollBooksBinding
import com.bhatia.booknest.model.Books
import com.bumptech.glide.Glide


class HomeAdapter(
    private var booksList: List<Books>,
    private val onClick: (Books) -> Unit
) :
    RecyclerView.Adapter<HomeAdapter.HomeViewHolder>() {
    class HomeViewHolder(val itemBinding: LayoutHorizontalScrollBooksBinding) :
        RecyclerView.ViewHolder(itemBinding.root)

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): HomeViewHolder {
        return HomeViewHolder(
            LayoutHorizontalScrollBooksBinding.inflate(
                LayoutInflater.from(parent.context),
                parent,
                false
            )
        )
    }

    override fun getItemCount() = booksList.size

    @SuppressLint("SetTextI18n", "SuspiciousIndentation")
    override fun onBindViewHolder(holder: HomeViewHolder, position: Int) {
        val books = booksList[position]

        holder.itemView.setOnClickListener {
            onClick(books)
        }
        val imageUrl = books.coverImage
        Glide.with(holder.itemView.context)
            .load(imageUrl)
            .into(holder.itemBinding.coverImageView)
        holder.itemBinding.BookNameTextView.text = books.name
        holder.itemBinding.authorNameTextView.text = books.authorName
        holder.itemBinding.languageTextView.text = books.language
    }
}
