package com.example.liberary.View.Activities

import android.content.Context
import android.content.Intent
import android.content.res.Configuration
import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import androidx.appcompat.app.AppCompatDelegate
import androidx.lifecycle.ViewModelProvider
import androidx.lifecycle.lifecycleScope
import com.example.liberary.App.MyContextWrapper
import com.example.liberary.R
import com.example.liberary.ViewModel.ViewModel
import com.example.liberary.constants.Constants
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.delay
import kotlinx.coroutines.flow.single
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext
import java.util.*


class MainActivity : AppCompatActivity() {
    private lateinit var myIntent : Intent
    private lateinit var viewModel: ViewModel
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)
        supportActionBar?.hide()
        viewModel = ViewModelProvider(this).get(ViewModel::class.java)
        viewModel.initContext(this)
        myIntent = Intent(this, WelcomeActivity::class.java)
        lifecycleScope.launch{
            val prefs = viewModel.getPreferences().single()
            if (prefs.isDarkMode){
                AppCompatDelegate.setDefaultNightMode(AppCompatDelegate.MODE_NIGHT_YES)

            }else if(!prefs.isDarkMode){
                AppCompatDelegate.setDefaultNightMode(AppCompatDelegate.MODE_NIGHT_NO)
            }

            delay(5000)
            withContext(Dispatchers.Main){
                startActivity(myIntent)
                finish()
            }
        }
    }

    override fun onConfigurationChanged(newConfig: Configuration) {
        super.onConfigurationChanged(newConfig)
    }



    override fun attachBaseContext(newBase: Context?) {
        val sharedPreferences = newBase?.getSharedPreferences(Constants.sharedName,MODE_PRIVATE)
        val language = sharedPreferences?.getString(Constants.language, "en")
        super.attachBaseContext(newBase?.let { MyContextWrapper.wrap(it, language!!) })
        val locale = Locale(language!!)
        val resources = baseContext.resources
        val conf = resources.configuration
        conf.locale = locale
        resources.updateConfiguration(conf, resources.displayMetrics)
    }


}