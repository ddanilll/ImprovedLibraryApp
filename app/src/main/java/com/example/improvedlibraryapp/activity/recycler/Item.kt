package com.example.improvedlibraryapp.activity.recycler


sealed class Item {

    abstract fun isSameItem(other: Item): Boolean

    data class Header(val title: String) : Item() {
        override fun isSameItem(other: Item): Boolean = other is Header && title == other.title
    }

    data class Book(
        val name: String, val id: Int, val info: String
    ) : Item() {
        override fun isSameItem(other: Item): Boolean = other is Book && id == other.id
    }

    data class Newspaper(
        val name: String, val id: Int, val info: String
    ) : Item() {
        override fun isSameItem(other: Item): Boolean = other is Newspaper && id == other.id
    }

    data class Disk(
        val name: String, val id: Int, val info: String
    ) : Item() {
        override fun isSameItem(other: Item): Boolean = other is Disk && id == other.id
    }
}