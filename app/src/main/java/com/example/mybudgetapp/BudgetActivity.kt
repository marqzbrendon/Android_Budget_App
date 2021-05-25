package com.example.mybudgetapp

import android.annotation.SuppressLint
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.widget.TextView

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
    }
}