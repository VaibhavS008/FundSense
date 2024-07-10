package com.example.fundsense

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Button
import android.widget.ImageButton
import android.widget.TextView
import androidx.core.content.ContextCompat
import androidx.recyclerview.widget.RecyclerView

class TransactionAdapter(private var transactions: List<MainTransactions>, private val onDeleteClick: (MainTransactions) -> Unit) :
    RecyclerView.Adapter<TransactionAdapter.TransactionHolder>() {

    class TransactionHolder(view: View) : RecyclerView.ViewHolder(view) {
        val label: TextView = view.findViewById(R.id.label)
        val data: TextView = view.findViewById(R.id.data)
        val deleteBtn: ImageButton = view.findViewById(R.id.deleteBtn)
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): TransactionHolder {
        val view = LayoutInflater.from(parent.context).inflate(R.layout.layout_for_transactions, parent, false)
        return TransactionHolder(view)
    }

    override fun onBindViewHolder(holder: TransactionHolder, position: Int) {
        val transaction = transactions[position]
        val context = holder.data.context

        if (transaction.data >= 0) {
            holder.data.text = "+ ₹%.2f".format(transaction.data)
            holder.data.setTextColor(ContextCompat.getColor(context, R.color.green))
        } else {
            holder.data.text = "- ₹%.2f".format(Math.abs(transaction.data))
            holder.data.setTextColor(ContextCompat.getColor(context, R.color.red))
        }

        holder.label.text = transaction.label
        holder.deleteBtn.setOnClickListener {
            onDeleteClick(transaction)
        }
    }

    override fun getItemCount(): Int {
        return transactions.size
    }

    fun settingdata(transactions: List<MainTransactions>) {
        this.transactions = transactions
        notifyDataSetChanged()
    }
}
