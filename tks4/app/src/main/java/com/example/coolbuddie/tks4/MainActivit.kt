package com.example.coolbuddie.tks4

import android.os.Bundle
import android.support.v7.app.AppCompatActivity
import android.util.Log
import android.widget.Button
import android.widget.TextView
import com.github.kittinunf.fuel.Fuel
import com.github.kittinunf.fuel.core.FuelError
import com.github.kittinunf.fuel.core.Handler
import com.github.kittinunf.fuel.core.Request
import com.github.kittinunf.fuel.core.Response
import android.arch.core.R

class MainActivity : AppCompatActivity() {
    private var resultText: TextView? = null

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)
        resultText = findViewById(R.id.mainResultText) as TextView


        val b = findViewById(R.id.mainGoButton) as Button
        b.setOnClickListener {
            Fuel.get("https://jsonplaceholder.typicode.com/users").responseString(object : Handler<String> {
                override fun success(request: Request, response: Response, value: String) {
                    Log.e("message", "success")
                    resultText!!.text = value
                }

                override fun failure(request: Request, response: Response, fuelError: FuelError) {
                    Log.e("message", "error" + fuelError!!.exception.message)

                }


            }
            )
        }
    }
}

