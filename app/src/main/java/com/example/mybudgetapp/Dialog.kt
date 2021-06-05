import android.annotation.SuppressLint
import android.app.Activity
import android.app.Dialog
import android.os.Bundle
import android.view.View
import android.view.Window
import android.widget.Button
import android.widget.EditText
import android.widget.TextView
import com.example.mybudgetapp.*
import com.google.firebase.firestore.FirebaseFirestore

class MyDialog(
    activity: Activity?,
    private val db: FirebaseFirestore,
    private val databaseFunctions: AppDatabase,
    private val data: AppData,
    private val type: String
) : Dialog(activity!!), View.OnClickListener {

    @SuppressLint("SetTextI18n")
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        requestWindowFeature(Window.FEATURE_NO_TITLE)
        setContentView(R.layout.my_dialog)

        val newSource: EditText = findViewById(R.id.et_Source)
        val newValue: EditText = findViewById(R.id.et_Value)
        val tvType: TextView = findViewById(R.id.tv_NewType)
        tvType.text = "Add new ${type}."
        val btAddIncomeConf: Button = findViewById(R.id.bt_addIncomeConf)
        btAddIncomeConf.setOnClickListener {
            if (type == "Income") {
                addIncome(newSource, newValue, db, databaseFunctions, data)
            } else {
                addExpense(newSource, newValue, db, databaseFunctions, data)
            }
        }
    }

    override fun onClick(v: View) {
        when (v.id) {
            else -> {
            }
        }
        dismiss()
    }

    fun addIncome(
        newIncomeSource: EditText,
        newIncomeValue: EditText,
        db: Any?,
        databaseFunctions: AppDatabase,
        data: AppData,
    ) {
        val newIncomeValueDouble: Double = newIncomeValue.text.toString().toDouble()
        val newIncomeSourceString: String = newIncomeSource.text.toString()
        val newIncome: Income = (Income(newIncomeSourceString, newIncomeValueDouble))
        databaseFunctions.addIncomeDb(db as FirebaseFirestore, newIncome, data)
        dismiss()
    }

    fun addExpense(
        newExpenseSource: EditText,
        newExpenseValue: EditText,
        db: Any?,
        databaseFunctions: AppDatabase,
        data: AppData,
    ) {
        val newExpenseValueDouble: Double = newExpenseValue.text.toString().toDouble()
        val newExpenseSourceString: String = newExpenseSource.text.toString()
        val newExpense: Expense = (Expense(newExpenseSourceString, newExpenseValueDouble, ""))
        databaseFunctions.addExpenseDb(db as FirebaseFirestore, newExpense, data)
        dismiss()
    }
}