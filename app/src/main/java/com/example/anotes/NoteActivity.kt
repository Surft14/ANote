package com.example.anotes

import android.content.Intent
import android.os.Build
import android.os.Bundle
import android.util.Log
import android.view.Menu
import android.view.MenuItem
import androidx.appcompat.widget.Toolbar
import androidx.activity.enableEdgeToEdge
import androidx.annotation.RequiresApi
import androidx.appcompat.app.AppCompatActivity
import androidx.core.view.ViewCompat
import androidx.core.view.WindowInsetsCompat
import androidx.lifecycle.ViewModelProvider
import com.example.anotes.databinding.ActivityNoteBinding
import com.example.anotes.db_notes.DatabaseProvider
import com.example.anotes.db_notes.Note
import com.example.anotes.model.NoteRepository
import com.example.anotes.view_model.NoteViewModel
import com.example.anotes.view_model.NoteViewModelFactory
import java.time.LocalDate
import java.time.LocalTime

class NoteActivity : AppCompatActivity() {
    private lateinit var binding: ActivityNoteBinding
    private lateinit var noteViewModel: NoteViewModel
    override fun onCreate(savedInstanceState: Bundle?) {
        Log.d("MyLog", "onCreate NoteActivity")
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()

        binding = ActivityNoteBinding.inflate(layoutInflater)
        setContentView(binding.root)


        // Создание репозитория
        val repository = NoteRepository(DatabaseProvider.getDatabase(applicationContext).noteDao())

        //Получаем ViewModel
        // Использование ViewModelFactory
        val factory = NoteViewModelFactory(repository)
        noteViewModel = ViewModelProvider(this, factory).get(NoteViewModel::class.java)


        // Установка Toolbar
        val toolbar = findViewById<Toolbar>(R.id.tbNote)
        setSupportActionBar(toolbar)

        // Включение стрелки назад
        supportActionBar?.setDisplayHomeAsUpEnabled(true)
        supportActionBar?.setHomeAsUpIndicator(R.drawable.ic_arrow_back)

        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.main)) { v, insets ->
            val systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars())
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom)
            insets
        }

        // Работа с UI
        noteViewModel.getAllNotes().observe(this) { notes ->
            // Обновляем список заметок
            updateRecyclerView(notes)
        }
        
    }

    private fun updateRecyclerView(notes: List<Note>?): List<Note> {
        return TODO()
    }

    override fun onCreateOptionsMenu(menu: Menu?): Boolean {
        Log.d("MyLog", "onCreateOptionsMenu NoteActivity")
        menuInflater.inflate(R.menu.note_menu, menu)
        return true
    }

    // Обработка нажатия на стрелку назад

    @RequiresApi(Build.VERSION_CODES.O)
    override fun onOptionsItemSelected(item: MenuItem): Boolean {
        return when (item.itemId) {
            android.R.id.home -> {
                Log.d("MyLog", "click home NoteActivity")
                finish() // Закрыть текущую Activity
                true
            }
            R.id.menuNewNote ->{
                Log.d("MyLog", "click home NoteActivity")
                val note = Note(
                    title = binding.edTitle.text.toString(),
                    content = binding.edContent.text.toString(),
                    date = LocalDate.now().toString(),
                    time = LocalTime.now().toString(),
                    timeStamp = System.currentTimeMillis(), // Текущее время
                )
                noteViewModel.insertNote(note)

                val editIntent = Intent().apply {
                    putExtra("note", note)
                }
                setResult(RESULT_OK, intent)
                finish()
                true
            }
            else -> super.onOptionsItemSelected(item)
        }
    }




}