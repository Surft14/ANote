package com.example.anotes

import android.app.Activity
import android.content.Intent
import android.os.Build
import android.os.Bundle
import android.util.Log
import android.view.Menu
import android.view.MenuItem
import android.view.View
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
    private var favoriteNote = false
    private lateinit var noteLauncher: ActivityResultLauncher<Intent>
    private var categoryUp: String = Constant.Category.firstCategory
    override fun onCreate(savedInstanceState: Bundle?) {
        Log.d("MyLog", "NoteActivity: onCreate start")
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()

        binding = ActivityNoteBinding.inflate(layoutInflater)
        setContentView(binding.root)

        // Установка Toolbar
        val toolbar = findViewById<Toolbar>(R.id.tbNote)
        setSupportActionBar(toolbar)
        // Пытаемся получить заметку если нам его отправили
        noteUp = intent.getSerializableExtra(Constant.Key.keyNote) as? Note
        if (noteUp != null){// Сработает если отправили
            Log.d("MyLog", "NoteActivity: Received note - id=${noteUp!!.id}, title=${noteUp!!.title}, content=${noteUp!!.content}")
            binding.edTitle.setText(noteUp!!.title)// Доболяем title из заметки
            binding.edContent.setText(noteUp!!.content)// И content тооже из заметки
            favoriteNote = noteUp!!.favorite// Добовляем favorite or not
            categoryUp = noteUp!!.category// добовляем категорию
        }
        else {// Сработает если не отправили
            Log.w("MyLog", "NoteActivity: Received note null")
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
        )
        //Получаем ViewModel
        // Использование ViewModelFactory
        val factory = NoteViewModelFactory(repository)
        noteViewModel = ViewModelProvider(this, factory).get(NoteViewModel::class.java)

        noteLauncher = registerForActivityResult(ActivityResultContracts.StartActivityForResult()){ result ->
            if (result.resultCode == Activity.RESULT_OK){
                Log.d("MyLog", "NoteActivity: received result from CategoryActivity: result_ok")
                categoryUp = result.data?.getStringExtra(Constant.Key.keyCategory) ?: "general"
            }
        }
        Log.d("MyLog", "NoteActivity: end Create ViewModel")
        Log.d("MyLog", "NoteActivity: onCreate finish")
    }
    // Создание меню на toolBar
    override fun onCreateOptionsMenu(menu: Menu?): Boolean {
        Log.d("MyLog", "NoteActivity: onCreateOptionsMenu")
        menuInflater.inflate(R.menu.note_menu, menu)

        // Ищем MenuItem по ID
        val item = menu?.findItem(R.id.noteMenuFavorite)
        //Если избранное то меняем иконку
        if (favoriteNote == true){
            item?.setIcon(R.drawable.ic_star)
        }

        return true
    }

    // Обработка нажатия в toolBar
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
                if(noteUp == null){// еСли заметки нет и нужно ее создать
                    Log.d("MyLog", "NoteActivity: save new note")
                    val note = Note(
                        title = binding.edTitle.text.toString(),
                        content = binding.edContent.text.toString(),
                        date = LocalDate.now().toString(),
                        time = LocalTime.now().toString(),
                        timeStamp = System.currentTimeMillis(),
                        category = categoryUp,// Текущее время
                        favorite = favoriteNote,
                    )

                    noteViewModel.insertNote(note)
                    noteViewModel.insertResult.observe(this) { result ->
                        when (result) {
                            is OperationResult.Success -> {
                                val newNote = note.copy(id = result.newId)
                                val intent = Intent().apply {
                                    putExtra(Constant.Key.keyNote, newNote)
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
                else{// Есди уже есть заметка и нужно ее просто отредактировать
                    Log.d("MyLog", "NoteActivity: update")
                    noteUp!!.title = binding.edTitle.text.toString()
                    noteUp!!.content = binding.edContent.text.toString()
                    noteUp!!.category = categoryUp
                    noteUp!!.favorite = favoriteNote
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
            R.id.noteMenuDeleteNote -> {// Удаляем звметку
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
            R.id.noteMenuCategory -> {// Открывем CategoryActivity
                Log.d("MyLog", "NoteActivity: click noteMenuCategory")
                val intent = Intent(this, CategoryActivity::class.java)
                noteLauncher.launch(intent)
                true
            }
            R.id.noteMenuFavorite -> {
                Log.d("MyLog", "NoteActivity: click noteMenuFavorite")
                if (favoriteNote == false){// Доболяем в избранное
                    Log.i("MyLog", "NoteActivity: click noteMenuFavorite: favoriteNote = true")
                    item.setIcon(R.drawable.ic_star)
                    favoriteNote = true
                }
                else{// Ибираем из избранного
                    Log.i("MyLog", "NoteActivity: click noteMenuFavorite: favoriteNote = false")
                    item.setIcon(R.drawable.ic_star_outline)
                    favoriteNote = false
                }
                true
            }
            else -> super.onOptionsItemSelected(item)

        }
    }

}