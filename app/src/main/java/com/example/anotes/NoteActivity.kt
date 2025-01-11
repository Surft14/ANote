package com.example.anotes

import android.app.Activity
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
import androidx.room.util.copy
import com.example.anotes.constant.Constant
import com.example.anotes.databinding.ActivityNoteBinding
import com.example.anotes.db_notes.DatabaseProvider
import com.example.anotes.db_notes.Note
import com.example.anotes.model.NoteRepository
import com.example.anotes.model.OperationResult
import com.example.anotes.view_model.NoteViewModel
import com.example.anotes.view_model.NoteViewModelFactory
import java.time.LocalDate
import java.time.LocalTime


class NoteActivity : AppCompatActivity() {
    private lateinit var binding: ActivityNoteBinding
    private lateinit var noteViewModel: NoteViewModel
    override fun onCreate(savedInstanceState: Bundle?) {
        Log.d("MyLog", "NoteActivity: onCreate start")
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
        Log.d("MyLog", "NoteActivity: Start Create ViewModel")
        // Создание репозитория
        val repository = NoteRepository(DatabaseProvider.getDatabase().noteDao())

        //Получаем ViewModel
        // Использование ViewModelFactory
        val factory = NoteViewModelFactory(repository)
        noteViewModel = ViewModelProvider(this, factory).get(NoteViewModel::class.java)
        Log.d("MyLog", "NoteActivity: end Create ViewModel")
        Log.d("MyLog", "NoteActivity: onCreate finish")
    }

    override fun onCreateOptionsMenu(menu: Menu?): Boolean {
        Log.d("MyLog", "NoteActivity: onCreateOptionsMenu")
        menuInflater.inflate(R.menu.note_menu, menu)
        return true
    }

    // Обработка нажатия на стрелку назад
    @RequiresApi(Build.VERSION_CODES.O)
    override fun onOptionsItemSelected(item: MenuItem): Boolean {
        return when (item.itemId) {
            android.R.id.home -> {
                Log.d("MyLog", "NoteActivity: click home")
                finish() // Закрыть текущую Activity
                true
            }
            R.id.noteMenuSaveNote ->{
                Log.d("MyLog", "NoteActivity: click home")
                val note = Note(
                    title = binding.edTitle.text.toString(),
                    content = binding.edContent.text.toString(),
                    date = LocalDate.now().toString(),
                    time = LocalTime.now().toString(),
                    timeStamp = System.currentTimeMillis(), // Текущее время
                )

                /*
                val result = noteViewModel.insertResult
                val intent = Intent()
                intent.putExtra(Constant.keyNote, note)

                when(result){
                    is OperationResult.Success ->{
                        setResult(Activity.RESULT_OK, intent)
                    }
                    is OperationResult.Error -> {
                        setResult(Activity.RESULT_CANCELED, intent)
                    }
                    else ->{
                        setResult(Activity.RESULT_CANCELED, intent)
                    }
                }
                finish()*/
                noteViewModel.insertNote(note)
                noteViewModel.insertResult.observe(this) { result ->
                    when (result) {
                        is OperationResult.Success -> {
                            val newNote = note.copy(id = result.newId)
                            val intent = Intent().apply {
                                putExtra(Constant.keyNote, newNote)
                            }
                            setResult(Activity.RESULT_OK, intent)
                            finish()
                        }
                        is OperationResult.Error -> {
                            Log.e("MyLog", "NoteActivity: Error inserting note: ${result.exception.message}")
                            setResult(Activity.RESULT_CANCELED)
                            finish()
                        }
                    }
                }
                true
            }
            else -> super.onOptionsItemSelected(item)
        }
    }

}