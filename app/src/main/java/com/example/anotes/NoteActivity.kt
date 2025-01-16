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
import androidx.activity.result.ActivityResultLauncher
import androidx.activity.result.contract.ActivityResultContracts
import androidx.annotation.RequiresApi
import androidx.appcompat.app.AppCompatActivity
import androidx.core.view.ViewCompat
import androidx.core.view.WindowInsetsCompat
import androidx.lifecycle.ViewModelProvider
import com.example.anotes.constant.Constant
import com.example.anotes.databinding.ActivityNoteBinding
import com.example.anotes.datebase.DatabaseProvider
import com.example.anotes.datebase.db_notes.Note
import com.example.anotes.model.NoteRepository
import com.example.anotes.model.OperationResult
import com.example.anotes.view_model.NoteViewModel
import com.example.anotes.view_model.NoteViewModelFactory
import java.time.LocalDate
import java.time.LocalTime


class NoteActivity : AppCompatActivity() {
    private lateinit var binding: ActivityNoteBinding
    private lateinit var noteViewModel: NoteViewModel
    private var noteUp: Note? = null
    private lateinit var noteLauncher: ActivityResultLauncher<Intent>
    override fun onCreate(savedInstanceState: Bundle?) {
        Log.d("MyLog", "NoteActivity: onCreate start")
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()

        binding = ActivityNoteBinding.inflate(layoutInflater)
        setContentView(binding.root)

        // Установка Toolbar
        val toolbar = findViewById<Toolbar>(R.id.tbNote)
        setSupportActionBar(toolbar)

        noteUp = intent.getSerializableExtra("note") as? Note
        if (noteUp != null){
            Log.d("MyLog", "NoteActivity: Received note - id=${noteUp!!.id}, title=${noteUp!!.title}, content=${noteUp!!.content}")
            binding.edTitle.setText(noteUp!!.title)
            binding.edContent.setText(noteUp!!.content)
        }
        else {
            Log.e("MyLog", "NoteActivity: Received note null")
        }

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
        val repository = NoteRepository(
            DatabaseProvider.getDatabase().noteDao(),
            DatabaseProvider.getDatabase().categoryDao(),
            DatabaseProvider.getDatabase().favoriteDao()
        )
        //Получаем ViewModel
        // Использование ViewModelFactory
        val factory = NoteViewModelFactory(repository)
        noteViewModel = ViewModelProvider(this, factory).get(NoteViewModel::class.java)

        noteLauncher = registerForActivityResult(ActivityResultContracts.StartActivityForResult()){ result ->
            if (result.resultCode == Activity.RESULT_OK){
                Log.d("MyLog", "NoteActivity: received result from CategoryActivity: result_ok")
            }

        }

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
                Log.d("MyLog", "NoteActivity: click noteMenuSaveNote")
                if(noteUp == null){
                    Log.d("MyLog", "NoteActivity: save new note")
                    val note = Note(
                        title = binding.edTitle.text.toString(),
                        content = binding.edContent.text.toString(),
                        date = LocalDate.now().toString(),
                        time = LocalTime.now().toString(),
                        timeStamp = System.currentTimeMillis(), // Текущее время
                    )

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
                                Log.e(
                                    "MyLog",
                                    "NoteActivity: Error inserting note: ${result.exception.message}"
                                )
                                setResult(Activity.RESULT_CANCELED)
                                finish()
                            }
                        }
                    }
                    true
                }
                else{
                    Log.d("MyLog", "NoteActivity: update")
                    noteUp!!.title = binding.edTitle.text.toString()
                    noteUp!!.content = binding.edContent.text.toString()
                    noteViewModel.updateNote(noteUp!!)
                    noteViewModel.insertResult.observe(this) { result ->
                        when (result) {
                            is OperationResult.Success -> {
                                Log.e("MyLog", "NoteActivity: Success update")
                            }
                            is OperationResult.Error -> {
                                Log.e("MyLog", "NoteActivity: Error update note: ${result.exception.message}")
                            }
                        }
                    }
                    finish()
                    true
                }
            }
            R.id.noteMenuDeleteNote -> {
                Log.d("MyLog", "NoteActivity: click delete")
                if (noteUp != null){
                    noteViewModel.deleteNote(noteUp!!)
                    noteViewModel.insertResult.observe(this) { result ->
                        when (result) {
                            is OperationResult.Success -> {
                                Log.e("MyLog", "NoteActivity: Success delete")
                            }
                            is OperationResult.Error -> {
                                Log.e("MyLog", "NoteActivity: Error delete note: ${result.exception.message}")
                            }
                        }
                    }
                }
                finish()
                true
            }
            R.id.noteMenuCategory -> {
                Log.d("MyLog", "NoteActivity: click noteMenuCategory")
                val intent = Intent(this, CategoryActivity::class.java)
                noteLauncher.launch(intent)
                true
            }
            else -> super.onOptionsItemSelected(item)

        }
    }

}