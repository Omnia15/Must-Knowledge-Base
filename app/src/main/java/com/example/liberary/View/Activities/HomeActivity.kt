package com.example.liberary.View.Activities


import android.content.Context
import android.content.Intent
import android.os.Bundle
import android.util.Log
import android.view.Menu
import android.view.MenuItem
import android.view.View
import androidx.appcompat.app.ActionBarDrawerToggle
import androidx.appcompat.app.AppCompatActivity
import androidx.appcompat.app.AppCompatDelegate
import androidx.appcompat.widget.SwitchCompat
import androidx.core.view.GravityCompat
import androidx.drawerlayout.widget.DrawerLayout
import androidx.fragment.app.Fragment
import androidx.lifecycle.ViewModelProvider
import androidx.lifecycle.lifecycleScope
import com.etebarian.meowbottomnavigation.MeowBottomNavigation
import com.example.liberary.App.MyContextWrapper
import com.example.liberary.Course
import com.example.liberary.R
import com.example.liberary.View.Fragment.FavoriteFragment
import com.example.liberary.View.Fragment.HomeFragment
import com.example.liberary.View.Fragment.RecentOpenFragment
import com.example.liberary.ViewModel.ViewModel
import com.example.liberary.constants.Constants
import com.google.android.material.navigation.NavigationView
import kotlinx.android.synthetic.main.activity_home.*
import kotlinx.coroutines.flow.single
import kotlinx.coroutines.launch
import kotlinx.coroutines.runBlocking
import java.util.*


class HomeActivity : AppCompatActivity() {
    private lateinit var courses: ArrayList<Course>
    // the bundle for that we pass the array on to the home fragment
    private val bundle = Bundle()
//  tie DrawerLayout and  ActionBar
    private lateinit var toggle :ActionBarDrawerToggle
    private lateinit var viewModel:ViewModel
    // access the switch view to change it state according to the current appearance
    private lateinit var action: View
    // side menu container
    private lateinit var drawerLayout:DrawerLayout

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_home)
        supportActionBar?.setDisplayHomeAsUpEnabled(true)
        viewModel = ViewModelProvider(this).get(ViewModel::class.java)
        viewModel.initContext(this)
        // side menu
        val navigationView:NavigationView = findViewById(R.id.navigationView)
        // get the menu items from navigation
        val menu: Menu = navigationView.menu
        drawerLayout = findViewById(R.id.navigationDrawer)


        toggle = ActionBarDrawerToggle(this,drawerLayout,R.string.open,R.string.close)
        drawerLayout.addDrawerListener(toggle)
        toggle.syncState()
        supportActionBar?.setDisplayHomeAsUpEnabled(true)

        if(drawerLayout.isDrawerOpen(GravityCompat.START)){
            drawerLayout.close()
        }
        val item =  menu.findItem(R.id.app_bar_switch)
        action = item!!.actionView
        runBlocking { action.findViewById<SwitchCompat>(R.id.switchDarkModeState).isChecked = viewModel.getPreferences().single().isDarkMode }
        navigationView.setNavigationItemSelectedListener {
            when(it.itemId){
                R.id.changeLanguage -> {
                    lifecycleScope.launch {
                        viewModel.getPreferences().collect{
                            if(it.language == "ar") {
                                viewModel.saveToPreferences("en")
                            }
                            else{
                                viewModel.saveToPreferences("ar")
                            }
                        }
                    }
                    val myCustom = Intent(this@HomeActivity, HomeActivity::class.java)

                    myCustom.putExtras(intent)

                    startActivity(myCustom)

                }
                R.id.app_bar_switch -> {
                    if (this.getSharedPreferences(Constants.sharedName,MODE_PRIVATE).getBoolean(Constants.darkMode,false)){
                        AppCompatDelegate.setDefaultNightMode(AppCompatDelegate.MODE_NIGHT_NO)
                        viewModel.saveDarkModeState(false)
                    }else{

                        AppCompatDelegate.setDefaultNightMode(AppCompatDelegate.MODE_NIGHT_YES)
                        viewModel.saveDarkModeState(true)
                    }
                    runBlocking { action.findViewById<SwitchCompat>(R.id.switchDarkModeState).isChecked = viewModel.getPreferences().single().isDarkMode }
                }
                R.id.logout -> {

                    viewModel.clearAllData()
                    restartApp()
                }
            }
            true
        }

        bottomNavigation.show(0)
        bottomNavigation.add(MeowBottomNavigation.Model(0, R.drawable.homeicon))
        bottomNavigation.add(MeowBottomNavigation.Model(1, R.drawable.favoriteicon))
        bottomNavigation.add(MeowBottomNavigation.Model(2, R.drawable.recenticon))

        courses = intent.getSerializableExtra(Constants.courses) as ArrayList<Course>

        // replace fragment on the screen according to the navigation btn id
        bottomNavigation.setOnClickMenuListener { meow ->
            when (meow.id){
                0 -> {
                    replaceFragment(constructFragmentWithCourses())
                }
                1 -> {
                    replaceFragment(FavoriteFragment())
                }
                2 ->{
                    replaceFragment(RecentOpenFragment())
                }
            }
        }

        replaceFragment(constructFragmentWithCourses())

    }

    private fun constructFragmentWithCourses():Fragment{
        val homeFragment = HomeFragment()
        bundle.putSerializable(Constants.homeCourses,courses)
        homeFragment.arguments = bundle
        return  homeFragment
    }

    override fun attachBaseContext(newBase: Context?) {
        val sharedPreferences = newBase?.getSharedPreferences(Constants.sharedName,MODE_PRIVATE)
        val language = sharedPreferences?.getString(Constants.language, "en")
        super.attachBaseContext(newBase?.let { MyContextWrapper.wrap(it, language!!) })

    }

    // call back method that return the selected item from menu
    override fun onOptionsItemSelected(item: MenuItem): Boolean {
        if(toggle.onOptionsItemSelected(item)){
            return  true
        }
        return super.onOptionsItemSelected(item)
    }

    private fun replaceFragment(fragment: Fragment){
        val fragmentManager = supportFragmentManager
        val fragmentTransAction = fragmentManager.beginTransaction()
        fragmentTransAction.replace(R.id.framelayout, fragment)
        fragmentTransAction.commit()
    }
    private fun restartApp(){
        finish()
        startActivity(Intent(this, MainActivity::class.java))
        finishAffinity()
    }
}