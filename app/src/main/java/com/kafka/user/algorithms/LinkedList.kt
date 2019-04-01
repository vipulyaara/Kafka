package com.kafka.user.algorithms


/**
 * @author Vipul Kumar; dated 25/03/19.
 */

fun main() {
    val array = arrayOf(1, 2, 3, 4, 5, 6, 7, 8, 9)
    var reversed = buildLinkedList(null, array, 0)
//    print("${head} ")

    while(reversed.next != null) {
        print("node ${reversed.data} ")
        reversed = reversed.next!!
    }
}

// takes an array and returns head of list
fun buildLinkedList(node: LinkedList.Node?, array: Array<Int>, position: Int): LinkedList.Node {
    var head = node
    if (head == null) {
        head = LinkedList.Node()
    }
    if (position < array.size) {
        head.also {
            it.data = array[position]
            it.next = buildLinkedList(head, array, position + 1)
        }
    }
    return head
}

fun reverseList(node: LinkedList.Node?): LinkedList.Node? {
    var head = node
    if (head == null) {
        return head
    }

    while (head.next != null) {
        val temp = head.next?.next
        head.next?.next = head
        head.next = temp
    }
    return head
}

object LinkedList {
    class Node {
        var data: Int? = null
        var next: Node? = null

        override fun toString(): String {
            return "$data"
        }
    }
}
