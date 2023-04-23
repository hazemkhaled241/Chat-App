package com.example.chat.presentation.login

import android.app.Dialog
import android.content.Context
import android.os.Bundle
import android.telephony.TelephonyManager
import android.util.Log
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.viewModels
import androidx.lifecycle.ViewModelProvider
import androidx.lifecycle.lifecycleScope
import androidx.navigation.fragment.findNavController
import androidx.navigation.fragment.navArgs
import com.example.chat.R
import com.example.chat.data.local.Country
import com.example.chat.databinding.FragmentLoginBinding
import com.example.chat.presentation.login.viewmodel.CountriesViewModel
import com.example.chat.presentation.login.viewmodel.LoginState
import com.example.chat.presentation.login.viewmodel.LoginViewModel
import com.example.chat.presentation.login.viewmodel.SharedViewModel
import com.example.chat.utils.createAlertDialog
import com.google.android.material.snackbar.Snackbar
import dagger.hilt.android.AndroidEntryPoint
import kotlinx.coroutines.launch

@AndroidEntryPoint
class LoginFragment : Fragment() {
    private var _binding: FragmentLoginBinding? = null
    private val binding get() = _binding!!
    private val args: LoginFragmentArgs by navArgs()
    private var allCountries: ArrayList<Country> = arrayListOf()
    private lateinit var code: String
    private var currCountry: Country = Country(0, "EG", "Egypt", "+20", "EGP")
    private val countriesViewModel: CountriesViewModel by viewModels()
    private val loginViewModel: LoginViewModel by viewModels()
    private lateinit var dialog: Dialog
    private lateinit var model:SharedViewModel
    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        // Inflate the layout for this fragment
        _binding = FragmentLoginBinding.inflate(inflater, container, false)
        initializeSharedViewModel()
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        countriesViewModel.fetchAllCountries()
        dialog = requireContext().createAlertDialog(requireActivity())
        start()
        observe()
        binding.txtCountryCode.setOnClickListener {
            model.message = binding.edtMobile.text.toString()
            findNavController().navigate(R.id.action_loginFragment_to_allCountriesFragment)
        }

        binding.btnSendCode.setOnClickListener {
            verifyNumber(binding.edtMobile.text.toString())

        }

    }

    private fun verifyNumber(phoneNumber: String) {
        loginViewModel.isValidNumber(currCountry.code, phoneNumber)
    }
  private fun initializeSharedViewModel(){
       model = ViewModelProvider(requireActivity())[SharedViewModel::class.java]
      binding.sharedViewModel=model
  }

    private fun observe() {
        lifecycleScope.launch {
            loginViewModel.loginState.collect {
                when (it) {
                    LoginState.Init -> Unit
                    is LoginState.ShowError -> {
                        Log.d("Error test", "isValidNumber:${it.message} ")
                        Snackbar.make(
                            requireActivity().findViewById(android.R.id.content),
                            it.message,
                            2000
                        ).show()
                    }
                    is LoginState.ValidNumber -> {
                        findNavController().navigate(R.id.action_loginFragment_to_verifyFragment)
                    }
                    is LoginState.IsLoading -> handleLoadingState(it.isLoading)
                }
            }
        }
    }


    private fun start() {
        if (findNavController().previousBackStackEntry?.destination?.id == R.id.allCountriesFragment && args.countryItem.code.isNotEmpty()) {
            setArgs()
        } else
            getAllCountries()
    }

    private fun setArgs() {
        binding.txtCountryCode.text = args.countryItem.noCode
        currCountry = args.countryItem
    }

    private fun setDefaultCountryCode() {
        try {


            val manager =
                requireActivity().getSystemService(Context.TELEPHONY_SERVICE) as (TelephonyManager)?
            manager?.let {
                code = manager.networkCountryIso?.trim() ?: ""
                if (code.isEmpty())
                    return
                setCodeNumber()


            }
        } catch (e: Exception) {
            e.printStackTrace()
        }
    }

    private fun setCodeNumber() {
        currCountry = allCountries.first {
            it.code.equals(code, true)
        }
        binding.txtCountryCode.text = currCountry.noCode
    }

    private fun getAllCountries() {
        viewLifecycleOwner.lifecycleScope.launch {
            countriesViewModel.countryState.collect {
                if (it != null) {
                    allCountries = it as ArrayList<Country>
                    setDefaultCountryCode()
                }

            }

        }
    }
    private fun handleLoadingState(isLoading: Boolean) {
        when (isLoading) {
            true -> {
                dialog.show()
            }
            false -> dialog.hide()
        }
    }


}