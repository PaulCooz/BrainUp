package com.example.myapplication

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.widget.Button
import android.widget.TextView
import kotlin.random.Random

class MainActivity : AppCompatActivity()
{
    private var sum: Int = 0
    private var ans: Int = 0
    private var mod: Int = 10

    override fun onCreate(savedInstanceState: Bundle?)
    {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        val button1: Button = findViewById(R.id.button1)
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

        nextGame()
    }

    fun checkButton(currentButt: Button)
    {
        val textView: TextView = findViewById(R.id.textView1)
        if (currentButt.text == "$sum")
        {
            ans++
            nextGame()
        }
        else
        {
            ans -= mod / 10
            currentButt.text = "NO!!!"
        }
        textView.text = "scores: $ans"
    }

    var isSetSum = false
    var adds: MutableSet<Int> = mutableSetOf(0)
    fun setIntOn(currentButt: Button)
    {
        if (!isSetSum && (Random.nextBoolean() || currentButt == findViewById<Button>(R.id.button4)))
        {
            currentButt.text = "$sum"
            isSetSum = true
        }
        else
        {
            var add = Random.nextInt(-25, 25)
            if (Random.nextBoolean() && add % 10 != 0)
            {
                add /= 2
                add *= 10
            }

            if (adds.contains(add))
            {
                add = adds.last() + 1
            }
            if (add == 0) add = 10

            adds.add(add)

            var nextSum = sum + add
            currentButt.text = "$nextSum"
        }
    }

    fun nextGame()
    {
        mod += mod / 2
        mod = if (mod > 10000) 10000 else mod

        var left  = Random.nextInt(-mod, mod)
        var right = Random.nextInt(-mod, mod)
        sum = left + right

        val textView: TextView = findViewById(R.id.textView)
        if (right < 0)
        {
            right *= -1;
            textView.text = "$left - $right = ?"
        }
        else
        {
            textView.text = "$left + $right = ?"
        }

        isSetSum = false
        adds.clear()
        setIntOn(findViewById(R.id.button1))
        setIntOn(findViewById(R.id.button2))
        setIntOn(findViewById(R.id.button3))
        setIntOn(findViewById(R.id.button4))
    }
}
