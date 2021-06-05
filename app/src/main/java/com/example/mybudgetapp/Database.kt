package com.example.mybudgetapp

import android.widget.ArrayAdapter
import android.widget.ListView
import android.widget.TextView
import com.google.firebase.firestore.EventListener
import com.google.firebase.firestore.FirebaseFirestore
import com.google.firebase.firestore.FirebaseFirestoreException
import com.google.firebase.firestore.QuerySnapshot


open class AppDatabase(private val budgetActivity: BudgetActivity) {
    // Push an income to the database
    fun addIncomeDb(db: FirebaseFirestore, income: Income, data: AppData) {
        val incomeDb = HashMap<String, Any>()
        incomeDb["source"] = income.source
        incomeDb["value"] = income.value
        db.collection("${data.year}").document("${data.month}").collection("income").document()
            .set(incomeDb)
    }

    // Push an expense to the database
    fun addExpenseDb(db: FirebaseFirestore, expense: Expense, data: AppData) {
        val expenseDb = HashMap<String, Any>()
        expenseDb["source"] = expense.source
        expenseDb["value"] = expense.value
        expenseDb["category"] = expense.categoryName
        db.collection("${data.year}").document("${data.month}").collection("expense").document()
            .set(expenseDb)
    }

    fun addCategoryDb(db: FirebaseFirestore, data: AppData, category: Category) {
        val categoryDb = HashMap<String, Any>()
        categoryDb["name"] = category.name
        categoryDb["budget"] = category.budget
        db.collection("${data.year}").document("${data.month}").collection("categories").document()
            .set(categoryDb)
    }

    // Edit an income in the database
    fun editIncomeDb(db: FirebaseFirestore, data: AppData, index: Int, newAppData: Income) {
        val docRef = db.collection("${data.year}").document("${data.month}").collection("income")
            .document(data.incomesDbKeys[index - 1])
        docRef.update("source", newAppData.source, "value", newAppData.value)
        println("Income edited.")
    }

    // Edit an expense in the database
    fun editExpenseDb(db: FirebaseFirestore, data: AppData, index: Int, newAppData: Expense) {
        val docRef = db.collection("${data.year}").document("${data.month}").collection("expense")
            .document(data.expensesDbKeys[index - 1])
        docRef.update(
            "source",
            newAppData.source,
            "value",
            newAppData.value,
            "category",
            newAppData.categoryName
        )
        println("Expense edited.")
    }

    fun editCategoryDb(db: FirebaseFirestore, data: AppData, index: Int, category: Category) {
        val docRef =
            db.collection("${data.year}").document("${data.month}").collection("categories")
                .document(data.categoriesId[index - 1])
        docRef.update("name", category.name, "budget", category.budget)

        // This code will edit the category for the existing expenses entries
        val newCategory = data.categories[index - 1].name
        for ((idx, value) in data.expensesDb.withIndex()) {
            if (value.categoryName == newCategory) {
                val docRefExisting =
                    db.collection("${data.year}").document("${data.month}").collection("expense")
                        .document(data.expensesDbKeys[idx])
                docRefExisting.update("category", newCategory)
            }
        }

        println("Category edited.")
    }

    // Delete an income in the database
    fun deleteIncomeDb(db: FirebaseFirestore, data: AppData, index: Int) {
        db.collection("${data.year}").document("${data.month}").collection("income")
            .document(data.incomesDbKeys[index - 1]).delete()
        println("Income deleted.")
    }

    // Delete an expense in the database
    fun deleteExpenseDb(db: FirebaseFirestore, data: AppData, index: Int) {
        db.collection("${data.year}").document("${data.month}").collection("expense")
            .document(data.expensesDbKeys[index - 1]).delete()
        println("Expense deleted.")
    }

    fun deleteCategoryDb(db: FirebaseFirestore, data: AppData, index: Int) {
        // This code will delete the category for the existing expenses entries
        for ((idx, value) in data.expensesDb.withIndex()) {
            if (value.categoryName == data.categories[index - 1].name) {
                val docRef =
                    db.collection("${data.year}").document("${data.month}").collection("expense")
                        .document(data.expensesDbKeys[idx])
                docRef.update("category", "")
            }
        }
        db.collection("${data.year}").document("${data.month}").collection("categories")
            .document(data.categoriesId[index - 1]).delete()
        println("Category deleted.")
    }

//    // Calls two functions, one for the income and the other for the expense collection,
//    // that will delete all the data in those collections
//    fun deleteAll(db: FirebaseFirestore, data: AppData) {
//        if (data.expensesDb.size < 1 && data.incomesDb.size < 1 && data.categories.size < 1) {
//            println("Nothing to delete.")
//            return
//        }
//        println("THIS ACTION IS IRREVERSIBLE. DO YOU WISH TO CONTINUE? y/n")
//        var status = readLine()!!.toLowerCase()
//        while (status != "n" && status != "y") {
//            println("Invalid input. Delete all data? y/n")
//            status = readLine()!!.toLowerCase()
//        }
//        if (status == "n") {
//            return
//        }
//        deleteAllIncomes(db, data)
//        deleteAllExpenses(db, data)
//        deleteAllCategories(db, data)
//        println("All data has been deleted.")
//    }
//
//    // Delete all incomes in the database
//    fun deleteAllIncomes(db: FirebaseFirestore, data: AppData) {
//        try {
//            val incomes: ApiFuture<QuerySnapshot> =
//                db.collection("${data.year}").document("${data.month}").collection("income").get()
//            val documents = incomes.get().documents
//            for (document in documents) {
//                document.reference.delete()
//            }
//        } catch (e: Exception) {
//            println("Error deleting collection : " + e.message)
//        }
//    }
//
//    // Delete all expenses in the database
//    fun deleteAllExpenses(db: FirebaseFirestore, data: AppData) {
//        try {
//            val expenses: ApiFuture<QuerySnapshot> =
//                db.collection("${data.year}").document("${data.month}").collection("expense").get()
//            val documents = expenses.get().documents
//            for (document in documents) {
//                document.reference.delete()
//            }
//        } catch (e: Exception) {
//            println("Error deleting collection : " + e.message)
//        }
//    }
//
//    fun deleteAllCategories(db: FirebaseFirestore, data: AppData) {
//        try {
//            val expenses: ApiFuture<QuerySnapshot> =
//                db.collection("${data.year}").document("${data.month}").collection("categories").get()
//            val documents = expenses.get().documents
//            for (document in documents) {
//                document.reference.delete()
//            }
//        } catch (e: Exception) {
//            println("Error deleting collection : " + e.message)
//        }
//    }

