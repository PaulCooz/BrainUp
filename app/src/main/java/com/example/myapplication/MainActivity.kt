package com.example.myapplication

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.widget.Button
import android.widget.TextView
import kotlin.random.Random

class MainActivity : AppCompatActivity()
{
    override fun onCreate(savedInstanceState: Bundle?)
    {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        val button1: Button = findViewById(R.id.button1)                        // Set all buttons
        button1.setOnClickListener()
        {
            checkButton(button1)
        }
        val button2: Button = findViewById(R.id.button2)
        button2.setOnClickListener()
        {
            checkButton(button2)
        }
        val button3: Button = findViewById(R.id.button3)
        button3.setOnClickListener()
        {
            checkButton(button3)
        }
        val button4: Button = findViewById(R.id.button4)
        button4.setOnClickListener()
        {
            checkButton(button4)
        }

        nextGame()                                                              // Start!
    }

    private var ans: Int = 0                                                    // Right answer
    private var mod: Int = 10                                                   // Border size
    private var score: Int = 0                                                  // Current score

    fun checkButton(currentButt: Button)                                        // If clicked
    {
        if (currentButt.text == "$ans")                                         // Correct answer
        {
            score++
            nextGame()
        }
        else                                                                    // Incorrect
        {
            score -= 5
            currentButt.text = "NO!!!"
        }

        val textView: TextView = findViewById(R.id.textView1)                   // Update score
        textView.text = "scores: $score"
    }

    var isSetAns = false
    var adds: MutableSet<Int> = mutableSetOf(0)
    fun setIntOn(currentButt: Button)
    {
        if (!isSetAns &&                                                        // If'n set before
            (Random.nextBoolean() || currentButt == findViewById(R.id.button4)))
        {
            currentButt.text = "$ans"
            isSetAns = true
        }
        else                                                                    // Set wrong ans
        {
            var add = Random.nextInt(-19, 19)
            if (Random.nextBoolean() && add % 10 != 0)
            {
                add /= 2
                add *= 10
            }

            if (adds.contains(add))
            {
                add = adds.last() + 1
            }
            if (add == 0)
            {
                add = 10
            }

            adds.add(add)

            var nextAns = ans + add
            currentButt.text = "$nextAns"
        }
    }

    fun setExpression(left: Int, right: Int, c: Char)                           // Set main text
    {
        var out = ""
        out += if (left < 0) "($left)" else "$left"
        out += if (c == '+') " + " else " * "
        out += if (right < 0) "($right)" else "$right"
        out += " = ?"

        val textView: TextView = findViewById(R.id.textView)
        textView.text = out

        isSetAns = false
        adds.clear()

        setIntOn(findViewById(R.id.button1))
        setIntOn(findViewById(R.id.button2))
        setIntOn(findViewById(R.id.button3))
        setIntOn(findViewById(R.id.button4))
    }

    fun nextGame()                                                              // Make scene
    {
        mod += mod / 5
        mod = if (mod > 10000) 10000 else mod

        var left  = Random.nextInt(-mod, mod)
        var right = Random.nextInt(-mod, mod)

        if (Random.nextBoolean())                                               // Addition
        {
            ans = left + right
            setExpression(left, right, '+')
        }
        else                                                                    // Multiplication
        {
            left = (left / 10 + Random.nextInt(-1, 2)) % 100
            right = (right / 10 + Random.nextInt(-1, 2)) % 1000
            if (Random.nextBoolean())                                           // Swap
            {
                left += right
                right = left - right
                left -= right
            }

            ans = left * right
            setExpression(left, right, '*')
        }
    }
}
