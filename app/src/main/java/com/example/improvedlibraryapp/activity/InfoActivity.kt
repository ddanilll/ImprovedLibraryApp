package com.example.improvedlibraryapp.activity

import android.app.Activity
import android.content.Context
import android.content.Intent
import android.os.Bundle
import android.view.View
import android.widget.Button
import android.widget.EditText
import android.widget.ImageView
import com.example.improvedlibraryapp.R

class InfoActivity : Activity() {

    private lateinit var nameEditText: EditText
    private lateinit var infoEditText: EditText
    private lateinit var saveButton: Button
    private lateinit var iconImageView: ImageView

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.item_info)

        nameEditText = findViewById(R.id.name)
        infoEditText = findViewById(R.id.information)
        saveButton = findViewById(R.id.saveButton)
        iconImageView = findViewById(R.id.icon)

        if (intent.hasExtra(SUBJECT_NAME)) {
            setupViewMode()
        } else {
            setupCreateMode()
        }
    }

    private fun setupViewMode() {
        nameEditText.isEnabled = false
        infoEditText.isEnabled = false
        saveButton.visibility = View.GONE

        val type = intent.getStringExtra(SUBJECT_TYPE) ?: return
        val name = intent.getStringExtra(SUBJECT_NAME) ?: return
        val info = intent.getStringExtra(SUBJECT_INFO) ?: return
        nameEditText.setText(name)
        infoEditText.setText(info)

        when (type) {
            "BOOK" -> iconImageView.setImageResource(R.drawable.ic_bookimage)
            "NEWSPAPER" -> iconImageView.setImageResource(R.drawable.ic_newspaperimage)
            "DISK" -> iconImageView.setImageResource(R.drawable.ic_diskimage)
        }
    }

    private fun setupCreateMode() {
        nameEditText.isEnabled = true
        infoEditText.isEnabled = true
        saveButton.visibility = View.VISIBLE

        val type = intent.getStringExtra(SUBJECT_TYPE)

        when (type) {
            "BOOK" -> {
                iconImageView.setImageResource(R.drawable.ic_bookimage)
                nameEditText.hint = "Название книги"
                infoEditText.hint = "Информация о книге: "
            }

            "NEWSPAPER" -> {
                iconImageView.setImageResource(R.drawable.ic_newspaperimage)
                nameEditText.hint = "Название газеты"
                infoEditText.hint = "Информация о газете: "
            }

            "DISK" -> {
                iconImageView.setImageResource(R.drawable.ic_diskimage)
                nameEditText.hint = "Название диска"
                infoEditText.hint = "Информация о диске "
            }
        }

        saveButton.setOnClickListener {
            val name = nameEditText.text.toString().trim()
            val info = infoEditText.text.toString().trim()

            val resultIntent = Intent().apply {
                putExtra(SUBJECT_NAME, name)
                putExtra(SUBJECT_TYPE, type)
                putExtra(SUBJECT_INFO, info)
            }

            setResult(RESULT_OK, resultIntent)
            finish()
        }
    }

    companion object {
        const val SUBJECT_NAME = "subjectName"
        const val SUBJECT_TYPE = "subjectType"
        const val SUBJECT_INFO = "subjectInfo"

        fun newCreateIntent(context: Context, type: String): Intent {
            return Intent(context, InfoActivity::class.java).apply {
                putExtra(SUBJECT_TYPE, type)
            }
        }
    }
}
