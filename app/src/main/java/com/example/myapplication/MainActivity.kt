package com.example.myapplication

import android.os.Bundle
import android.view.View
import android.widget.Button
import android.widget.TextView
import androidx.appcompat.app.AppCompatActivity
import java.io.*
import java.util.*
import kotlin.random.Random

class MainActivity : AppCompatActivity()
{
    override fun onCreate(savedInstanceState: Bundle?)
    {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        bestScore = getBestScore()
        val show = findViewById<TextView>(R.id.textView1)
        show.text = "score: $score/$bestScore"

        nextGame()                                                              // Start!
    }

    private var ans = 0                                                         // Right answer
    private var mod = 10                                                        // Border size
    private var score = 0
    private var bestScore = 0

    private fun getBestScore() : Int
    {
        try {
            val fin = openFileInput("bestScore.txt")
            return fin.read()
        } catch (ex: IOException) {
            return 0
        }
    }

    private fun updateBestScore()
    {
        val fos = openFileOutput("bestScore.txt", MODE_PRIVATE)
        bestScore = score
        fos.write(bestScore)
    }

    fun checkButton(view: View)
    {
        val button = findViewById<Button>(view.id)
        if (button.text == "$ans")                                              // Correct answer
        {
            score++
            nextGame()
        }
        else                                                                    // Incorrect
        {
            score -= 5
            button.text = "NO!!!"
        }

        if (score > bestScore) updateBestScore()                                // Update score

        val update = findViewById<TextView>(R.id.textView1)
        update.text = "score: $score/$bestScore"
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
        mod += mod / 10                                                         //
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
            left = (left / 10 + Random.nextInt(1, 9)) % 100
            right = (right / 10 + Random.nextInt(1, 9)) % 100
            ans = left * right

            setExpression(left, right, '*')
        }
        else                                                                    // Division
        {
            left = (left / 10 + Random.nextInt(1, 9)) % 100
            right = (right / 10 + Random.nextInt(1, 9)) % 100
            if (left == 0) left = Random.nextInt(2, 9)
            ans = right

            setExpression(left * right, left, '/')
        }
    }
}
