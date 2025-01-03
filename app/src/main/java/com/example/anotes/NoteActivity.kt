package com.example.anotes

import android.os.Bundle
import android.view.Menu
import android.view.MenuItem
import androidx.appcompat.widget.Toolbar
import androidx.activity.enableEdgeToEdge
import androidx.appcompat.app.AppCompatActivity
import androidx.core.view.ViewCompat
import androidx.core.view.WindowInsetsCompat
import com.example.anotes.databinding.ActivityNoteBinding


class NoteActivity : AppCompatActivity() {
    private lateinit var binding: ActivityNoteBinding
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()

        binding = ActivityNoteBinding.inflate(layoutInflater)
        setContentView(binding.root)

        // Установка Toolbar
        val toolbar = findViewById<Toolbar>(R.id.tbNote)
        setSupportActionBar(toolbar)

        // Включение стрелки назад
        supportActionBar?.setDisplayHomeAsUpEnabled(true)
        supportActionBar?.setHomeAsUpIndicator(R.drawable.ic_arrow_back) // Опционально

        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.main)) { v, insets ->
            val systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars())
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom)
            insets
        }
    }

    override fun onCreateOptionsMenu(menu: Menu?): Boolean {
        menuInflater.inflate(R.menu.note_menu, menu)
        return true
    }

    // Обработка нажатия на стрелку назад
    override fun onOptionsItemSelected(item: MenuItem): Boolean {
        return when (item.itemId) {
            android.R.id.home -> {
                finish() // Закрыть текущую Activity
                true
            }
            else -> super.onOptionsItemSelected(item)
        }
    }

}