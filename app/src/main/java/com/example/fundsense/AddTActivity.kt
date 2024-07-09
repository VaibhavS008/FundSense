package com.example.fundsense

import android.os.Bundle
import androidx.activity.enableEdgeToEdge
import androidx.appcompat.app.AppCompatActivity
import androidx.core.widget.addTextChangedListener
import androidx.room.Room
import com.example.fundsense.databinding.ActivityAddTactivityBinding
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.GlobalScope
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext

class AddTActivity : AppCompatActivity() {

    private lateinit var binding: ActivityAddTactivityBinding

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
            }
            else if(amount == null) {
                binding.amountLayout.error = "Please enter a valid amount"
            }
            else {
                val transaction=MainTransactions(0,label,amount)
                insert(transaction)
            }
        }
        binding.goback.setOnClickListener {
            finish()
        }
    }
    private fun insert(transaction :MainTransactions){
        val  db:MainDataBase = Room.databaseBuilder(this,MainDataBase::class.java, "transactions").build()
        GlobalScope.launch {
            db.dataAccessObj().insertAll(transaction)
            finish()
        }
    }
}