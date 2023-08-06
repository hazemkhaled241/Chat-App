package com.hazem.chat.presentation.login

import android.app.Dialog
import android.content.Context
import android.net.Uri
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
import com.hazem.chat.R
import com.hazem.chat.data.local.Country
import com.hazem.chat.databinding.FragmentLoginBinding
import com.hazem.chat.presentation.login.viewmodel.CountriesViewModel
import com.hazem.chat.presentation.login.viewmodel.LoginState
import com.hazem.chat.presentation.login.viewmodel.LoginViewModel
import com.hazem.chat.presentation.login.viewmodel.SharedViewModel
import com.hazem.chat.utils.createAlertDialog
import com.google.android.material.snackbar.Snackbar
import com.hazem.chat.domain.model.User
import com.hazem.chat.presentation.login.viewmodel.CurrCountryNoCodeState
import dagger.hilt.android.AndroidEntryPoint
import kotlinx.coroutines.launch

@AndroidEntryPoint
class LoginFragment : Fragment() {
    private var _binding: FragmentLoginBinding? = null
    private val binding get() = _binding!!
    private val args: LoginFragmentArgs by navArgs()
    private var allCountries: ArrayList<Country> = arrayListOf()
    private lateinit var currCountry: Country
    private lateinit var code: String
    private val countriesViewModel: CountriesViewModel by viewModels()
    private val loginViewModel: LoginViewModel by viewModels()
    private lateinit var dialog: Dialog
    private lateinit var model: SharedViewModel

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
       // insertCountries()
        binding.btnSendCode.setOnClickListener {
            verifyNumber(binding.edtMobile.text.toString())

        }
        observeOnCurrCountryCode()

    }

    private fun verifyNumber(phoneNumber: String) {
        loginViewModel.isValidNumber(currCountry, phoneNumber, requireActivity(),)
    }

    private fun initializeSharedViewModel() {
        model = ViewModelProvider(requireActivity())[SharedViewModel::class.java]
        binding.sharedViewModel = model
    }

    private fun observe() {
        lifecycleScope.launch {
            loginViewModel.loginState.collect {
                when (it) {
                    LoginState.Init -> Unit
                    is LoginState.ShowError -> {
                        Snackbar.make(
                            requireActivity().findViewById(android.R.id.content),
                            it.message,
                            2000
                        ).show()
                        Log.d("hhh", it.message)
                    }
                    is LoginState.ValidNumber -> {
                       /* findNavController().addOnDestinationChangedListener{ _, destination, _ ->
                            val currentFragmentTag = destination.label?.toString()
                            Log.d("hhh1", "onCreate: $currentFragmentTag")
                        }*/
                       // if(findNavController().isValidDestination(R.id.verifyFragment)){

                        val action =
                            LoginFragmentDirections.actionLoginFragmentToVerifyFragment(it.message,
                                User("",binding.edtMobile.text.toString(),"", Uri.parse(""))
                            )
                        findNavController().navigate(action)
                        //}

                    }
                    is LoginState.IsLoading -> handleLoadingState(it.isLoading)
                }
            }
        }
    }

    private fun observeOnCurrCountryCode() {
        lifecycleScope.launch {
            loginViewModel.currCountryNoCode.collect {
                when (it) {
                    CurrCountryNoCodeState.Init -> Unit
                    is CurrCountryNoCodeState.ShowError -> {
                        Snackbar.make(
                            requireActivity().findViewById(android.R.id.content),
                            it.message,
                            2000
                        ).show()
                        currCountry=Country(0,"TL", "East Timor", "+670", "USD")
                    }
                    is CurrCountryNoCodeState.GetCurrCountryNoCodeSuccessfully -> {
                        currCountry=it.date
                        binding.txtCountryCode.text = it.date.noCode
                    }
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
                code = manager.networkCountryIso?.trim() ?: "EC"
               /* if (code.isEmpty())
                    code=""*/

                Log.d("hhhh", allCountries.size.toString())

                loginViewModel.getCurrCountryCode(code,allCountries)


            }
        } catch (e: Exception) {
            e.printStackTrace()
        }
    }



    private fun getAllCountries() {
        lifecycleScope.launch {
           countriesViewModel.countryState.collect{
                if (it != null) {
                    allCountries = it as ArrayList<Country>
                    Log.d("hhhh", "ok")
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