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
import com.bhatia.booknest.databinding.FragmentSeeAllBookBinding
import com.bhatia.booknest.model.Books
import com.bhatia.booknest.ui.adapter.FavouriteAdapter
import com.bhatia.booknest.viewmodel.AppViewModel


class SeeAllBookFragment : Fragment() {
    private lateinit var binding: FragmentSeeAllBookBinding
    private lateinit var categoryName: String
    private lateinit var viewModel: AppViewModel
    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        binding = FragmentSeeAllBookBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        categoryName = arguments?.getString("categoryName") ?: ""
        viewModel = ViewModelProvider(requireActivity())[AppViewModel::class.java]
        viewModel.getBooks()
        viewModel.books.observe(viewLifecycleOwner) { booksData ->
            val booksVisible = booksData.filter { it.show }
            booksFilter(booksVisible)
        }
        binding.categoryTextView.text = categoryName
    }

    private fun booksFilter(books: List<Books>) {
        val bookList = books.filter { it.category == categoryName }
        setupRecyclerView(bookList, binding.categoryRecyclerView)
    }

    private fun setupRecyclerView(booksData: List<Books>, recyclerView: RecyclerView) {
        recyclerView.layoutManager =
            LinearLayoutManager(requireContext())
        recyclerView.adapter = FavouriteAdapter(booksData) { selectedBook ->
            findNavController().navigate(
                R.id.action_seeAllBookFragment_to_bookDetailsFragment,
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