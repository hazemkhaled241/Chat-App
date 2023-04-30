package com.hazem.chat.presentation.login.adapter

import android.annotation.SuppressLint
import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.hazem.chat.data.local.Country
import com.hazem.chat.databinding.CountryItemBinding

class CountriesAdapter : RecyclerView.Adapter<CountriesAdapter.MyViewHolder>() {
    private var countries: ArrayList<Country> = arrayListOf()
    var onItemClicked: OnItemClick<Country>? =null

    inner class MyViewHolder(private val binding: CountryItemBinding) :
        RecyclerView.ViewHolder(binding.root) {
        fun bind(country: Country, position: Int) {
            binding.country = country
            binding.layout.setOnClickListener {
                onItemClicked?.onItemClicked(country,position)
            }
        }
    }

    override fun onCreateViewHolder(
        parent: ViewGroup,
        viewType: Int
    ): MyViewHolder {
        return MyViewHolder(
            CountryItemBinding.inflate(
                LayoutInflater.from(parent.context), parent, false
            )
        )
    }

    override fun onBindViewHolder(holder: MyViewHolder, position: Int) {
        holder.bind(countries[position], position)
    }

    override fun getItemCount(): Int {
        return countries.size
    }

    @SuppressLint("NotifyDataSetChanged")
    fun updateList(countries: ArrayList<Country>) {
        this.countries = countries
        notifyDataSetChanged()
    }
}