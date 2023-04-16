package com.example.chat.presentation.login

import android.content.Context
import android.os.Bundle
import android.telephony.TelephonyManager
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.viewModels
import androidx.lifecycle.lifecycleScope
import androidx.navigation.fragment.findNavController
import androidx.navigation.fragment.navArgs
import com.example.chat.R
import com.example.chat.data.local.Country
import com.example.chat.databinding.FragmentLoginBinding
import com.example.chat.presentation.CountriesViewModel
import dagger.hilt.android.AndroidEntryPoint
import kotlinx.coroutines.launch

@AndroidEntryPoint
class LoginFragment : Fragment() {
    private var _binding: FragmentLoginBinding? = null
    private val binding get() = _binding!!
    private val args: LoginFragmentArgs by navArgs()
    private var allCountries:ArrayList<Country> = arrayListOf()
    private lateinit var code:String
    private val countriesViewModel:CountriesViewModel by viewModels()

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        // Inflate the layout for this fragment
        _binding = FragmentLoginBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        countriesViewModel.fetchAllCountries()
        start()
        binding.txtCountryCode.setOnClickListener {
            findNavController().navigate(R.id.action_loginFragment_to_allCountriesFragment)
        }

    }
    private fun start(){
        if (findNavController().previousBackStackEntry?.destination?.id == R.id.allCountriesFragment&&args.countryItem.code.isNotEmpty())
            setArgs()
        else
            getAllCountries()
    }
    private fun setArgs(){
            binding.txtCountryCode.text=args.countryItem.noCode
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
       }
       catch (e:Exception){
           e.printStackTrace()
       }
    }

    private fun setCodeNumber() {
        val codeNumber=allCountries.firstOrNull {
            it.code.equals(code, true)
        }
        if(codeNumber !=null)
            binding.txtCountryCode.text=codeNumber.noCode
    }

    private fun getAllCountries() {
        viewLifecycleOwner.lifecycleScope.launch {
            countriesViewModel.countryState.collect {
                if(it!=null){
                 allCountries= it as ArrayList<Country>
                    setDefaultCountryCode()
                }

            }

        }
    }

}