    // Loop through the income and expense collections in the database, and store the data in local variables.
    // The 'onEvent' function will listen to any changes on the database, and automatically retrieve the changed data.
    fun retrieveAllDocuments(
        db: FirebaseFirestore?,
        data: AppData,
        lvIncome: ListView,
        lvExpense: ListView,
        textViewResult: TextView,
        adapterIncome: ArrayAdapter<Any>,
        adapterExpense: ArrayAdapter<Any>
    ) {

//        val docRef = db?.collection("${data.year}")?.document("${data.month}")?.collection("income")
//        docRef?.get()
//            ?.addOnSuccessListener { document ->
//                data.incomesDbKeys.clear()
//                data.incomesDb.clear()
//                println("is document null ${document == null}")
//                if (document != null) {
//                    println("DocumentSnapshot data: ${document.data}")
//                    Log.d(TAG, "DocumentSnapshot data: ${document.data}")
//                } else {
//                    Log.d(TAG, "No such document")
//                }
//            }
//            ?.addOnFailureListener { exception ->
//                Log.d(TAG, "get failed with ", exception)
//            }

//        db!!.collection("Hello")
//            .get()
//            .addOnSuccessListener { result ->
//                println("success listening $result")
//                for (document in result) {
//                    println("id and data test ${document.id} => ${document.data}")
//                    Log.d(TAG, "${document.id} => ${document.data}")
//                }
//            }
//            .addOnFailureListener { exception ->
//                println("failure listening")
//                Log.d(TAG, "Error getting documents: ", exception)
//            }

        db?.collection("${data.year}")?.document("${data.month}")?.collection("income")
            ?.addSnapshotListener(
                object : EventListener<QuerySnapshot?> {
                    override fun onEvent(
                        snapshots: QuerySnapshot?,
                        e: FirebaseFirestoreException?
                    ) {
                        data.incomesDbKeys.clear()
                        data.incomesDb.clear()
                        if (e != null) {
                            println("Listen failed:$e")
                            return
                        }
                        if (snapshots != null) {
                            for (doc in snapshots) {
                                doc.getString("source")?.let {
                                    doc.getDouble("value")?.let {
                                            it1 -> Income(it, it1) }
                                }?.let { data.incomesDb.add(it) }
                                data.incomesDbKeys.add(doc.id)
                            }
                            budgetActivity.setUpDataIncome(
                                data.incomesDb,
                                lvIncome,
                                textViewResult,
                                adapterIncome)
                        }
                    }
                })

        db?.collection("${data.year}")?.document("${data.month}")?.collection("expense")
            ?.addSnapshotListener(
                object : EventListener<QuerySnapshot?> {
                    override fun onEvent(
                        snapshots: QuerySnapshot?,
                        e: FirebaseFirestoreException?
                    ) {
                        data.expensesDb.clear()
                        data.expensesDbKeys.clear()
                        if (e != null) {
                            println("Listen failed:$e")
                            return
                        }
                        if (snapshots != null) {
                            for (doc in snapshots) {
                                doc.getString("source")?.let { source ->
                                    doc.getDouble("value")?.let { value ->
                                        doc.getString("category")?.let { category ->
                                            Expense(source, value, category)
                                        }
                                    }
                                }?.let { data.expensesDb.add(it) }
                                data.expensesDbKeys.add(doc.id)
                            }
                            budgetActivity.setUpDataExpense(
                                data.expensesDb,
                                lvExpense,
                                textViewResult,
                                adapterExpense)
                        }
                    }
                })

//        db?.collection("${data.year}")?.document("${data.month}")?.collection("categories")
//            ?.addSnapshotListener(
//                object : EventListener<QuerySnapshot?> {
//                    override fun onEvent(
//                        snapshots: QuerySnapshot?,
//                        e: FirebaseFirestoreException?
//                    ) {
//                        data.categories.clear()
//                        data.categoriesId.clear()
//                        if (e != null) {
//                            println("Listen failed:$e")
//                            return
//                        }
//                        if (snapshots != null) {
//                            for (doc in snapshots) {
//                                doc.getString("name")?.let { name ->
//                                    doc.getDouble("budget")?.let { budget ->
//                                        Category(name, budget)
//                                    }
//                                }?.let { data.categories.add(it) }
//                                data.categoriesId.add(doc.id)
//                                budgetActivity.setUpData(data)
//                            }
//                        }
//                    }
//                })
    }
}
