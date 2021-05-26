package com.example.mybudgetapp

import android.annotation.SuppressLint
import android.os.Bundle
import android.widget.AdapterView
import android.widget.ArrayAdapter
import android.widget.ListView
import android.widget.TextView
import androidx.appcompat.app.AppCompatActivity
import com.google.firebase.firestore.FirebaseFirestore


class BudgetActivity : AppCompatActivity() {
    @SuppressLint("SetTextI18n")
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_budget)

        val period = intent.getStringExtra("name")!!.split("-")
        val month = period[0].toLong()
        val year = period[1].toLong()

        val tvMessage : TextView = findViewById(R.id.tv_period)
        tvMessage.text = "Period: $month/$year"

        val db = FirebaseFirestore.getInstance()
        val data = AppData()
        data.month = month
        data.year = year
        val databaseFunctions = AppDatabase()

        databaseFunctions.retrieveAllDocuments(db, data)
        println("incomedata ${data.incomesDb} month/year ${data.month} / ${data.year}")

        val lvIncome: ListView = findViewById(R.id.lv_income)
        val textViewResult: TextView = findViewById(R.id.tv_result)

        val adapter: ArrayAdapter<Income> = ArrayAdapter(
            this,
            android.R.layout.simple_dropdown_item_1line,data.incomesDb
        )
        lvIncome.adapter = adapter

        lvIncome.onItemClickListener =
            AdapterView.OnItemClickListener { parent, view, position, id ->
                val selectedItem = parent.getItemAtPosition(position)
                textViewResult.text = "Selected : $selectedItem"
            }
    }
}

