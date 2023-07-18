package com.hazem.chat

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.util.Log
import android.view.View
import androidx.navigation.NavController
import androidx.navigation.fragment.NavHostFragment
import androidx.navigation.fragment.findNavController
import androidx.navigation.ui.AppBarConfiguration
import androidx.navigation.ui.setupWithNavController
import com.hazem.chat.databinding.ActivityMainBinding

import dagger.hilt.android.AndroidEntryPoint

@AndroidEntryPoint
class MainActivity : AppCompatActivity() {
    private var _binding: ActivityMainBinding? = null
    private val binding get() = _binding!!
    private lateinit var navController: NavController
    lateinit var appBarConfiguration: AppBarConfiguration
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        _binding = ActivityMainBinding.inflate(layoutInflater)
        setContentView(binding.root)
        val navHostFragment =
            supportFragmentManager.findFragmentById(R.id.navHostFragment) as NavHostFragment
        navController = navHostFragment.navController
        navController.addOnDestinationChangedListener { _, destination, _ ->
            //val currentFragmentTag = destination.label?.toString()
            //Log.d("hhh", "onCreate: $currentFragmentTag")
            when (destination.id) {
                R.id.splashFragment -> {
                    binding.bottomNavigationView.visibility = View.GONE
                }
                R.id.allCountriesFragment -> {
                    binding.bottomNavigationView.visibility = View.GONE
                }
                R.id.verifyFragment -> {
                    binding.bottomNavigationView.visibility = View.GONE
                }
                R.id.loginFragment -> {
                    binding.bottomNavigationView.visibility = View.GONE
                }
                else -> {
                    binding.bottomNavigationView.visibility = View.VISIBLE
                }
            }
        }
        appBarConfiguration = AppBarConfiguration.Builder(
            setOf(
                R.id.chatsHomeFragment,
                R.id.profileFragment,
            )
        ).build()
        setupNavigationComponent()

    }
    private fun setupNavigationComponent() {
        val navHostFragment = supportFragmentManager.findFragmentById(R.id.navHostFragment) as NavHostFragment
        navController = navHostFragment.findNavController()
        binding.bottomNavigationView.setupWithNavController(navController)
    }

}