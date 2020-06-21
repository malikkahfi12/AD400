package com.learetechno.ad400

import android.os.Bundle
import android.view.Menu
import android.view.MenuInflater
import android.view.MenuItem

import androidx.appcompat.app.AppCompatActivity
import androidx.appcompat.widget.Toolbar
import androidx.navigation.findNavController
import androidx.navigation.ui.AppBarConfiguration
import androidx.navigation.ui.setupWithNavController
import com.google.android.material.bottomnavigation.BottomNavigationView
import com.learetechno.ad400.forecast.CurrentForecastFragmentDirections


class MainActivity : AppCompatActivity() {


    private lateinit var tempDisplaySettingsManager: TempDisplaySettingsManager
    // region Setup Method
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)


        tempDisplaySettingsManager = TempDisplaySettingsManager(this)

        val navController = findNavController(R.id.nav_host_fragment)
        val appBarConfiguration = AppBarConfiguration(navController.graph)
        findViewById<Toolbar>(R.id.toolbar).setTitle(R.string.app_name)
        findViewById<BottomNavigationView>(R.id.bottomNavigationView).setupWithNavController(navController)
    }

    override fun onCreateOptionsMenu(menu: Menu?): Boolean {
        val inflater : MenuInflater = menuInflater
        inflater.inflate(R.menu.settings_menu, menu)
        return true
    }

    override fun onOptionsItemSelected(item: MenuItem): Boolean {
        // Handle item selection
        return when(item.itemId){
            R.id.tempDisplaySettings -> {
                // do something
                showTempDisplaySettingDialog(this, tempDisplaySettingsManager)
                return true
            }
            else -> super.onOptionsItemSelected(item)
        }
    }
}
