package com.tokopedia.climbingstairs

import android.util.Log
import java.lang.Math.pow
import kotlin.math.round
import kotlin.math.sqrt

object Solution {
    fun climbStairs(n: Int): Long {
        // TODO, return in how many distinct ways can you climb to the top. Each time you can either climb 1 or 2 steps.
        // 1 <= n < 90

        val result = fib((n+1).toLong())
        Log.d("ClimbingStairsSolution", result.toString())

        return result
    }

    private fun fib(n: Long) : Long {
        var a = 0L
        var b = 1L
        var c = 1L
        if (n == 0L) return a

        for(i in 2..n){
            println(i)
            c = a + b
            a = b
            b = c
        }

        return b
    }

}
