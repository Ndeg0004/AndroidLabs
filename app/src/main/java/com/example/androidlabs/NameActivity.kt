package com.example.androidlabs

import android.app.Activity
import android.os.Bundle
import android.widget.Button
import android.widget.TextView
import androidx.appcompat.app.AppCompatActivity

class NameActivity : AppCompatActivity() {
    private lateinit var textView: TextView
    private lateinit var buttonDontCallMeThat: Button
    private lateinit var buttonThankYou: Button
    private var userName: String? = null

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_name)

        textView = findViewById(R.id.textView)
        buttonDontCallMeThat = findViewById(R.id.buttonDontCallMeThat)
        buttonThankYou = findViewById(R.id.buttonThankYou)

        userName = intent.getStringExtra("Name")

        textView.text = "Welcome $userName!"

        buttonDontCallMeThat.setOnClickListener {

            setResult(Activity.RESULT_CANCELED)
            finish()
        }

        buttonThankYou.setOnClickListener {

            setResult(Activity.RESULT_OK)
            finish()
        }
    }
}
