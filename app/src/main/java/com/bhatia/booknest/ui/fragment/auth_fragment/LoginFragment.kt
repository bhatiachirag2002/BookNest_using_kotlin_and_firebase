package com.bhatia.booknest.ui.fragment.auth_fragment

import android.os.Bundle
import android.util.Log
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.lifecycle.ViewModelProvider
import androidx.navigation.fragment.findNavController
import com.bhatia.booknest.R
import com.bhatia.booknest.databinding.FragmentLoginBinding
import com.bhatia.booknest.util.Validator
import com.bhatia.booknest.viewmodel.AppViewModel

class LoginFragment : Fragment() {

    private lateinit var binding: FragmentLoginBinding
    private lateinit var viewModel: AppViewModel

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        binding = FragmentLoginBinding.inflate(inflater, container, false)
        return binding.root

    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        binding.loading.visibility = View.GONE
        binding.loginBtn.visibility = View.VISIBLE
        viewModel = ViewModelProvider(requireActivity())[AppViewModel::class.java]
        viewModel.authResult.observe(viewLifecycleOwner) { result ->
            result.onSuccess { user ->
                user?.let {
                    binding.loading.visibility = View.GONE
                    binding.loginBtn.visibility = View.VISIBLE
                    viewModel.getBooks()  // Ensure this method is called to fetch the books
                    viewModel.books.observe(viewLifecycleOwner) { booksData ->
                        if (booksData != null) {
                            findNavController().navigate(R.id.action_loginFragment_to_homeFragment)
                            Toast.makeText(
                                requireContext(),
                                "Login successfully",
                                Toast.LENGTH_LONG
                            )
                                .show()
                            Log.d("Login", "login successfully: ${user.uid}, ${user.email}")
                        }
                    }

                }
            }.onFailure { e ->
                binding.loading.visibility = View.GONE
                binding.loginBtn.visibility = View.VISIBLE
                Log.d("Login", "loginFailed: ${e.message}")
                Toast.makeText(requireContext(), "Login failed: ${e.message}", Toast.LENGTH_LONG)
                    .show()
            }
        }

        binding.NewAccountBtn.setOnClickListener {
            findNavController().navigate(R.id.action_loginFragment_to_signupFragment)
        }

        binding.loginBtn.setOnClickListener {
            binding.loading.visibility = View.VISIBLE
            binding.loginBtn.visibility = View.GONE
            loginBtn()
        }

        binding.ForgetPasswordBtn.setOnClickListener {
            findNavController().navigate(R.id.action_loginFragment_to_forgotPasswordFragment)
        }


    }

    private fun loginBtn() {
        binding.passwordInputLayout.error = Validator.passwordValid(binding.passwordEditText.text.toString().trim())
        binding.emailInputLayout.error = Validator.emailValid(binding.emailEditText.text.toString().trim())

        if (binding.emailInputLayout.error == null &&
            binding.passwordInputLayout.error == null
        ) {
            viewModel.login(
                binding.emailEditText.text.toString().trim(),
                binding.passwordEditText.text.toString()
            )
        }else{
            binding.loading.visibility = View.GONE
            binding.loginBtn.visibility = View.VISIBLE
        }
    }


}