package com.example.mybudgetapp

data class AppData(
    var month: Long = 0L,
    var year: Long = 0L,
    val categories: MutableList<Category> = mutableListOf(),
    val categoriesId: MutableList<String> = mutableListOf(),
    val incomesDb: MutableList<Income> = mutableListOf(),
    val expensesDb: MutableList<Expense> = mutableListOf(),
    val incomesDbKeys: MutableList<String> = mutableListOf(),
    val expensesDbKeys: MutableList<String> = mutableListOf()
)

data class Income(
    val source: String,
    val value: Double
)

data class Expense(
    val source: String,
    val value: Double,
    val categoryName: String
)

data class Category(
    val name: String,
    val budget: Double
)
