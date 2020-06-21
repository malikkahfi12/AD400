package com.learetechno.ad400.forecast

import android.content.Context
import android.content.Intent
import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.lifecycle.Observer
import androidx.navigation.fragment.findNavController
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.google.android.material.floatingactionbutton.FloatingActionButton
import com.learetechno.ad400.*

import com.learetechno.ad400.details.ForecastDetailsFragment

/**
 * A simple [Fragment] subclass.
 */
class WeeklyForecastFragment : Fragment() {
    private val forecastRepository = ForecastRepository()
    private lateinit var tempDisplaySettingsManager: TempDisplaySettingsManager



    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        tempDisplaySettingsManager = TempDisplaySettingsManager(requireContext())

        val zipcode = arguments?.getString(KEY_ZIPCDE) ?: ""
        // Inflate the layout for this fragment
        val view = inflater.inflate(R.layout.fragment_weekly_forecast, container, false)

        val locationEntryButton: FloatingActionButton = view.findViewById(R.id.locationEntryButton)
        locationEntryButton.setOnClickListener {
            showLocationEntry()
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
        forecastRepository.weeklyForecast.observe(viewLifecycleOwner, weeklyForecastObserver)

        forecastRepository.loadForecast(zipcode)

        return view
    }

    private fun showLocationEntry(){
        val action = WeeklyForecastFragmentDirections.actionWeeklyForecastFragmentToLocationEntryFragment()
        findNavController().navigate(action)
    }

    private fun showForecastDetails(forecast : DailyForecast){
        val action = WeeklyForecastFragmentDirections.actionWeeklyForecastFragmentToForecastDetailsFragment(forecast.temp, forecast.description)
        findNavController().navigate(action)
    }

    companion object{
        const val KEY_ZIPCDE = "key_zipcode"

        fun newInstance(zipcode : String) : WeeklyForecastFragment{
            val fragment= WeeklyForecastFragment()

            val args = Bundle()
            args.putString(KEY_ZIPCDE, zipcode)
            fragment.arguments = args

            return fragment
        }
    }

}
