package com.example.androidlabs

import android.content.Intent
import android.os.Bundle
import android.widget.Button
import android.widget.EditText
import android.widget.TextView
import android.widget.Toast
import androidx.activity.result.contract.ActivityResultContracts
import androidx.appcompat.app.AppCompatActivity

class MainActivity : AppCompatActivity() {
    private lateinit var textView: TextView
    private lateinit var editText: EditText
    private lateinit var button: Button
    private val launcher = registerForActivityResult(ActivityResultContracts.StartActivityForResult()) { result ->
        handleActivityResult(result.resultCode, result.data)
    }
    private var text: String? = null

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        textView = findViewById(R.id.textView)
        editText = findViewById(R.id.editText)
        button = findViewById(R.id.button)

        loadData()
        updateViews()

        button.setOnClickListener {
            textView.text = editText.text.toString()
            saveData()
            loadData()

            val intent = Intent(this@MainActivity, NameActivity::class.java)
            intent.putExtra("Name",editText.text.toString())
            launcher.launch(intent)

            textView.text = editText.text.toString()
        }
    }

    private fun saveData() {
        val sharedPreferences = getSharedPreferences(SHARED_PREFS, MODE_PRIVATE)
        val editor = sharedPreferences.edit()
        editor.putString(TEXT, editText.text.toString())
        editor.apply()
        Toast.makeText(this, "Info Saved", Toast.LENGTH_LONG).show()
    }

    private fun loadData() {
        val sharedPreferences = getSharedPreferences(SHARED_PREFS, MODE_PRIVATE)
        text = sharedPreferences.getString(TEXT, "")
    }

    private fun updateViews() {
        textView.text = text
    }

    private fun handleActivityResult(resultCode: Int, data: Intent?) {
        if (resultCode == RESULT_OK) {
            if (data != null) {

                val result = data.getStringExtra("result")

            }
        } else if (resultCode == RESULT_CANCELED) {

        }
    }

    companion object {
        const val SHARED_PREFS = "sharedPrefs"
        const val TEXT = "text"
        const val REQUEST_CODE_NAME_ACTIVITY = 1
        const val RESULT_CODE_CHANGE_NAME = 0
        const val RESULT_CODE_HAPPY = 1
    }
}

