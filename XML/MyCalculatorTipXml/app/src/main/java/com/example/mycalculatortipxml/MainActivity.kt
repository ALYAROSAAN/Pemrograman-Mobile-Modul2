package com.example.mycalculatortipxml

import android.os.Bundle
import android.widget.*
import androidx.appcompat.app.AppCompatActivity
import kotlin.math.ceil

class MainActivity : AppCompatActivity() {

    private lateinit var amountInput: EditText
    private lateinit var tipSpinner: Spinner
    private lateinit var roundUpSwitch: Switch
    private lateinit var resultText: TextView

    private val tipOptions = listOf(0.15, 0.18, 0.20)

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        amountInput = findViewById(R.id.billAmountInput)
        tipSpinner = findViewById(R.id.tipSpinner)
        roundUpSwitch = findViewById(R.id.roundUpSwitch)
        resultText = findViewById(R.id.resultText)

        val tipStrings = tipOptions.map { "${(it * 100).toInt()}%" }
        val adapter = ArrayAdapter(this, android.R.layout.simple_spinner_dropdown_item, tipStrings)
        tipSpinner.adapter = adapter

        val calculate = {
            val amount = amountInput.text.toString().toDoubleOrNull() ?: 0.0
            var tip = amount * tipOptions[tipSpinner.selectedItemPosition]
            if (roundUpSwitch.isChecked) tip = ceil(tip)
            resultText.text = "Tip Amount: $${String.format("%.2f", tip)}"
        }

        amountInput.setOnEditorActionListener { _, _, _ ->
            calculate()
            false
        }

        tipSpinner.onItemSelectedListener = object : AdapterView.OnItemSelectedListener {
            override fun onItemSelected(parent: AdapterView<*>, view: android.view.View?, position: Int, id: Long) {
                calculate()
            }

            override fun onNothingSelected(parent: AdapterView<*>) {}
        }

        roundUpSwitch.setOnCheckedChangeListener { _, _ -> calculate() }
    }
}
