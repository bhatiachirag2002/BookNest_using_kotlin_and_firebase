package com.bhatia.booknest.ui.fragment.auth_fragment

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.fragment.app.Fragment
import androidx.lifecycle.ViewModelProvider
import com.bhatia.booknest.databinding.FragmentChangePasswordBinding
import com.bhatia.booknest.util.Validator
import com.bhatia.booknest.viewmodel.AppViewModel

class ChangePasswordFragment : Fragment() {
    private lateinit var binding: FragmentChangePasswordBinding
    private lateinit var viewModel: AppViewModel
    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        binding = FragmentChangePasswordBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        binding.loading.visibility = View.GONE
        binding.changePasswordBtn.visibility = View.VISIBLE
        viewModel = ViewModelProvider(requireActivity())[AppViewModel::class.java]
        viewModel.resetPasswordResult.observe(viewLifecycleOwner) { result ->
            result.onSuccess {
                Toast.makeText(requireContext(), "Change password successfully", Toast.LENGTH_LONG)
                    .show()
                binding.loading.visibility = View.GONE
                binding.changePasswordBtn.visibility = View.VISIBLE
            }.onFailure { e ->
                binding.loading.visibility = View.GONE
                binding.changePasswordBtn.visibility = View.VISIBLE
                Toast.makeText(requireContext(), "Login failed: ${e.message}", Toast.LENGTH_LONG)
                    .show()
            }

        }
        binding.changePasswordBtn.setOnClickListener {
            binding.loading.visibility = View.GONE
            binding.changePasswordBtn.visibility = View.VISIBLE
            changePassword()
        }
    }

    private fun changePassword() {
        passwordValid()
        if (binding.oldPasswordInputLayout.error == null &&
            binding.newPasswordInputLayout.error == null &&
            binding.confirmPasswordInputLayout.error == null
        ) {
            viewModel.changePassword(
                binding.oldPasswordEditText.toString().trim(),
                binding.confirmPasswordEditText.toString().trim()
            )
        }else {
            binding.loading.visibility = View.GONE
            binding.changePasswordBtn.visibility = View.VISIBLE
        }
    }

    private fun passwordValid() {
        val oldPassword = binding.oldPasswordEditText.text.toString().trim()
        val newPassword = binding.newPasswordEditText.text.toString().trim()
        val confirmPassword = binding.confirmPasswordEditText.text.toString().trim()
        binding.oldPasswordInputLayout.error = Validator.passwordValid(oldPassword)
        binding.newPasswordInputLayout.error = Validator.passwordValid(newPassword)
        binding.confirmPasswordInputLayout.error = Validator.confirmPassword(newPassword, confirmPassword)
    }
}