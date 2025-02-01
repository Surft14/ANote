packageCpackage com.example.anotes

import android.app.Activity
import android.content.Intent
import android.os.Bundle
import android.util.Log
import android.view.View
import android.widget.Toast
import androidx.activity.enableEdgeToEdge
import androidx.activity.result.ActivityResultLauncher
import androidx.activity.result.contract.ActivityResultContracts
import androidx.appcompat.app.AppCompatActivity
import androidx.core.view.ViewCompat
import androidx.core.view.WindowInsetsCompat
import androidx.lifecycle.ViewModelProvider
import androidx.recyclerview.widget.GridLayoutManager
import com.example.anotes.constant.Constant
import com.example.anotes.databinding.ActivitySearchBinding
import com.example.anotes.datebase.DatabaseProvider
import com.example.anotes.datebase.db_notes.Note
import com.example.anotes.model.NoteRepository
import com.example.anotes.view_model.NoteViewModel
import com.example.anotes.view_model.NoteViewModelFactory
import com.example.anotes.view_model.adapter.NoteAdapter
import com.example.anotes.view_model.click_interface.OnNoteClickListener

class SearchActivity : AppCompatActivity(), OnNoteClickListener {
    private lateinit var binding: ActivitySearchBinding
    private lateinit var noteViewModel: NoteViewModel
    private var adapter = NoteAdapter(this)
    private lateinit var searchLauncher: ActivityResultLauncher<Intent>
    override fun onCreate(savedInstanceState: Bundle?) {
        Log.d("MyLog", "onCreate SearchActivity start")
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        binding = ActivitySearchBinding.inflate(layoutInflater)
        setContentView(binding.root)
        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.main)) { v, insets ->
            val systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars())
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom)
            insets
        }
        // Улавливаем inite
        searchLauncher = registerForActivityResult(ActivityResultContracts.StartActivityForResult()){ result ->
            if (result.resultCode == Activity.RESULT_OK){
                Toast.makeText(this, "Result OK", Toast.LENGTH_LONG).show()
            }

        }


        Log.d("MyLog", "SearchActivity: Start Create ViewModel")
        // Создание репозитория
        val repository = NoteRepository(
            DatabaseProvider.getDatabase().noteDao(),
            DatabaseProvider.getDatabase().categoryDao(),
        )
        //Получаем ViewModel
        // Использование ViewModelFactory
        val factory = NoteViewModelFactory(repository)
        noteViewModel = ViewModelProvider(this, factory).get(NoteViewModel::class.java)
        Log.d("MyLog", "SearchActivity: end Create ViewModel")
        init()
        Log.d("MyLog", "onCreate SearchActivity end")
    }
    // Нажатие на кнопку назад
    fun onClickHome(view: View){//Кнопка закрывающая нынешние активити
        Log.d("MyLog", "onClickHome SearchActivity")
        finish()
    }
    // Нажате на поиск
    fun onClickSearch(view: View){
        Log.d("MyLog", "Call function in SearchActivity: onClickSearch")
        noteViewModel.searchNotes(binding.edSearch.text.toString()).observe(this){ notes ->// Ищим заметки если найдем то добовляем их в rvList
            if (notes == null){
                Log.i("MyLog", "onClickSearch: notes is null")
                Toast.makeText(this, "No result searched!!", Toast.LENGTH_LONG).show()
            }
            else{
                Log.i("MyLog", "onClickSearch: notes is not null")
                adapter.updateNotes(notes)
            }
        }
    }
    // инициализация адаптера и rvListNote
    fun init(){
        Log.d("MyLog", "Call function in SearchActivity: init")
        binding.rvListNotes.adapter = adapter
        binding.rvListNotes.layoutManager = GridLayoutManager(this, 1)
    }
    // Слушатель для быстрого нажатия
    override fun onNoteClick(note: Note) {
        Log.d("MyLog", "SearchActivity: Call onNoteClick in SearchActivity")
        Toast.makeText(this, "Clicked: ${note.id}, ${note.title}", Toast.LENGTH_SHORT).show()
        val intent = Intent(this, NoteActivity::class.java)
        intent.putExtra(Constant.Key.keyNote, note)
        searchLauncher.launch(intent)
    }
    // Слушателб для долгого нажатия
    override fun onNoteLongClick(note: Note): Boolean {
        Toast.makeText(this, "Long Clicked: ${note.id}, ${note.title}", Toast.LENGTH_SHORT).show()
        return true
    }



}