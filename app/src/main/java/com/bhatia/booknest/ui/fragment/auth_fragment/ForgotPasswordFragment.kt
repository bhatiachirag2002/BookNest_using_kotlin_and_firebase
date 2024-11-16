package com.bhatia.booknest.ui.fragment.auth_fragment

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.fragment.app.Fragment
import androidx.lifecycle.ViewModelProvider
import androidx.navigation.fragment.findNavController
import com.bhatia.booknest.databinding.FragmentForgotPasswordBinding
import com.bhatia.booknest.util.Validator
import com.bhatia.booknest.viewmodel.AppViewModel

class ForgotPasswordFragment : Fragment() {
    private lateinit var binding: FragmentForgotPasswordBinding
    private lateinit var viewModel: AppViewModel

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        binding = FragmentForgotPasswordBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        binding.loading.visibility = View.GONE
        binding.forgotBtn.visibility = View.VISIBLE
        viewModel = ViewModelProvider(requireActivity())[AppViewModel::class.java]
        viewModel.resetPasswordResult.observe(viewLifecycleOwner) { result ->
            result.onSuccess {
                binding.loading.visibility = View.VISIBLE
                binding.forgotBtn.visibility = View.GONE
                Toast.makeText(
                    requireContext(),
                    "A reset password link has been sent to your email. Please check your inbox.",
                    Toast.LENGTH_LONG
                ).show()

                findNavController().popBackStack()
            }.onFailure { e ->
                binding.loading.visibility = View.GONE
                binding.forgotBtn.visibility = View.VISIBLE
                Toast.makeText(
                    requireContext(),
                    "Forgot password Failed: ${e.message}",
                    Toast.LENGTH_LONG
                )
                    .show()
            }

        }

        binding.forgotBtn.setOnClickListener {
            binding.loading.visibility = View.VISIBLE
            binding.forgotBtn.visibility = View.GONE
            binding.emailInputLayout.error = Validator.emailValid(binding.emailEditText.text.toString().trim())
            if (binding.emailInputLayout.error == null) {
                viewModel.forgetPassword(binding.emailEditText.text.toString().trim())

            }else{
                binding.loading.visibility = View.GONE
                binding.forgotBtn.visibility = View.VISIBLE
            }


        }
    }


}
