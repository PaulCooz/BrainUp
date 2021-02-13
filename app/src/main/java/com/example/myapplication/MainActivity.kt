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

    var ans = 0                                                                 // Right answer
    var mod = 10                                                                // Border size
    var score = 0
    var left = 0
    var right = 0

    private var bestScore = 0
    fun getBestScore() : Int
    {
        return try
        {
            val fin = openFileInput("bestScore.txt")
            fin.read()
        }
        catch (ex: IOException)
        {
            0
        }
    }

    fun updateBestScore()
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

            val nextAns = ans + add
            currentButt.text = "$nextAns"
        }
    }

    fun setExpression(c: Char)                                                  // Set main text
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

    fun pow() : Int                                                             // bin pow
    {
        var res = 1
        var p = left
        var d = right

        while(d > 0)
        {
            if (d % 2 == 1) res *= p
            p *= p
            d /= 2
        }

        return res
    }

    fun nextGame()                                                              // Make scene
    {
        mod += mod / 10                                                         // Inc range
        mod = if (mod > 1000) 1000 else mod

        if (Random.nextBoolean())                                               // Addition
        {
            left  = Random.nextInt(-mod, mod)
            right = Random.nextInt(-mod, mod)
            ans = left + right

            setExpression('+')
        }
        else if (Random.nextBoolean())                                          // Multiplication
        {
            left  = Random.nextInt(-mod / 10, mod / 10) % 100
            right = Random.nextInt(-mod / 10, mod / 10) % 100
            ans = left * right

            setExpression('*')
        }
        else if (Random.nextBoolean())                                          // Division
        {
            ans = Random.nextInt(-mod / 10, mod / 10) % 100
            right = Random.nextInt(-mod / 10, mod / 10) % 100
            if (right == 0) right++
            left = ans * right

            setExpression('/')
        }
        else if (Random.nextBoolean())                                          // Remainder
        {
            left = Random.nextInt(-mod, mod) % 100
            if (left == 0) left++
            right = Random.nextInt(-mod, mod) % left
            if (right == 0) right++
            ans = left % right

            setExpression('%')
        }
        else                                                                    // Pow
        {
            left = Random.nextInt(-mod / 10, mod / 10) % 100
            right = Random.nextInt(0, 4)
            if (right > 2) left %= 10
            ans = pow()

            setExpression('^')
        }
    }
}
