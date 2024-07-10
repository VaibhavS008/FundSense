package com.example.fundsense

import android.content.Intent
import android.os.Bundle
import androidx.activity.enableEdgeToEdge
import androidx.appcompat.app.AppCompatActivity
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.room.Room
import com.example.fundsense.databinding.ActivityMainBinding
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.GlobalScope
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext

class MainActivity : AppCompatActivity() {

    private lateinit var transactions: List<MainTransactions>
    private lateinit var transactionAdapter: TransactionAdapter
    private lateinit var layoutManager: LinearLayoutManager
    private lateinit var binding: ActivityMainBinding
    private lateinit var db:  MainDataBase

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        binding = ActivityMainBinding.inflate(layoutInflater)
        setContentView(binding.root)

        transactions = arrayListOf()

        transactionAdapter = TransactionAdapter(transactions)
        layoutManager = LinearLayoutManager(this)


        binding.recyclerview.apply {
            adapter = transactionAdapter
            layoutManager = this@MainActivity.layoutManager
        }
        db = Room.databaseBuilder(this,
            MainDataBase::class.java,
            "transactions").build()

        binding.addBtn.setOnClickListener{
            val intent= Intent(this,AddTActivity::class.java)
            startActivity(intent)
        }
    }
    private fun fetching(){
        GlobalScope.launch {
            //db.dataAccessObj().insertAll(MainTransactions(0,"abc",-200.0))
            transactions=db.dataAccessObj().getAll()
            runOnUiThread{
                dataupdate()
                transactionAdapter.settingdata(transactions)
            }
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

    override fun onResume() {
        super.onResume()
        fetching()
    }
}
