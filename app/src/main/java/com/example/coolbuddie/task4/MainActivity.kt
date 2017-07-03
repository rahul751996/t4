package com.example.coolbuddie.task4

import android.app.ProgressDialog
import android.os.AsyncTask
import android.os.Bundle
import android.support.v7.app.AppCompatActivity
import android.util.Log
import android.widget.ListAdapter
import android.widget.ListView
import android.widget.SimpleAdapter
import android.widget.Toast

import org.json.JSONArray
import org.json.JSONException
import org.json.JSONObject

import java.util.ArrayList
import java.util.HashMap

class MainActivity : AppCompatActivity() {

    private val TAG = "Main"

    private var pDialog: ProgressDialog? = null
    private var lv: ListView? = null

    internal var contactList: ArrayList<HashMap<String, String>>

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        contactList = ArrayList<HashMap<String, String>>()

        lv = findViewById(R.id.list) as ListView

        GetContacts().execute()
    }

    /**
     * Async task class to get json by making HTTP call
     */
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
            val sh = HttpHandler()

            // Making a request to url and getting response
            val jsonStr = sh.makeServiceCall(url)

            Log.e(TAG, "Response from url: " + jsonStr!!)

            if (jsonStr != null) {
                try {
                    val jsonObj = JSONObject(jsonStr)

                    // Getting JSON Array node
                    val contacts = jsonObj.getJSONArray("contacts")

                    // looping through All Contacts
                    for (i in 0..contacts.length() - 1) {
                        val c = contacts.getJSONObject(i)

                        val id = c.getString("id")
                        val name = c.getString("name")
                        val email = c.getString("email")
                        val address = c.getString("address")
                        val gender = c.getString("gender")

                        // Phone node is JSON Object
                        val phone = c.getJSONObject("phone")
                        val mobile = phone.getString("mobile")
                        val home = phone.getString("home")
                        val office = phone.getString("office")

                        // tmp hash map for single contact
                        val contact = HashMap<String, String>()

                        // adding each child node to HashMap key => value
                        contact.put("id", id)
                        contact.put("name", name)
                        contact.put("email", email)
                        contact.put("mobile", mobile)

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
                            "Couldn't get json from server. Check LogCat for possible errors!",
                            Toast.LENGTH_LONG).show()
                }

            }

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

    companion object {

        // URL to get contacts JSON
        private val url = "http://api.androidhive.info/contacts/"
    }
}