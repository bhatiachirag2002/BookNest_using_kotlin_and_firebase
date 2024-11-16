package com.bhatia.booknest.ui.fragment

import android.content.res.Configuration
import android.os.Bundle
import android.util.Log
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.core.content.ContextCompat
import androidx.lifecycle.ViewModelProvider
import androidx.navigation.fragment.findNavController
import com.bhatia.booknest.R
import com.bhatia.booknest.databinding.FragmentBookDetailsBinding
import com.bhatia.booknest.util.NetworkUtil
import com.bhatia.booknest.viewmodel.AppViewModel
import com.bumptech.glide.Glide


class BookDetailsFragment : Fragment() {
    private lateinit var binding: FragmentBookDetailsBinding
    private lateinit var viewModel: AppViewModel
    private lateinit var coverImage: String
    private lateinit var bookName: String
    private lateinit var authorName: String
    private lateinit var language: String
    private lateinit var description: String
    private lateinit var pdfLink: String
    private var isFavourite = false

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        binding = FragmentBookDetailsBinding.inflate(inflater, container, false)
        coverImage = arguments?.getString("coverImage") ?: ""
        bookName = arguments?.getString("bookName") ?: ""
        authorName = arguments?.getString("authorName") ?: ""
        language = arguments?.getString("language") ?: ""
        description = arguments?.getString("description") ?: ""
        pdfLink = arguments?.getString("pdfLink") ?: ""
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        val isDarkMode =
            resources.configuration.uiMode and Configuration.UI_MODE_NIGHT_MASK == Configuration.UI_MODE_NIGHT_YES
        viewModel = ViewModelProvider(requireActivity())[AppViewModel::class.java]
        viewModel.getFavouriteBooks()

        setupFavoriteButton(isDarkMode)
        openBtn()





        Glide.with(requireContext()).load(coverImage).into(binding.coverImageView)
        binding.bookNameTextView.text = bookName
        binding.authorNameTextView.text = authorName
        binding.languageTxtView.text = language
        binding.descriptionTxtView.text = description

        binding.favouriteBtn.setOnClickListener {
            isFavourite = !isFavourite
            if (isFavourite) {
                binding.favouriteBtn.backgroundTintList =
                    ContextCompat.getColorStateList(requireContext(), R.color.error_color)
                viewModel.addBookToFavourite(bookName)
                Toast.makeText(requireContext(), "Add in favourite", Toast.LENGTH_SHORT).show()
            } else {
                val colorRes = if (isDarkMode) R.color.white else R.color.black
                binding.favouriteBtn.backgroundTintList =
                    ContextCompat.getColorStateList(requireContext(), colorRes)
                viewModel.deleteBookFromFavourite(bookName)
                Toast.makeText(requireContext(), "remove from favourite", Toast.LENGTH_SHORT).show()
            }

        }

    }

    private fun openBtn() {

        binding.OpenBtn.setOnClickListener {
            Log.d("pdfLink", "buyAndOpenBtn: $pdfLink")
            if (NetworkUtil.isConnected(requireContext())) {
                findNavController().navigate(
                    R.id.action_bookDetailsFragment_to_PDFFragment,
                    Bundle().apply {
                        putString("chapterName", bookName)
                        putString("chapterLink", pdfLink)
                    })

            } else {
                Toast.makeText(
                    requireContext(),
                    "No internet connection. Please check your connection.",
                    Toast.LENGTH_SHORT
                ).show()
            }
        }
    }

    private fun setupFavoriteButton(isDarkMode: Boolean) {
        viewModel.favouriteBooks.observe(viewLifecycleOwner) { favouriteBooks ->
            isFavourite = favouriteBooks.contains(bookName)
            updateFavoriteButtonColor(isDarkMode)

            binding.favouriteBtn.setOnClickListener {
                isFavourite = !isFavourite
                if (isFavourite) {
                    viewModel.addBookToFavourite(bookName)
                    Toast.makeText(requireContext(), "Added to favourites", Toast.LENGTH_SHORT)
                        .show()
                } else {
                    viewModel.deleteBookFromFavourite(bookName)
                    Toast.makeText(requireContext(), "Removed from favourites", Toast.LENGTH_SHORT)
                        .show()
                }
                updateFavoriteButtonColor(isDarkMode)
            }
        }
    }

    private fun updateFavoriteButtonColor(isDarkMode: Boolean) {
        val colorRes =
            if (isFavourite) R.color.error_color else if (isDarkMode) R.color.white else R.color.black
        binding.favouriteBtn.backgroundTintList =
            ContextCompat.getColorStateList(requireContext(), colorRes)
    }
}