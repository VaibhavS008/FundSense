package com.example.fundsense

import android.app.DatePickerDialog
import android.icu.text.SimpleDateFormat
import android.icu.util.Calendar
import android.os.Bundle
import android.util.Log
import android.view.View
import androidx.activity.enableEdgeToEdge
import androidx.appcompat.app.AppCompatActivity
import androidx.core.widget.addTextChangedListener
import androidx.room.Room
import com.example.fundsense.databinding.ActivityAddTactivityBinding
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.GlobalScope
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext
import java.util.Locale

class AddTActivity : AppCompatActivity() {

    private lateinit var binding: ActivityAddTactivityBinding
    private var selectedDate: Calendar = Calendar.getInstance()
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        binding = ActivityAddTactivityBinding.inflate(layoutInflater)
        setContentView(binding.root)

        binding.labelInput.addTextChangedListener {
            if (it!!.isNotEmpty()) {
                binding.labelLayout.error = null
            }
        }

        binding.amountInput.addTextChangedListener {
            if (it!!.isNotEmpty()) {
                binding.amountLayout.error = null
            }
        }

        binding.addTransactionBtn.setOnClickListener {
            val label = binding.labelInput.text.toString()
            val amount = binding.amountInput.text.toString().toDoubleOrNull()

            if (label.isEmpty()) {
                binding.labelLayout.error = "Please enter a valid label"
            } else if (amount == null) {
                binding.amountLayout.error = "Please enter a valid amount"
            } else {
                val finalAmount = if (binding.expenseCheckbox.isChecked) {
                    //Log.d("AddTActivity", "Expense Checkbox is checked. Amount: -$amount")
                    -amount
                } else {
                    //Log.d("AddTActivity", "Expense Checkbox is not checked. Amount: -$amount")
                    amount
                }
                val transaction = MainTransactions(0, label, amount, selectedDate.timeInMillis)
                insert(transaction)
            }
        }
        binding.goback.setOnClickListener {
            finish()
        }
    }
    fun showDatePickerDialog(view: View) {
        val datePickerDialog = DatePickerDialog(
            this,
            DatePickerDialog.OnDateSetListener { view, year, monthOfYear, dayOfMonth ->
                selectedDate.set(year, monthOfYear, dayOfMonth)
                updateDateInput()
            },
            selectedDate.get(Calendar.YEAR),
            selectedDate.get(Calendar.MONTH),
            selectedDate.get(Calendar.DAY_OF_MONTH)
        )
        datePickerDialog.show()
    }

    private fun updateDateInput() {
        val dateFormat = SimpleDateFormat("dd/MM/yyyy", Locale.getDefault())
        binding.dateInput.setText(dateFormat.format(selectedDate.time))
    }

    private fun insert(transaction: MainTransactions) {
        val db: MainDataBase =
            Room.databaseBuilder(this, MainDataBase::class.java, "transactions").build()
        GlobalScope.launch {
            val finalTransaction = if (transaction.data > 0 && binding.expenseCheckbox.isChecked) {
                transaction.copy(data = -transaction.data)
            } else {
                transaction
            }
            db.dataAccessObj().insertAll(finalTransaction)
            //Log.d("AddTActivity", "Inserted transaction: $finalTransaction")
            runOnUiThread {
                finish()
            }
        }
    }
}
