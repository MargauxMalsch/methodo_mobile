package com.example.myapplication

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.text.TextUtils.isDigitsOnly
import android.util.Log
import android.view.ContextThemeWrapper
import android.view.ViewGroup
import android.widget.LinearLayout
import android.widget.TextView
import androidx.core.text.isDigitsOnly

class MainActivity : AppCompatActivity() {
    companion object {
        private val INPUT_BUTTONS = listOf(
                listOf("1", "2", "3", "/"),
                listOf("4", "5", "6", "*"),
                listOf("7", "8", "9", "-"),
                listOf("0", ".", "=", "+")
        )
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        addCells(findViewById(R.id.calculator_input_container_line1), 0)
        addCells(findViewById(R.id.calculator_input_container_line2), 1)
        addCells(findViewById(R.id.calculator_input_container_line3), 2)
        addCells(findViewById(R.id.calculator_input_container_line4), 3)
    }

    private fun addCells(linearLayout: LinearLayout, position: Int) {
        for (x in INPUT_BUTTONS[position].indices) {
            linearLayout.addView(
                    TextView(
                            ContextThemeWrapper(this, R.style.CalculatorInputButton)
                    ).apply {
                        text = INPUT_BUTTONS[position][x]
                        setOnClickListener { onCellClicked(this.text.toString()) }
                    },
                    LinearLayout.LayoutParams(
                            0,
                            ViewGroup.LayoutParams.MATCH_PARENT,
                            1f
                    )
            )
        }
    }
    private var input: Int? = null
    private var previousInput: Int? = null
    private var symbol: String? = null

    private fun onCellClicked(value: String) {
        fun updateDisplayContainer(value: Any) {
            findViewById<TextView>(R.id.calculator_display_container).text = value.toString()
        }

        fun onEqualsClicked() {
            if (input == null || previousInput == null || symbol == null) {
                return
            }

            updateDisplayContainer(when (symbol) {
                "+" -> input!! + previousInput!!
                "-" -> input!! - previousInput!!
                "*" -> input!! * previousInput!!
                "/" -> input!! / previousInput!!
                else -> "ERROR"
            })

            input = null
            previousInput = null
            symbol = null
        }

        when {
            value.isNum() -> {
                input = value.toInt()
                updateDisplayContainer(value)
            }
            value == "=" -> onEqualsClicked()
            listOf("/", "*", "-", "+").contains(value) -> onSymbolClicked(value)
        }
    }

    private fun onSymbolClicked(symbol: String) {
        this.symbol = symbol
        previousInput = input
        input = null
    }
}


fun String.isNum(): Boolean {
    return length == 1 && isDigitsOnly()
}