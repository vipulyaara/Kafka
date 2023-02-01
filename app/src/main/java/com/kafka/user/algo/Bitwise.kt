package com.kafka.user

fun main() {
    println(findSuccessor(intArrayOf(2, 0, 1)).joinToString()) // 0, 1, 1
    println(findSuccessor(intArrayOf(2, 1, 0)).joinToString()) // 0, 2, 0

    println()

//    println(isFirstSmallerTernary(listOf(2, 0, 1), listOf(0, 1, 1)))
//    println(isFirstSmallerTernary(listOf(2, 0, 1, 0), listOf(2, 0, 1)))
//
//    println()

    println(tritwiseMin(listOf(1,0,2), listOf(1,1,1)))
}

// find the successor of a number represented as an array of ternary bits
fun findSuccessor(list: IntArray): IntArray {
    // create a new array with size + 1; maxOf in case the array is empty
    val result = IntArray(maxOf(list.size, 1) + 1)
    var carry = 0
    for (i in result.indices) {
        val itemBit = if (i < list.size) list[i] else 0
        val additionBit = if (i == 0) 1 else 0 // set the least significant bit
        val sum = (itemBit + additionBit + carry) % 3 // set the bit
        carry = (itemBit + additionBit + carry) / 3 // take carry out
        result[i] = sum
    }

    // remove extra zeros before returning
    return if (result[result.lastIndex] == 0) result.copyOfRange(0, result.lastIndex) else result
}

fun leq(aList: List<Int>, bList: List<Int>): Boolean {
    val aDecimal = aList.foldRight(0) { digit, acc -> acc * 3 + digit } //  apply ternary logic right to left
    val bDecimal = bList.foldRight(0) { digit, acc -> acc * 3 + digit } //  apply ternary logic right to left

    return aDecimal < bDecimal
}

fun tritwiseMin(aList: List<Int>, bList: List<Int>): List<Int> {
    val result = mutableListOf<Int>()
    var i = 0

    while (i <= maxOf(aList.lastIndex, bList.lastIndex)) {
        val aBit = if (i < aList.size) aList[i] else 0
        val bBit = if (i < bList.size) bList[i] else 0
        val minBit = minOf(aBit, bBit)
        result.add(minBit)
        i++
    }

    return result
}

