package com.example.fundsense

import android.content.Intent
import android.os.Bundle
import androidx.activity.enableEdgeToEdge
import androidx.appcompat.app.AppCompatActivity
import androidx.recyclerview.widget.LinearLayoutManager
import com.example.fundsense.databinding.ActivityMainBinding

class MainActivity : AppCompatActivity() {

    private lateinit var transactions: ArrayList<MainTransactions>
    private lateinit var transactionAdapter: TransactionAdapter
    private lateinit var layoutManager: LinearLayoutManager
    private lateinit var binding: ActivityMainBinding

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        binding = ActivityMainBinding.inflate(layoutInflater)
        setContentView(binding.root)

        // Initialize your data
        transactions = arrayListOf(
            MainTransactions("food", 200.00),
            MainTransactions("movie", -20.00),
            MainTransactions("travel", 100.00)
        )

        // Initialize the adapter and layout manager
        transactionAdapter = TransactionAdapter(transactions)
        layoutManager = LinearLayoutManager(this)

        // Set up the RecyclerView
        binding.recyclerview.apply {
            adapter = transactionAdapter
            layoutManager = this@MainActivity.layoutManager
        }
        dataupdate()
        binding.addBtn.setOnClickListener{
            val intent= Intent(this,AddTActivity::class.java)
            startActivity(intent)
        }
    }
    private fun dataupdate(){
        val total = transactions.sumOf { it.data }
        val budgetfinal = transactions.filter { it.data > 0 }.sumOf { it.data }
        val expensefinal = transactions.filter { it.data < 0 }.sumOf { it.data }

        // Update TextViews with calculated values
        binding.bal.text = "₹ %.2f".format(total)
        binding.budget.text = "₹ %.2f".format(budgetfinal)
        binding.expense.text = "₹ %.2f".format(expensefinal)
    }
}
