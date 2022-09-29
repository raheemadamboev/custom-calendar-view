package xyz.teamgravity.customcalendarview.presentation.activity

import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import dagger.hilt.android.AndroidEntryPoint
import timber.log.Timber
import xyz.teamgravity.customcalendarview.databinding.ActivityMainBinding
import xyz.teamgravity.customcalendarview.presentation.fragment.Calendar

@AndroidEntryPoint
class Main : AppCompatActivity() {

    private lateinit var binding: ActivityMainBinding

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityMainBinding.inflate(layoutInflater)
        setContentView(binding.root)

        if (savedInstanceState == null) loadFragment()
    }

    private fun loadFragment() {
        supportFragmentManager
            .beginTransaction()
            .replace(binding.fragmentContainer.id, Calendar.instance())
            .commit()
    }
}