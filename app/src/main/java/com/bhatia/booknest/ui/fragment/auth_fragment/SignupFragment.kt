package com.bhatia.booknest.ui.fragment.auth_fragment

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.fragment.app.Fragment
import androidx.lifecycle.ViewModelProvider
import androidx.navigation.fragment.findNavController
import com.bhatia.booknest.R
import com.bhatia.booknest.databinding.FragmentSignupBinding
import com.bhatia.booknest.util.Validator
import com.bhatia.booknest.viewmodel.AppViewModel


class SignupFragment : Fragment() {

    private lateinit var binding: FragmentSignupBinding
    private lateinit var viewModel: AppViewModel

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        // Inflate the layout for this fragment
        binding = FragmentSignupBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        binding.loading.visibility = View.GONE
        binding.signupBtn.visibility = View.VISIBLE
        viewModel = ViewModelProvider(requireActivity())[AppViewModel::class.java]
        viewModel.authResult.observe(viewLifecycleOwner) { result ->
            result.onSuccess {
                it?.let {
                    binding.loading.visibility = View.GONE
                    binding.signupBtn.visibility = View.VISIBLE
                    viewModel.getBooks()  // Ensure this method is called to fetch the books
                    viewModel.books.observe(viewLifecycleOwner) { booksData ->
                        if (booksData != null) {
                            findNavController().navigate(R.id.action_signupFragment_to_homeFragment)
                            Toast.makeText(
                                requireContext(),
                                "Signup Successfully",
                                Toast.LENGTH_LONG
                            )
                                .show()
                        }
                    }
                }
            }.onFailure { e ->
                binding.loading.visibility = View.GONE
                binding.signupBtn.visibility = View.VISIBLE
                Toast.makeText(requireContext(), "Signup Failed: ${e.message}", Toast.LENGTH_LONG)
                    .show()
            }
        }
        alreadyAccountBtn()
        binding.signupBtn.setOnClickListener {
            binding.loading.visibility = View.VISIBLE
            binding.signupBtn.visibility = View.GONE
            signUpBtn()
        }
    }

    //Buttons
    private fun signUpBtn() {
        binding.emailInputLayout.error = Validator.emailValid(binding.emailEditText.text.toString().trim())
        passwordValid()
        if (binding.emailInputLayout.error == null &&
            binding.passwordInputLayout.error == null &&
            binding.confirmPasswordInputLayout.error == null
        ) {
            viewModel.signUp(
                binding.emailEditText.text.toString().trim(),
                binding.confirmPasswordEditText.toString().trim()
            )
        }else{
            binding.loading.visibility = View.GONE
            binding.signupBtn.visibility = View.VISIBLE
        }
    }

    private fun alreadyAccountBtn() {
        binding.alreadyAccountBtn.setBackgroundResource(0)
        binding.alreadyAccountBtn.setOnClickListener {
            findNavController().navigate(R.id.action_signupFragment_to_loginFragment)
        }
    }



    private fun passwordValid() {
        val password = binding.passwordEditText.text.toString().trim()
        val confirmPassword = binding.confirmPasswordEditText.text.toString().trim()
        binding.passwordInputLayout.error = Validator.passwordValid(password)
        binding.confirmPasswordInputLayout.error = Validator.confirmPassword(password, confirmPassword)
    }


}