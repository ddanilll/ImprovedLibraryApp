package com.example.improvedlibraryapp.activity.recycler

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel

class MainViewModel : ViewModel() {
    private val _items = MutableLiveData<List<Item>>()
    val items: LiveData<List<Item>> = _items

    init {
        _items.value = getInitialItems()
    }

    fun updateItems(newItems: List<Item>) {
        _items.value = newItems
    }

    fun removeItem(position: Int) {
        val currentList = _items.value?.toMutableList() ?: return
        currentList.removeAt(position)
        _items.value = currentList
    }

    private fun getInitialItems(): List<Item> {
        return mutableListOf(
            Item.Header("Books"),
            Item.Book("Маугли", 1, "Автор: Редьярд Киплинг, 250 страниц"),
            Item.Book("Бесы", 2, "Автор: Фёдор Достоевский, 760 страниц"),
            Item.Book("Три товарища", 3, "Автор: Эрих Мария Ремарк, 340 страниц"),

            Item.Header("Newspapers"),
            Item.Newspaper("Правда", 4, "Месяц выпуска: Май, 12 номер выпуска"),
            Item.Newspaper("Тайны вселенной", 5, "Месяц выпуска: Январь, 3 номер выпуска"),
            Item.Newspaper("Новости", 6, "Месяц выпуска: Июль, 20 номер выпуска"),

            Item.Header("Disks"),
            Item.Disk("Веном", 7, "Тип диска: DVD"),
            Item.Disk("Форсаж", 8, "Тип диска: CD"),
            Item.Disk("Марвел", 9, "Тип диска: CD")
        )
    }
}