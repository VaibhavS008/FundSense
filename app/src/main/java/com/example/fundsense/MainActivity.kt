package com.example.fundsense

import android.app.NotificationChannel
import android.app.NotificationManager
import android.app.PendingIntent
import android.content.Context
import android.content.Intent
import android.os.Build
import android.os.Bundle
import android.util.Log
import androidx.activity.enableEdgeToEdge
import androidx.appcompat.app.AppCompatActivity
import androidx.core.app.NotificationCompat
import androidx.core.app.NotificationManagerCompat
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.room.Room
import com.example.fundsense.databinding.ActivityMainBinding
import kotlinx.coroutines.GlobalScope
import kotlinx.coroutines.launch

class MainActivity : AppCompatActivity() {

    private lateinit var transactions: List<MainTransactions>
    private lateinit var transactionAdapter: TransactionAdapter
    private lateinit var layoutManager: LinearLayoutManager
    private lateinit var binding: ActivityMainBinding
    private lateinit var db: MainDataBase

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        binding = ActivityMainBinding.inflate(layoutInflater)
        setContentView(binding.root)

        transactions = emptyList()

        layoutManager = LinearLayoutManager(this)
        transactionAdapter = TransactionAdapter(transactions) { transaction ->
            deleteTransaction(transaction)
        }

        binding.recyclerview.apply {
            layoutManager = this@MainActivity.layoutManager
            adapter = transactionAdapter
        }

        db = Room.databaseBuilder(
            applicationContext,
            MainDataBase::class.java, "transactions"
        ).build()

        createNotificationChannel()

        binding.addBtn.setOnClickListener {
            val intent = Intent(this, AddTActivity::class.java)
            startActivity(intent)
        }
    }

    override fun onResume() {
        super.onResume()
        fetchTransactions()
    }

    private fun fetchTransactions() {
        GlobalScope.launch {
            transactions = db.dataAccessObj().getAll()
            runOnUiThread {
                updateUI(transactions)
            }
        }
    }

    private fun updateUI(transactions: List<MainTransactions>) {
        transactionAdapter.settingdata(transactions)

        val total = transactions.sumOf { it.data }
        val budgetFinal = transactions.filter { it.data > 0 }.sumOf { it.data }
        val expenseFinal = transactions.filter { it.data < 0 }.sumOf { it.data }

        binding.bal.text = "₹ %.2f".format(total)
        binding.budget.text = "₹ %.2f".format(budgetFinal)
        binding.expense.text = "₹ %.2f".format(expenseFinal)

        if (total < 0) {
            sendBalanceAlertNotification(total)
        }
    }

    private fun deleteTransaction(transaction: MainTransactions) {
        GlobalScope.launch {
            db.dataAccessObj().delete(transaction)
            fetchTransactions()
        }
    }

    private fun sendBalanceAlertNotification(balance: Double) {
        val intent = Intent(this, MainActivity::class.java).apply {
            flags = Intent.FLAG_ACTIVITY_NEW_TASK or Intent.FLAG_ACTIVITY_CLEAR_TASK
        }
        val pendingIntent: PendingIntent = PendingIntent.getActivity(this, 0, intent, 0)

        val builder = NotificationCompat.Builder(this, CHANNEL_ID)
            .setSmallIcon(R.drawable.baseline_notifications_active_24) // Replace with your notification icon
            .setContentTitle("Balance Alert")
            .setContentText("Your balance is ₹ %.2f".format(balance))
            .setPriority(NotificationCompat.PRIORITY_DEFAULT)
            .setContentIntent(pendingIntent)
            .setAutoCancel(true)

        with(NotificationManagerCompat.from(this)) {
            notify(0, builder.build())
        }
    }

    private fun createNotificationChannel() {
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
            val name = "Balance Alert Channel"
            val descriptionText = "Notifications for balance alerts"
            val importance = NotificationManager.IMPORTANCE_DEFAULT
            val channel = NotificationChannel(CHANNEL_ID, name, importance).apply {
                description = descriptionText
            }
            val notificationManager: NotificationManager =
                getSystemService(Context.NOTIFICATION_SERVICE) as NotificationManager
            notificationManager.createNotificationChannel(channel)
        }
    }

    companion object {
        const val CHANNEL_ID = "balance_alert_channel"
    }
}
