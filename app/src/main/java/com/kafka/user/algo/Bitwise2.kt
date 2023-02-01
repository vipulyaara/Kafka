package com.kafka.user

fun main() {
    println(f(listOf(2,4,6,5)))
    println(fEff(listOf(2,4,6,5)))
    println(xorList(listOf(2,4,6,5)))
}

fun andList(list: List<Int>): Int {
    return list.foldRight(1) { digit, acc -> digit and acc }
}

fun xorList(list: List<Int>): Int {
    return list.foldRight(0) { digit, acc -> digit xor acc }
}

fun f(list: List<Int>): Int {
    return andList(findSubsets(list).map { xorList(it) })
}

fun findSubsets(list: List<Int>): List<List<Int>> {
    val subsets = mutableListOf<List<Int>>()
    for (i in list.indices) {
        for (j in i + 1..list.size) {
            subsets.add(list.subList(i, j))
        }
    }
    return subsets
}

fun fEff(arr: List<Int>): Int {
    val n = arr.size
    var sum = 1
    for (i in 0 until n) {
        var xorr = 0
        for (j in i until n) {
            xorr = xorr xor arr[j]
            sum = sum and xorr
        }
    }
    return sum
}
