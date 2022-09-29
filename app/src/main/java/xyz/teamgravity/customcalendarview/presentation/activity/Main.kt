package xyz.teamgravity.customcalendarview.presentation.activity

import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import xyz.teamgravity.customcalendarview.databinding.ActivityMainBinding
import xyz.teamgravity.customcalendarview.presentation.fragment.Calendar

class Main : AppCompatActivity() {

    private lateinit var binding: ActivityMainBinding

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityMainBinding.inflate(layoutInflater)
        setContentView(binding.root)

        loadFragment()
    }

    private fun loadFragment() {
        supportFragmentManager
            .beginTransaction()
            .replace(binding.fragmentContainer.id, Calendar.instance())
            .commit()
    }
}