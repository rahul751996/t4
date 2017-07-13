package com.example.coolbuddie.tks4

//import android.app.ProgressDialog
import android.os.AsyncTask
import android.os.Bundle
//import android.os.Environment
import android.support.v7.app.AppCompatActivity
import android.util.Log
import android.app.ProgressDialog
import android.widget.*
import android.arch.core.R

import com.github.kittinunf.fuel.Fuel
import com.github.kittinunf.fuel.core.FuelError
import com.github.kittinunf.fuel.core.Handler
import com.github.kittinunf.fuel.core.Request
import com.github.kittinunf.fuel.core.Response
import org.json.JSONException
import org.json.JSONObject

//import java.io.File
//import java.net.URL
import java.util.*

class MainActivity : AppCompatActivity() {

    private val TAG = "Main"

    private var pDialog: ProgressDialog? = null
    private var lv: ListView? = null

    internal var contactList: ArrayList<HashMap<String, String>> = null!!


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        contactList = ArrayList<HashMap<String, String>>()

        lv = findViewById(R.id.list) as ListView

        GetContacts().execute()
    }


    private inner class GetContacts : AsyncTask<Void, Void, Void>() {

        override fun onPreExecute() {
            super.onPreExecute()
            // Showing progress dialog
            pDialog = ProgressDialog(this@MainActivity)
            pDialog!!.setMessage("Please wait...")
            pDialog!!.setCancelable(false)
            pDialog!!.show()

        }

        override fun doInBackground(vararg arg0: Void): Void? {
             Fuel.get("https://jsonplaceholder.typicode.com/users").responseString(object : Handler<String> {
                override fun success(request: Request, response: Response, value: String) {
                    Log.e("message", "success")

            Log.e(TAG, "Response from url: " + value)

            if (true) {
                try {
                    val jsonObj = JSONObject(value)

                    // Getting JSON Array node
                    val contacts = jsonObj.getJSONArray("contacts")

                    // looping through All Contacts
                    for (i in 0..contacts.length() - 1) {
                        val c = contacts.getJSONObject(i)

                        val id = c.getString("id")
                        val name = c.getString("name")
                        val email = c.getString("email")

                        

                        val contact = HashMap<String, String>()

                        // adding each child node to HashMap key => value
                        contact.put("id", id)
                        contact.put("name", name)
                        contact.put("email", email)
                       
                        // adding contact to contact list
                        contactList.add(contact)
                    }
                } catch (e: JSONException) {
                    Log.e(TAG, "Json parsing error: " + e.message)
                    runOnUiThread {
                        Toast.makeText(applicationContext,
                                "Json parsing error: " + e.message,
                                Toast.LENGTH_LONG).show()
                    }

                }

            } else {
                Log.e(TAG, "Couldn't get json from server.")
                runOnUiThread {
                    Toast.makeText(applicationContext,
                            "Couldn't get json from server",
                            Toast.LENGTH_LONG).show()
                }


            }
                    override fun failure(request: Request, response: Response, fuelError: FuelError): Unit? {
                        Log.e("message", "error" + fuelError!!.exception.message)
                        // Making a request to url and getting response
                    }
                    )}


                        return null
        }

        override fun onPostExecute(result: Void) {
            super.onPostExecute(result)
            // Dismiss the progress dialog
            if (pDialog!!.isShowing)
                pDialog!!.dismiss()
            /**
             * Updating parsed JSON data into ListView
             */
            val adapter = SimpleAdapter(
                    this@MainActivity, contactList,
                    R.layout.list_item, arrayOf("name", "email", "mobile"), intArrayOf(R.id.name, R.id.email, R.id.mobile))

            lv!!.adapter = adapter
        }

    }


}




