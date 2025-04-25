package com.example.mycalculatortip

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.enableEdgeToEdge
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.mycalculatortip.ui.theme.MyCalculatorTipTheme
import kotlin.math.ceil

class MainActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        setContent {
            MyCalculatorTipTheme {
            
                Scaffold(modifier = Modifier.fillMaxSize()) { innerPadding ->
                    // Panggil fungsi utama aplikasi tip calculator
                    TipCalculatorApp(modifier = Modifier.padding(innerPadding))
                }
            }
        }
    }
}

@Composable
fun TipCalculatorApp(modifier: Modifier = Modifier) {
    var amountInput by remember { mutableStateOf("") }
    var selectedTip by remember { mutableStateOf(0.15) }
    var roundUp by remember { mutableStateOf(false) }

    val amount = amountInput.toDoubleOrNull() ?: 0.0
    var tip = amount * selectedTip
    if (roundUp) tip = ceil(tip)

    Column(
        modifier = modifier
            .fillMaxSize()
            .padding(24.dp),
    ) {
        Text(
            text = "Calculate Tip",
            fontSize = 20.sp,
            fontWeight = FontWeight.Medium
        )

        Spacer(modifier = Modifier.height(16.dp))

        OutlinedTextField(
            value = amountInput,
            onValueChange = { amountInput = it },
            label = { Text("Bill Amount") },
            modifier = Modifier.fillMaxWidth(),
            keyboardOptions = KeyboardOptions(keyboardType = KeyboardType.Number)

        )

        Spacer(modifier = Modifier.height(12.dp))
        Text("Tip Percentage", fontSize = 14.sp, modifier = Modifier.padding(bottom = 4.dp))

        DropdownMenuTip(selectedTip) { selectedTip = it }

        Spacer(modifier = Modifier.height(12.dp))

        Row(
            verticalAlignment = Alignment.CenterVertically,
            modifier = Modifier.fillMaxWidth()
        ) {
            Text("Round up tip?", modifier = Modifier.weight(1f))
            Switch(checked = roundUp, onCheckedChange = { roundUp = it })
        }

        Spacer(modifier = Modifier.height(24.dp))

        Text(
            text = "Tip Amount: $${String.format("%.2f", tip)}",
            fontSize = 36.sp,
            fontWeight = FontWeight.Bold
        )
    }
}
@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun DropdownMenuTip(selectedTip: Double, onTipSelected: (Double) -> Unit) {
    val tips = listOf(0.15, 0.18, 0.20)
    var expanded by remember { mutableStateOf(false) }
    var selectedOptionText by remember {
        mutableStateOf("${(selectedTip * 100).toInt()}%")
    }

    ExposedDropdownMenuBox(
        expanded = expanded,
        onExpandedChange = { expanded = !expanded }
    ) {
        TextField(
            readOnly = true,
            value = selectedOptionText,
            onValueChange = { },
            label = { Text("Tip Percentage") },
            trailingIcon = {
                ExposedDropdownMenuDefaults.TrailingIcon(expanded = expanded)
            },
            modifier = Modifier
                .menuAnchor()
                .fillMaxWidth(),
            colors = ExposedDropdownMenuDefaults.textFieldColors()
        )

        ExposedDropdownMenu(
            expanded = expanded,
            onDismissRequest = { expanded = false }
        ) {
            tips.forEach { tip ->
                DropdownMenuItem(
                    text = { Text("${(tip * 100).toInt()}%") },
                    onClick = {
                        selectedOptionText = "${(tip * 100).toInt()}%"
                        onTipSelected(tip)
                        expanded = false
                    }
                )
            }
        }
    }
}

