package com.tokopedia.minimumpathsum

import android.util.Log

object Solution {
    fun minimumPathSum(matrix: Array<IntArray>): Int {
        val row = matrix.size - 1
        val col = matrix[0].size - 1

        Log.d("MPS Solution", "Row: $row")
        Log.d("MPS Solution", "Col: $col")
        Log.d("MPS Solution", "matrix[${row}][${col}]: ${matrix[row][col]}")
        Log.d("MPS Solution", "matrix[0][0]: ${matrix[0][0]}")

        // Jarak src dan dst + src dan dst
        val res = minCost(matrix, row, col) + matrix[row][col] + matrix[0][0]
        Log.d("MPS Solution", "Res: $res")

        Log.d("MPS Solution", "matrix: ${matrix.toString()}")

        return res
    }

    private fun minCost(cost: Array<IntArray>, m:Int, n:Int): Int {
        return if (n < 0 || m < 0)
            Int.MAX_VALUE
        else if (m == 0 && n == 0) {
            cost[m][n]
        }
        else {
            cost[m][n] + min(minCost(cost, m - 1, n - 1),
                    minCost(cost, m - 1, n),
                    minCost(cost, m, n - 1))
        }
    }

    private fun min(x: Int, y: Int, z: Int): Int {
        return if (x < y) if (x < z) x else z else if (y < z) y else z
    }

}
