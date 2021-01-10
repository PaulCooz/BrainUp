package com.example.myapplication

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.widget.Button
import android.widget.TextView
import java.util.*
import kotlin.math.abs
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

    private var ans = 0                                                         // Right answer
    private var mod = 10                                                        // Border size
    private var score = 0                                                       // Current score

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
    var previous: SortedSet<Int> = sortedSetOf()

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

            if (previous.contains(add))                                         // Remove repeats
            {
                add = previous.last() + 10
                if (add == 0) add += 10
            }
            previous.add(add)

            var nextAns = ans + add
            currentButt.text = "$nextAns"
        }
    }

    fun setExpression(left: Int, right: Int, c: Char)                           // Set main text
    {
        var out = ""
        out += if (left < 0) "($left)" else "$left"
        out += " $c "
        out += if (right < 0) "($right)" else "$right"
        out += " = ?"

        val textView: TextView = findViewById(R.id.textView)
        textView.text = out

        isSetAns = false
        previous = sortedSetOf(0)

        setIntOn(findViewById(R.id.button1))
        setIntOn(findViewById(R.id.button2))
        setIntOn(findViewById(R.id.button3))
        setIntOn(findViewById(R.id.button4))
    }

    fun nextGame()                                                              // Make scene
    {
        mod += mod / 10
        mod = if (mod > 1000) 1000 else mod

        var left  = Random.nextInt(-mod, mod)
        var right = Random.nextInt(-mod, mod)

        if (Random.nextBoolean())                                               // Addition
        {
            ans = left + right

            setExpression(left, right, '+')
        }
        else if (Random.nextBoolean())                                          // Multiplication
        {
            left = abs(left / 10 + Random.nextInt(1, 9)) % 100
            right = abs(right / 10 + Random.nextInt(1, 9)) % 100
            ans = left * right

            setExpression(left, right, '*')
        }
        else                                                                    // Division
        {
            left = abs(left / 10 + Random.nextInt(1, 9)) % 100 + 1
            right = abs(right / 10 + Random.nextInt(1, 9)) % 100 + 1
            ans = right

            setExpression(left * right, left, '/')
        }
    }
}
