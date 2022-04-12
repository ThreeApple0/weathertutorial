package com.example.weathertutorial

import android.os.AsyncTask
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.view.View
import android.widget.ProgressBar
import android.widget.RelativeLayout
import android.widget.TextView
import androidx.swiperefreshlayout.widget.SwipeRefreshLayout
import org.json.JSONObject
import java.net.URL
import java.text.SimpleDateFormat
import java.util.*
import kotlin.math.roundToInt
import kotlin.math.roundToLong

class MainActivity : AppCompatActivity() {
    val CITY: String = "seoul"
    val API: String = "0e863d0ed1d76744cb0ad1fb01a10d3c"
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        weatherTask().execute()

        val research_L = findViewById<SwipeRefreshLayout>(R.id.research_L)
        research_L.setOnRefreshListener {
            weatherTask().execute()
            research_L.isRefreshing = false
        }
    }

    inner class weatherTask(): AsyncTask<String, Void,String>(){
        override fun onPreExecute() {
            super.onPreExecute()
            findViewById<ProgressBar>(R.id.loader).visibility = View.VISIBLE
            findViewById<RelativeLayout>(R.id.main_C).visibility = View.GONE
            findViewById<TextView>(R.id.errortext).visibility = View.GONE
        }

        override fun doInBackground(vararg p0: String?): String? {
            var response:String?
            try{
                response = URL("https://api.openweathermap.org/data/2.5/weather?q=$CITY&APPID=$API")
                    .readText(Charsets.UTF_8)
            }
            catch (e:Exception){
                response=null
            }

            return response
        }

        override fun onPostExecute(result: String?) {
            super.onPostExecute(result)
            try {
                val jsonobj = JSONObject(result)
                val main = jsonobj.getJSONObject("main")
                val sys = jsonobj.getJSONObject("sys")
                val wind = jsonobj.getJSONObject("wind")
                val weather = jsonobj.getJSONArray("weather").getJSONObject(0)
                val updatedAt:Long = jsonobj.getLong("dt")
                val updatedAtText = "기준 : "+SimpleDateFormat("yyyy/MM/dd hh:mm a", Locale.ENGLISH).format(Date(updatedAt*1000))
                val temp = ((((main.getString("temp").toFloat() - 273 )*10).roundToLong())/10f).toString()+"°C"
                val tempMin = "최저온도 : "+((((main.getString("temp_min").toFloat() - 273 )*10).roundToLong())/10f).toString()+"°C"
                val tempMax = "최고온도 : "+((((main.getString("temp_max").toFloat() - 273 )*10).roundToLong())/10f).toString()+"°C"
                val pressure = main.getString("pressure")
                val humidity = main.getString("humidity")
                val sunrise:Long = sys.getLong("sunrise")
                val sunset:Long = sys.getLong("sunset")
                val windSpeed = wind.getString("speed")
                val weatherDescription = weather.getString("description")
                val address = jsonobj.getString("name")+", "+sys.getString("country")

                findViewById<TextView>(R.id.address).text=address
                findViewById<TextView>(R.id.updated_up).text=updatedAtText
                findViewById<TextView>(R.id.status).text=(weatherDescription).uppercase()
                findViewById<TextView>(R.id.temp).text=temp
                findViewById<TextView>(R.id.min_temp).text=tempMin
                findViewById<TextView>(R.id.max_temp).text=tempMax
                findViewById<TextView>(R.id.sunrise).text=SimpleDateFormat("hh:mm a",Locale.ENGLISH).format(Date(sunrise*1000))
                findViewById<TextView>(R.id.sunset).text=SimpleDateFormat("hh:mm a",Locale.ENGLISH).format(Date(sunset*1000))
                findViewById<TextView>(R.id.wind).text=windSpeed
                findViewById<TextView>(R.id.pressure).text=pressure
                findViewById<TextView>(R.id.humidity).text=humidity

                findViewById<ProgressBar>(R.id.loader).visibility = View.GONE
                findViewById<RelativeLayout>(R.id.main_C).visibility = View.VISIBLE


            }
            catch (e:Exception){
                findViewById<ProgressBar>(R.id.loader).visibility = View.GONE
                findViewById<TextView>(R.id.errortext).visibility = View.VISIBLE
                findViewById<TextView>(R.id.errortext).text = e.toString()
            }
        }
    }
}