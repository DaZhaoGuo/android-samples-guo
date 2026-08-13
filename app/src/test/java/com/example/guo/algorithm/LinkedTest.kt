package com.example.guo.algorithm

import org.junit.Test

class LinkedTest {
    /**
     * 反向遍历 - 递归
     */
    fun reversePrint(node: Node? = null) {
        if (node == null) return
        reversePrint(node.next)
        println(node.value)
    }

    @Test
    fun reversePrintLinked() {
        val n4 = Node(4, null)
        val n3 = Node(3, n4)
        val n2 = Node(2, n3)
        val n1 = Node(1, n2)
        val n0 = Node(0, n1)

        reversePrint(n0)
    }
}

class Node(
    val value: Int,
    val next: Node? = null
)