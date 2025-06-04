package com.bhatia.booknest.ui.fragment

import android.content.res.Configuration
import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.lifecycle.ViewModelProvider
import com.bhatia.booknest.databinding.FragmentPDFBinding
import com.bhatia.booknest.viewmodel.AppViewModel
import java.io.File

class PDFFragment : Fragment() {

    private lateinit var binding: FragmentPDFBinding
    private lateinit var chapterName: String
    private lateinit var chapterLink: String
    private lateinit var viewModel: AppViewModel
    private var currentPage = 0

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        binding = FragmentPDFBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        val isDarkMode = resources.configuration.uiMode and Configuration.UI_MODE_NIGHT_MASK == Configuration.UI_MODE_NIGHT_YES
        viewModel = ViewModelProvider(requireActivity())[AppViewModel::class.java]

        chapterName = arguments?.getString("chapterName") ?: ""
        chapterLink = arguments?.getString("chapterLink") ?: ""
        binding.chapterNameTextView.text = chapterName


        binding.loading.visibility = View.VISIBLE
        binding.pdfView.visibility = View.GONE


        val lastReadPage = viewModel.getLastReadPage()


        viewModel.downloadPdf(chapterName,chapterLink) { pdfBytes ->
            if (pdfBytes != null) {
                val pdfFile = File(requireContext().cacheDir, "downloaded_pdf.pdf")
                pdfFile.writeBytes(pdfBytes)

                // Load the PDF from the downloaded file
                binding.pdfView.fromFile(pdfFile)
                    .defaultPage(lastReadPage)
                    .swipeHorizontal(false)
                    .onPageChange { page, _ ->
                        currentPage = page
                                            }
                    .nightMode(isDarkMode)
                    .load()

                // Hide loading indicator and show PDF view
                binding.loading.visibility = View.GONE
                binding.pdfView.visibility = View.VISIBLE


            } else {
                Log.e("PDFFragment", "Failed to download PDF")
                // Hide loading indicator
                binding.loading.visibility = View.GONE
            }
        }
    }
    override fun onPause() {
        super.onPause()
        viewModel.saveLastReadPage(currentPage)
    }
}

