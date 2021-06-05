package com.example.mybudgetapp

import MyDialog
import android.annotation.SuppressLint
import android.os.Bundle
import android.widget.*
import androidx.appcompat.app.AppCompatActivity
import com.google.firebase.firestore.FirebaseFirestore
import com.google.firebase.firestore.ktx.firestore
import com.google.firebase.ktx.Firebase


class BudgetActivity : AppCompatActivity() {
    @SuppressLint("SetTextI18n")
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_budget)

        val period = intent.getStringExtra("name")!!.split("-")
        val month = period[0].toLong()
        val year = period[1].toLong()

        val tvMessage: TextView = findViewById(R.id.tv_period)
        tvMessage.text = "Period: $month/$year"

        val db = Firebase.firestore
        val data = AppData()
        data.month = month
        data.year = year

        val databaseFunctions = AppDatabase(BudgetActivity())

        val lvIncome: ListView = findViewById(R.id.lv_income)
        val lvExpense: ListView = findViewById(R.id.lv_expense)

        val textViewResult: TextView = findViewById(R.id.tv_result)

        val adapterIncome: ArrayAdapter<Any> = ArrayAdapter(
            this,
            android.R.layout.simple_dropdown_item_1line, arrayListOf("Getting data...") as List<Any>
        )
        adapterIncome.notifyDataSetChanged()
        val adapterExpense: ArrayAdapter<Any> = ArrayAdapter(
            this,
            android.R.layout.simple_dropdown_item_1line, arrayListOf("Getting data...") as List<Any>
        )

        adapterExpense.notifyDataSetChanged()

        lvIncome.adapter = adapterIncome
        lvExpense.adapter = adapterExpense

        databaseFunctions.retrieveAllDocuments(
            db,
            data,
            lvIncome,
            lvExpense,
            textViewResult,
            adapterIncome,
            adapterExpense
        )

        val btAddIncome: Button = findViewById(R.id.bt_addIncome)
        val btAddExpense: Button = findViewById(R.id.bt_addExpense)
        btAddIncome.setOnClickListener { openMyDialog(db, databaseFunctions, data, "Income") }
        btAddExpense.setOnClickListener { openMyDialog(db, databaseFunctions, data, "Expense") }
    }

    @SuppressLint("SetTextI18n")
    fun setUpDataIncome(
        incomes: MutableList<Income>,
        lvIncome: ListView,
        textViewResult: TextView,
        adapterIncome: ArrayAdapter<Any>
    ) {
        if (incomes.isEmpty()) {
            adapterIncome.notifyDataSetChanged()
            adapterIncome.clear()
            adapterIncome.add("No incomes")
            lvIncome.adapter = adapterIncome
        } else {
            adapterIncome.clear()
            incomes.map { x ->
                adapterIncome.add("${x.source} - ${x.value}")
                adapterIncome.notifyDataSetChanged()
            }
            lvIncome.adapter = adapterIncome
        }

        lvIncome.onItemClickListener =
            AdapterView.OnItemClickListener { parent, view, position, id ->
                val selectedItem = parent.getItemAtPosition(position)
                textViewResult.text = "Selected : $selectedItem"
            }
    }

    @SuppressLint("SetTextI18n")
    fun setUpDataExpense(
        expenses: MutableList<Expense>,
        lvExpense: ListView,
        textViewResult: TextView,
        adapterExpense: ArrayAdapter<Any>
    ) {
        if (expenses.isEmpty()) {
            adapterExpense.notifyDataSetChanged()
            adapterExpense.clear()
            adapterExpense.add("No expenses")
            lvExpense.adapter = adapterExpense
        } else {
            adapterExpense.clear()
            expenses.map { x ->
                adapterExpense.add("${x.source} - ${x.value}")
                adapterExpense.notifyDataSetChanged()
            }
            lvExpense.adapter = adapterExpense
        }

        lvExpense.onItemClickListener =
            AdapterView.OnItemClickListener { parent, view, position, id ->
                val selectedItem = parent.getItemAtPosition(position)
                textViewResult.text = "Selected : $selectedItem"
            }
    }

    private fun openMyDialog(
        db: FirebaseFirestore,
        databaseFunctions: AppDatabase,
        data: AppData,
        type: String
    ) {
        val dialog = MyDialog(this, db, databaseFunctions, data, type)
        dialog.show()
    }

}
