package com.example.mydata3itd

import android.content.Context
import android.content.Intent
import android.content.SharedPreferences
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.widget.Button
import android.widget.EditText
import android.widget.TextView

class MainActivity : AppCompatActivity() {

    private val sharedPrefFile = "kotlinsharedpreference"

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        val inputId = findViewById<EditText>(R.id.editTextID)
        val inputName = findViewById<EditText>(R.id.editTextName)
        val outputId = findViewById<TextView>(R.id.tvIdResult)
        val outputName = findViewById<TextView>(R.id.tvNameResult)

        val btnSave = findViewById<Button>(R.id.btnSave)
        val btnView = findViewById<Button>(R.id.btnView)
        val btnClear = findViewById<Button>(R.id.btnClear)
        val btnNext = findViewById<Button>(R.id.btnNext)

        val sharedPreferences : SharedPreferences = this.getSharedPreferences(sharedPrefFile, Context.MODE_PRIVATE) //Context.MODE PRIVATE specifies that sharedpref will only be used dito sa app na to

        btnSave.setOnClickListener{
            val id : Int = Integer.parseInt(inputId.text.toString())
            val name : String = inputName.text.toString()
            val editor : SharedPreferences.Editor = sharedPreferences.edit() //one dimensional array ung SharedPreferences, depends on the size of what we are putting
            editor.putInt("id_key", id)
            editor.putString("name_key", name)
            editor.apply()
            editor.commit()
        }

        btnView.setOnClickListener {
            val sharedIdValue = sharedPreferences.getInt("id_key", 0) //start index
            val sharedNameValue = sharedPreferences.getString("name_key", "default") //default value

            //pagwalang laman
            if(sharedIdValue.equals(0) && sharedNameValue.equals("default")){
                outputId.setText("${sharedIdValue.toString()}") //output 0
                outputName.setText("${sharedNameValue}").toString()
            } else {
                outputId.setText(sharedIdValue.toString())
                outputName.setText(sharedNameValue).toString()
            }
        }

        btnClear.setOnClickListener {
            val editor = sharedPreferences.edit()
            editor.clear()
            editor.apply()
            outputId.setText("".toString())
            outputName.setText("").toString()

        }

        btnNext.setOnClickListener {
            val i = Intent(this, Database::class.java)
            startActivity(i)
        }
    }
}