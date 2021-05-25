package com.example.mybudgetapp

import android.content.Intent
import android.os.Bundle
import android.view.View
import android.widget.Button
import android.widget.DatePicker
import android.widget.LinearLayout
import androidx.appcompat.app.AppCompatActivity
import java.util.*


class MainActivity : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        val datePicker = findViewById<DatePicker>(R.id.dp_date)
        val today = Calendar.getInstance()
        var period = "${today.get(Calendar.MONTH) + 1}-${today.get(Calendar.YEAR)}"
        datePicker.init(today.get(Calendar.YEAR), today.get(Calendar.MONTH),
            today.get(Calendar.DAY_OF_MONTH)

        ) { _, year, month, _ ->
            val month = month + 1
            period = "$month-$year"
        }

        val ll = datePicker.getChildAt(0) as LinearLayout
        val ll2 = ll.getChildAt(0) as LinearLayout
        ll2.getChildAt(1).visibility = View.GONE

        val btStart: Button = findViewById(R.id.bt_start)
        btStart.setOnClickListener { startApp(period) }
    }

    private fun startApp(period: String) {
        val intent = Intent(this, BudgetActivity::class.java)
        intent.putExtra("name", period)
        startActivity(intent)
    }
}