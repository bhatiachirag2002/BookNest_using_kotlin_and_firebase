package com.bhatia.booknest.ui.fragment

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.lifecycle.ViewModelProvider
import androidx.navigation.fragment.findNavController
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.bhatia.booknest.R
import com.bhatia.booknest.databinding.FragmentHomeBinding
import com.bhatia.booknest.model.Books
import com.bhatia.booknest.ui.adapter.HomeAdapter
import com.bhatia.booknest.viewmodel.AppViewModel

class HomeFragment : Fragment() {
    private lateinit var binding: FragmentHomeBinding
    private lateinit var viewModel: AppViewModel

    companion object {
        private const val SELF_HELP = "Self-Help"
        private const val PERSONAL_FINANCE = "Personal Finance"
        private const val PSYCHOLOGY = "Psychology"
        private const val BUSINESS_ENTREPRENEURSHIP = "Business & Entrepreneurship"
        private const val HEALTH_FITNESS = "Health & Fitness"
    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        binding = FragmentHomeBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        viewModel = ViewModelProvider(requireActivity())[AppViewModel::class.java]
        viewModel.getBooks()
        viewModel.books.observe(viewLifecycleOwner) { booksData ->
            val visibleBooks = booksData.filter { it.show }
            binding.emptyText.visibility = if (visibleBooks.isEmpty()) View.VISIBLE else View.GONE
            setupCategorySections(visibleBooks)
        }
        setupCategoryButtons()
    }

    private fun setupCategoryButtons() {
        with(binding) {
            selfHelpBtn.setOnClickListener { navigateToCategory(SELF_HELP) }
            personalFinanceBtn.setOnClickListener { navigateToCategory(PERSONAL_FINANCE) }
            psychologyBtn.setOnClickListener { navigateToCategory(PSYCHOLOGY) }
            businessEnterBtn.setOnClickListener { navigateToCategory(BUSINESS_ENTREPRENEURSHIP) }
            healthAndFitnessBtn.setOnClickListener { navigateToCategory(HEALTH_FITNESS) }
        }
    }

    private fun navigateToCategory(categoryName: String) {
        findNavController().navigate(
            R.id.action_homeFragment_to_seeAllBookFragment,
            Bundle().apply {
                putString("categoryName", categoryName)
            })
    }

    private fun setupCategorySections(books: List<Books>) {
        setupCategorySection(
            SELF_HELP,
            books,
            binding.selfHelpBtn,
            binding.selfHelpLayout,
            binding.selfHelpRecyclerView
        )
        setupCategorySection(
            HEALTH_FITNESS,
            books,
            binding.healthAndFitnessBtn,
            binding.healthAndFitnessLayout,
            binding.healthAndFitnessRecyclerView
        )
        setupCategorySection(
            PSYCHOLOGY,
            books,
            binding.psychologyBtn,
            binding.psychologyLayout,
            binding.psychologyRecyclerView
        )
        setupCategorySection(
            BUSINESS_ENTREPRENEURSHIP,
            books,
            binding.businessEnterBtn,
            binding.businessEnterLayout,
            binding.businessEnterRecyclerView
        )
        setupCategorySection(
            PERSONAL_FINANCE,
            books,
            binding.personalFinanceBtn,
            binding.personalFinanceLayout,
            binding.personalFinanceRecyclerView
        )
    }

    private fun setupCategorySection(
        category: String,
        books: List<Books>,
        button: View,
        layout: View,
        recyclerView: RecyclerView
    ) {
        val categoryBooks = books.filter { it.category == category }
        button.visibility = if (categoryBooks.size > 8) View.VISIBLE else View.GONE
        layout.visibility = if (categoryBooks.isNotEmpty()) View.VISIBLE else View.GONE
        setupRecyclerView(categoryBooks, recyclerView)
    }

    private fun setupRecyclerView(books: List<Books>, recyclerView: RecyclerView) {
        val limitedBooks = books.take(8)
        recyclerView.layoutManager =
            LinearLayoutManager(requireContext(), LinearLayoutManager.HORIZONTAL, false)
        recyclerView.adapter = HomeAdapter(limitedBooks) { selectedBook ->
            findNavController().navigate(
                R.id.action_homeFragment_to_BookDetailFragment,
                Bundle().apply {
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
