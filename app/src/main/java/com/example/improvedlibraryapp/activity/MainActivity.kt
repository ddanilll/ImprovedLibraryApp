package com.example.improvedlibraryapp.activity

import android.content.Intent
import android.os.Bundle
import android.widget.Button
import androidx.activity.result.contract.ActivityResultContracts
import androidx.appcompat.app.AlertDialog
import androidx.appcompat.app.AppCompatActivity
import androidx.lifecycle.ViewModelProvider
import androidx.recyclerview.widget.ItemTouchHelper
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.example.improvedlibraryapp.R
import com.example.improvedlibraryapp.activity.InfoActivity.Companion.SUBJECT_INFO
import com.example.improvedlibraryapp.activity.InfoActivity.Companion.SUBJECT_NAME
import com.example.improvedlibraryapp.activity.InfoActivity.Companion.SUBJECT_TYPE
import com.example.improvedlibraryapp.activity.recycler.Item
import com.example.improvedlibraryapp.activity.recycler.MainViewModel
import com.example.improvedlibraryapp.activity.recycler.ViewModelFactory
import com.example.improvedlibraryapp.activity.recycler.adapters.LibraryAdapter
import com.example.improvedlibraryapp.activity.recycler.adapters.vh.HeaderViewHolder
import com.example.improvedlibraryapp.databinding.ActivityMainBinding

class MainActivity : AppCompatActivity(), LibraryAdapter.OnItemClickListener {
    private val binding by lazy {
        ActivityMainBinding.inflate(layoutInflater)
    }

    private lateinit var adapter: LibraryAdapter
    private lateinit var createButton: Button
    private lateinit var viewModel: MainViewModel

    private val startForResult = registerForActivityResult(
        ActivityResultContracts.StartActivityForResult()
    ) { result ->
        if (result.resultCode == RESULT_OK) {
            val data = result.data
            val type = data?.getStringExtra(SUBJECT_TYPE)
            val name = data?.getStringExtra(SUBJECT_NAME)
            val info = data?.getStringExtra(SUBJECT_INFO)

            when (type) {
                "BOOK" -> {
                    if (name != null && info != null) {
                        val newBook = Item.Book(
                            name = name, id = generateNewId(), info = info
                        )
                        addItemToCorrectSection(newBook)
                    }
                }

                "NEWSPAPER" -> {
                    if (name != null && info != null) {
                        val newNewspaper = Item.Newspaper(
                            name = name, id = generateNewId(), info = info
                        )
                        addItemToCorrectSection(newNewspaper)
                    }
                }

                "DISK" -> {
                    if (name != null && info != null) {
                        val newDisk = Item.Disk(
                            name = name, id = generateNewId(), info = info
                        )
                        addItemToCorrectSection(newDisk)
                    }
                }
            }
        }
    }

    private fun generateNewId(): Int {
        return adapter.items.filter { it !is Item.Header }.maxOfOrNull {
                when (it) {
                    is Item.Book -> it.id
                    is Item.Newspaper -> it.id
                    is Item.Disk -> it.id
                    else -> 0
                }
            }?.plus(1) ?: 1
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(binding.root)

        initViewModel()

        adapter = LibraryAdapter(mutableListOf(), this)

        with(binding.recyclerView) {
            layoutManager = LinearLayoutManager(context)
            adapter = this@MainActivity.adapter
            setHasFixedSize(true)
        }
        setupItemTouchHelper()

        createButton = findViewById(R.id.createButton)
        createButton.setOnClickListener {
            val items = listOf("BOOK", "NEWSPAPER", "DISK")

            AlertDialog.Builder(this).setTitle("Выберите тип элемента")
                .setItems(items.toTypedArray()) { _, which ->
                    val intent = InfoActivity.newCreateIntent(this, items[which])
                    startForResult.launch(intent)
                }.show()
        }
    }

    private fun initViewModel() {
        val factory = ViewModelFactory()
        viewModel = ViewModelProvider(this, factory)[MainViewModel::class.java]

        viewModel.items.observe(this) { items ->
            adapter.updateList(items)
        }
    }

    private fun addItemToCorrectSection(newItem: Item) {
        val currentList = viewModel.items.value?.toMutableList() ?: return
        val sectionHeader = when (newItem) {
            is Item.Book -> "Books"
            is Item.Newspaper -> "Newspapers"
            is Item.Disk -> "Disks"
            else -> return
        }

        val sectionHeaderIndex = currentList.indexOfFirst {
            it is Item.Header && it.title == sectionHeader
        }

        if (sectionHeaderIndex != -1) {
            val sectionEndIndex = currentList.subList(sectionHeaderIndex + 1, currentList.size)
                .indexOfFirst { it is Item.Header } + sectionHeaderIndex + 1

            val insertPosition = if (sectionEndIndex > sectionHeaderIndex) {
                sectionEndIndex
            } else {
                sectionHeaderIndex + 1
            }
            currentList.add(insertPosition, newItem)
            viewModel.updateItems(currentList)
        } else {
            currentList.add(newItem)
            viewModel.updateItems(currentList)
        }
    }

    private fun setupItemTouchHelper() {
        val itemTouchHelperCallback = object : ItemTouchHelper.SimpleCallback(
            0, ItemTouchHelper.LEFT or ItemTouchHelper.RIGHT
        ) {
            override fun onMove(
                recyclerView: RecyclerView,
                viewHolder: RecyclerView.ViewHolder,
                target: RecyclerView.ViewHolder
            ): Boolean = false


            override fun onSwiped(viewHolder: RecyclerView.ViewHolder, direction: Int) {
                val position = viewHolder.bindingAdapterPosition
                if (position != RecyclerView.NO_POSITION) {
                    viewModel.removeItem(position)
                }
            }

            override fun getSwipeDirs(
                recyclerView: RecyclerView, viewHolder: RecyclerView.ViewHolder
            ): Int {
                return if (viewHolder is HeaderViewHolder) {
                    0
                } else {
                    super.getSwipeDirs(recyclerView, viewHolder)
                }
            }
        }
        ItemTouchHelper(itemTouchHelperCallback).attachToRecyclerView(binding.recyclerView)
    }

    override fun onItemClick(position: Int, item: Item) {
        val intent = Intent(this, InfoActivity::class.java).apply {
            putExtra(
                SUBJECT_NAME, when (item) {
                    is Item.Book -> item.name
                    is Item.Newspaper -> item.name
                    is Item.Disk -> item.name
                    is Item.Header -> return
                }
            )
            putExtra(
                SUBJECT_TYPE, when (item) {
                    is Item.Book -> "BOOK"
                    is Item.Newspaper -> "NEWSPAPER"
                    is Item.Disk -> "DISK"
                    is Item.Header -> return
                }
            )
            putExtra(
                SUBJECT_INFO, when (item) {
                    is Item.Book -> item.info
                    is Item.Newspaper -> item.info
                    is Item.Disk -> item.info
                    is Item.Header -> return
                }
            )
        }
        startActivity(intent)
    }
}


