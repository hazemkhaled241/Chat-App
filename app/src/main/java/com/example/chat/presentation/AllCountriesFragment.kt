package com.example.chat.presentation

import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.viewModels
import androidx.lifecycle.lifecycleScope
import com.example.chat.data.local.Country
import com.example.chat.databinding.FragmentAllCountriesBinding
import com.example.chat.presentation.adapter.CountriesAdapter
import dagger.hilt.android.AndroidEntryPoint
import kotlinx.coroutines.launch
@AndroidEntryPoint
class AllCountriesFragment : Fragment() {
    private var _binding: FragmentAllCountriesBinding? = null
    private val binding get() = _binding!!
    private val countriesAdapter:CountriesAdapter by lazy { CountriesAdapter() }
    private val countriesViewModel: CountriesViewModel by viewModels()

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        // Inflate the layout for this fragment
        _binding = FragmentAllCountriesBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        binding.rvCountries.adapter=countriesAdapter
        countriesViewModel.fetchAllCountries()
        observe()

    }

    private fun observe() {
        viewLifecycleOwner.lifecycleScope.launch {
            countriesViewModel.countryState.collect {
                 if (it != null) {
                     countriesAdapter.updateList(it as ArrayList<Country>)

                 }

             }

            }
        }

}