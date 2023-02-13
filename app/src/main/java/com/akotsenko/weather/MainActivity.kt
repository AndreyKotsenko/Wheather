package com.akotsenko.weather

import android.annotation.SuppressLint
import android.os.AsyncTask
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.widget.Toast
import kotlinx.android.synthetic.main.activity_main.*
import org.json.JSONException
import org.json.JSONObject
import java.io.BufferedReader
import java.io.IOException
import java.io.InputStream
import java.io.InputStreamReader
import java.net.HttpURLConnection
import java.net.MalformedURLException
import java.net.URL

class MainActivity : AppCompatActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        main_btn.setOnClickListener {
            if(user_field.text.toString().trim().equals(""))
                Toast.makeText(this, R.string.no_user_input, Toast.LENGTH_LONG).show()
            else {
                var city: String = user_field.text.toString()
                var key: String = "9a1a377af68d96113e08d0fa2c3d11e1"
                var url: String = "https://api.openweathermap.org/data/2.5/weather?q=$city&appid=$key&units=metric&lang=ru"

                GetURLData().execute(url)

            }
        }

    }

    inner class GetURLData : AsyncTask<String, String, String>() {

        protected override fun onPreExecute() {
            super.onPreExecute()
            result_info.setText("Ожидайте...")
        }
        override fun doInBackground(vararg p0: String?): String? {
            var connection: HttpURLConnection? = null

            try {
                var url: URL = URL(p0[0])
                connection = url.openConnection() as HttpURLConnection
                connection.connect()

                var stream: InputStream = connection.inputStream

                var line: String = ""

                val allText = stream.bufferedReader().use(BufferedReader::readText)

                return allText
            } catch (e: MalformedURLException) {
                e.printStackTrace()
            } catch (e: IOException) {
                e.printStackTrace()
            } finally {
                if(connection != null)
                    connection.disconnect()


            }
            return null
        }

        @SuppressLint("SetTextI18n")
        protected override fun onPostExecute(result: String?) {
            super.onPostExecute(result)

            try {
                var jsonObject: JSONObject = JSONObject(result)
                result_info.setText("Темпрература: ${jsonObject.getJSONObject("main").getDouble("temp")}")
            } catch (e: JSONException) {
                e.printStackTrace()
            }

        }

    }
}