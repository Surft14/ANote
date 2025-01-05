package com.example.anotes

import android.content.Intent
import android.os.Bundle
import android.util.Log
import android.view.Menu
import android.view.MenuItem
import androidx.appcompat.widget.Toolbar
import androidx.activity.enableEdgeToEdge
import androidx.appcompat.app.AppCompatActivity
import androidx.core.view.ViewCompat
import androidx.core.view.WindowInsetsCompat
import com.example.anotes.databinding.ActivityMainBinding

class MainActivity : AppCompatActivity() {
    private lateinit var binding: ActivityMainBinding
    override fun onCreate(savedInstanceState: Bundle?) {
        Log.d("MyLog", "onCreate MainActivity")
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        binding = ActivityMainBinding.inflate(layoutInflater)
        setContentView(binding.root)

        // Установите Toolbar
        val toolbar = findViewById<Toolbar>(R.id.tbMain)
        setSupportActionBar(toolbar)

        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.main)) { v, insets ->
            val systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars())
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom)
            insets
        }



    }

    override fun onCreateOptionsMenu(menu: Menu?): Boolean {
        Log.d("MyLog", "onCreateOptionsMenu MainActivity")
        menuInflater.inflate(R.menu.main_menu, menu)
        return true
    }

    override fun onOptionsItemSelected(item: MenuItem): Boolean {
        when (item.itemId) {
            R.id.menuNewNote -> {
                // Переход на NoteActivity
                Log.d("MyLog", "menuNewNote MainActivity")
                val intent = Intent(this, NoteActivity::class.java)
                startActivity(intent)
                return true
            }
            R.id.menuSearchNote ->{
                // Переход на SearchActivity
                Log.d("MyLog", "menuSearchNote MainActivity")
                val  intent = Intent(this, SearchActivity::class.java)
                startActivity(intent)
                return true
            }
        }
        return super.onOptionsItemSelected(item)
    }


}