package com.example.weathertutorial

import android.os.AsyncTask
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.view.View
import android.widget.ProgressBar
import android.widget.RelativeLayout
import android.widget.TextView
import org.json.JSONObject
import java.net.URL

class MainActivity : AppCompatActivity() {
    val CITY: String = "dhaka,bd"
    val API: String = "0e863d0ed1d76744cb0ad1fb01a10d3c"
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        weatherTask().execute()
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
            }
            catch (e:Exception){

            }
        }
    }
}