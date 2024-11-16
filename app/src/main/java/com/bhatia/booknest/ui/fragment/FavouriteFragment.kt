package com.bhatia.booknest.ui.fragment

import android.os.Bundle
import android.util.Log
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.lifecycle.ViewModelProvider
import androidx.navigation.fragment.findNavController
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.bhatia.booknest.R
import com.bhatia.booknest.databinding.FragmentFavouriteBinding
import com.bhatia.booknest.model.Books
import com.bhatia.booknest.ui.adapter.FavouriteAdapter
import com.bhatia.booknest.viewmodel.AppViewModel


class FavouriteFragment : Fragment() {

    private lateinit var binding: FragmentFavouriteBinding
    private lateinit var viewModel: AppViewModel

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        binding = FragmentFavouriteBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        viewModel = ViewModelProvider(requireActivity())[AppViewModel::class.java]
        viewModel.getBooks()
        viewModel.getFavouriteBooks()
        viewModel.books.observe(viewLifecycleOwner) { books ->
            viewModel.favouriteBooks.observe(viewLifecycleOwner) { favBooks ->
                val favBookList = books.filter { book -> favBooks.contains(book.name) }
                binding.emptyText.visibility =
                    if (favBookList.isEmpty()) View.VISIBLE else View.GONE
                setupRecyclerView(favBookList, binding.favouriteRecyclerView)
            }
        }

    }

    private fun setupRecyclerView(booksData: List<Books>, recyclerView: RecyclerView) {
        recyclerView.layoutManager =
            LinearLayoutManager(requireContext())
        recyclerView.adapter = FavouriteAdapter(booksData) { selectedBook ->
            findNavController().navigate(
                R.id.action_favouriteFragment_to_BookDetailFragment,
                Bundle().apply {
                    Log.d("pdfLink", "pdfLink: ${selectedBook.pdfLink}")
                    putString("coverImage", selectedBook.coverImage)
                    putString("bookName", selectedBook.name)
                    putString("authorName", selectedBook.authorName)
                    putString("language", selectedBook.language)
                    putString("description", selectedBook.description)
                    putString("pdfLink", selectedBook.pdfLink)
                })
        }
    }
}