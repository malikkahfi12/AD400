package com.learetechno.ad400.forecast

import android.content.Context
import android.content.Intent
import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.lifecycle.Observer
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.google.android.material.floatingactionbutton.FloatingActionButton
import com.learetechno.ad400.*

import com.learetechno.ad400.details.ForecastDetailsActivity

/**
 * A simple [Fragment] subclass.
 */
class CurrentForecastFragment : Fragment() {
    private val forecastRepository = ForecastRepository()
    private lateinit var tempDisplaySettingsManager: TempDisplaySettingsManager

    private lateinit var appNavigator: AppNavigator
    override fun onAttach(context: Context) {
        super.onAttach(context)
        appNavigator = context as AppNavigator
    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        tempDisplaySettingsManager = TempDisplaySettingsManager(requireContext())

        val zipcode = arguments!!.getString(KEY_ZIPCDE) ?: ""
        // Inflate the layout for this fragment
        val view = inflater.inflate(R.layout.fragment_current_forecast, container, false)

        val locationEntryButton: FloatingActionButton = view.findViewById(R.id.locationEntryButton)
        locationEntryButton.setOnClickListener {
            appNavigator.navigateToLocationEntry()
        }
        val forecastList : RecyclerView = view.findViewById(R.id.forecastList)



        forecastList.layoutManager = LinearLayoutManager(requireContext())

        val dailyForecastAdapter = DailyForecastAdapter(tempDisplaySettingsManager){forecastItem ->
            showForecastDetails(forecastItem)
        }
        forecastList.adapter = dailyForecastAdapter
        val weeklyForecastObserver = Observer<List<DailyForecast>>{ forecastItems ->
            // update our list adapter
            dailyForecastAdapter.submitList(forecastItems)
        }
        forecastRepository.weeklyForecast.observe(this, weeklyForecastObserver)

        forecastRepository.loadForecast(zipcode)

        return view
    }

    private fun showForecastDetails(forecast : DailyForecast){
        val forecastDetailsIntent = Intent(requireContext(), ForecastDetailsActivity::class.java)
        forecastDetailsIntent.putExtra("key_temp", forecast.temp)
        forecastDetailsIntent.putExtra("key_description", forecast.description)
        startActivity(forecastDetailsIntent)
    }

    companion object{
        const val KEY_ZIPCDE = "key_zipcode"

        fun newInstance(zipcode : String) : CurrentForecastFragment{
            val fragment= CurrentForecastFragment()

            val args = Bundle()
            args.putString(KEY_ZIPCDE, zipcode)
            fragment.arguments = args

            return fragment
        }
    }

}
