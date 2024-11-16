package com.bhatia.booknest

import android.annotation.SuppressLint
import android.content.pm.PackageManager
import android.os.Bundle
import android.view.Menu
import android.view.MenuInflater
import android.view.MenuItem
import android.view.View
import android.widget.Toast
import androidx.activity.enableEdgeToEdge
import androidx.activity.result.contract.ActivityResultContracts
import androidx.appcompat.app.AppCompatActivity
import androidx.core.content.ContextCompat
import androidx.lifecycle.ViewModelProvider
import androidx.navigation.fragment.NavHostFragment
import androidx.navigation.ui.setupWithNavController
import com.bhatia.booknest.auth.FirebaseAuthentication
import com.bhatia.booknest.databinding.ActivityMainBinding
import com.bhatia.booknest.db.FirebaseSource
import com.bhatia.booknest.repo.AppRepo
import com.bhatia.booknest.viewmodel.AppViewModel
import com.bhatia.booknest.viewmodel.AppViewModelFactory
import com.google.firebase.Firebase
import com.google.firebase.auth.auth
import android.Manifest
import android.os.Build
import androidx.core.view.WindowInsetsControllerCompat


class MainActivity : AppCompatActivity() {
    private lateinit var binding: ActivityMainBinding
    private lateinit var appViewModel: AppViewModel

    @SuppressLint("SourceLockedOrientationActivity")
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityMainBinding.inflate(layoutInflater)
        enableEdgeToEdge()
        setContentView(binding.root)
        statusBarColor()
        bottomNavController()
        setSupportActionBar(binding.toolbar)

        if (ContextCompat.checkSelfPermission(this, Manifest.permission.POST_NOTIFICATIONS)
            != PackageManager.PERMISSION_GRANTED
        ) {
            requestNotificationPermission()
        }
        setupViewModel()
    }

    private fun setupViewModel() {
        val appRepo = AppRepo(FirebaseSource(), FirebaseAuthentication(), this)
        val viewModelFactory = AppViewModelFactory(appRepo)
        appViewModel = ViewModelProvider(this, viewModelFactory)[AppViewModel::class.java]
    }

    override fun onCreateOptionsMenu(menu: Menu?): Boolean {
        val inflater: MenuInflater = menuInflater
        inflater.inflate(R.menu.top_menu, menu)
        return true
    }

    override fun onOptionsItemSelected(item: MenuItem): Boolean {
        val navHostFragment =
            supportFragmentManager.findFragmentById(R.id.fragmentContainerView) as NavHostFragment
        val navController = navHostFragment.navController
        return when (item.itemId) {
            R.id.action_Change_Password -> {
                navController.navigate(R.id.action_homeFragment_to_changePasswordFragment)
                true
            }

            R.id.action_Logout -> {
                Firebase.auth.signOut()
                navController.navigate(R.id.action_homeFragment_to_loginFragment)
                Toast.makeText(this, "Logout successfully", Toast.LENGTH_SHORT).show()
                true
            }

            else -> super.onOptionsItemSelected(item)
        }
    }


    private fun bottomNavController() {
        val navHostFragment =
            supportFragmentManager.findFragmentById(R.id.fragmentContainerView) as NavHostFragment
        val navController = navHostFragment.navController

        // Set up BottomNavigationView with NavController
        binding.bottomNavigationView.setupWithNavController(navController)

        // Hide BottomNavigationView in ChaptersFragment
        navController.addOnDestinationChangedListener { _, destination, _ ->
            if (destination.id == R.id.homeFragment || destination.id == R.id.favouriteFragment) {
                binding.bottomNavigationView.visibility = View.VISIBLE
                binding.toolbar.visibility = View.VISIBLE
            } else {
                binding.bottomNavigationView.visibility = View.GONE
                binding.toolbar.visibility = View.GONE
            }
        }
        binding.bottomNavigationView.setOnItemSelectedListener {
            when (it.itemId) {
                R.id.home -> {
                    navController.navigate(R.id.homeFragment)
                    navController.popBackStack(R.id.favouriteFragment, true)
                }

                R.id.favourite -> {
                    navController.navigate(R.id.favouriteFragment)

                }
            }
            true
        }
    }

    private fun statusBarColor() {
        val navHostFragment =
            supportFragmentManager.findFragmentById(R.id.fragmentContainerView) as NavHostFragment
        val navController = navHostFragment.navController

        navController.addOnDestinationChangedListener { _, destination, _ ->
            if (destination.id == R.id.splashFragment) {
                WindowInsetsControllerCompat(window, window.decorView).isAppearanceLightStatusBars =
                    false
            } else {
                WindowInsetsControllerCompat(window, window.decorView).isAppearanceLightStatusBars =
                    true
            }
        }

    }


    private fun requestNotificationPermission() {
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.TIRAMISU) {
            requestPermissionLauncher.launch(Manifest.permission.POST_NOTIFICATIONS)
        }
    }


    private val requestPermissionLauncher =
        registerForActivityResult(ActivityResultContracts.RequestPermission()) { isGranted: Boolean ->
            if (isGranted) {
                Toast.makeText(this, "Permission Granted", Toast.LENGTH_SHORT).show()
            } else {
                Toast.makeText(this, "Permission Denied", Toast.LENGTH_SHORT).show()
            }

        }

}

