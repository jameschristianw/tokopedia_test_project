package com.tokopedia.oilreservoir

/**
 * Created by fwidjaja on 2019-09-24.
 */
object Solution {
    fun collectOil(height: IntArray): Int? {
        // TODO, return the amount of oil blocks that could be collected
        val maxHeight: Int? = height.max()
        var isInsideWall:Boolean = false
        var currentCount = 0
        var currentOilCount:Int = 0
        var oilCount:Int = 0

        if (maxHeight != null){
            for(it in 0 until maxHeight){
                currentOilCount = 0
                isInsideWall = false

                height.forEachIndexed{ index, element ->
                    if(element > 0){
                        isInsideWall = true
                        currentCount = 0
                        element.minus(1)
                        return@forEachIndexed
                    }

                    if (isInsideWall) {
                        currentOilCount++
                        currentCount++
                    }
                    if(index == height.size && isInsideWall){
                        currentOilCount -= currentCount
                    }
                }
                oilCount += currentOilCount
            }
        }

        return oilCount
    }
}
