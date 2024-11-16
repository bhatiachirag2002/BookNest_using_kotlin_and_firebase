package com.bhatia.booknest.ui.fragment.auth_fragment

import android.os.Bundle
import android.os.Handler
import android.os.Looper
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.lifecycle.ViewModelProvider
import androidx.navigation.fragment.findNavController
import com.bhatia.booknest.R
import com.bhatia.booknest.databinding.FragmentSplashBinding
import com.bhatia.booknest.viewmodel.AppViewModel
import com.google.firebase.auth.FirebaseAuth

class SplashFragment : Fragment() {

    private lateinit var binding: FragmentSplashBinding
    private lateinit var viewModel: AppViewModel


    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        binding = FragmentSplashBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        Handler(Looper.getMainLooper()).postDelayed({
            checkAuthenticationState()
        }, 2000)
    }

    private fun checkAuthenticationState() {
        val currentUser = FirebaseAuth.getInstance().currentUser
        if (currentUser != null) {
            viewModel = ViewModelProvider(requireActivity())[AppViewModel::class.java]
            viewModel.getBooks()  // Ensure this method is called to fetch the books
            viewModel.books.observe(viewLifecycleOwner) { booksData ->
                if (booksData != null) {
                    findNavController().navigate(R.id.action_splashFragment_to_homeFragment)
                }
            }
        } else {
            findNavController().navigate(R.id.action_splashFragment_to_loginFragment)
        }
    }

}