package com.example.fundsense

import android.os.Bundle
import androidx.activity.enableEdgeToEdge
import androidx.appcompat.app.AppCompatActivity
import androidx.core.view.ViewCompat
import androidx.core.view.WindowInsetsCompat
import androidx.core.widget.addTextChangedListener
import com.example.fundsense.databinding.ActivityAddTactivityBinding
import kotlinx.coroutines.GlobalScope
import kotlinx.coroutines.launch

class AddTActivity : AppCompatActivity() {

    private lateinit var binding: ActivityAddTactivityBinding

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        binding = ActivityAddTactivityBinding.inflate(layoutInflater)
        setContentView(binding.root)



        binding.labelInput.addTextChangedListener {
            if (it!!.count() > 0) {
                binding.labelLayout.error = null
            }
        }

        binding.amountInput.addTextChangedListener {
            if (it!!.count() > 0) {
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
        }
        binding.goback.setOnClickListener {
            finish()
        }
    }
}
