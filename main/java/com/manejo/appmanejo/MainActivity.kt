package com.manejo.appmanejo

import android.content.Intent
import android.os.Bundle
import android.widget.Button
import android.widget.EditText
import android.widget.TextView
import androidx.activity.enableEdgeToEdge
import androidx.appcompat.app.AppCompatActivity
import androidx.core.view.ViewCompat
import androidx.core.view.WindowInsetsCompat

class MainActivity : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        setContentView(R.layout.activity_main)
        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.main)) { v, insets ->
            val systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars())
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom)
            insets
        }

        val text = findViewById<TextView>(R.id.text)
        val name = findViewById<EditText>(R.id.name)
        val button = findViewById<Button>(R.id.button)

        button.setOnClickListener {
            //text.text = "Holaa, cómo vas"
            //text.text = "${text.text}, ${name.text
            val intent = Intent(this, SecondActivity::class.java)
            intent.putExtra("dato", name.text.toString())
            startActivity(intent)
        }

    }
}