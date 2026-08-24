package com.example.guo.algorithm

import org.junit.Test

class LinkedTest {
    /**
     * TODO
     * 翻转链表
     */

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

    /**
     * 合并有序链表
     */
    @Test
    fun mergeLinkedLists() {
        // l1
        val n7 = Node(8, null)
        val n6 = Node(7, n7)
        val n5 = Node(5, n6)

        // l0
        val n4 = Node(4, null)
        val n3 = Node(3, n4)
        val n2 = Node(2, n3)
        val n1 = Node(1, n2)
        val n0 = Node(0, n1)

        // 合并两个有序链表
        println("合并两个有序链表:")
        val result = mergeTwoLists(n0, n5)

        var current = result
        print(current?.value)

        while (current?.next != null) {
            print(current.next?.value)
            current = current.next
        }

        // 翻转链表 todo, 未调通
        println("翻转链表: ${n0.value}")
        val node = reverseList2(n0)

        print("====${node?.value}")
        while (node?.next != null) {
            print(node.next?.value)
        }
    }

    /**
     * 合并两个升序有序链表
     *
     * 时间复杂度：O(m + n)
     * 空间复杂度：O(1)
     */
    fun mergeTwoLists(n1: Node?, n2: Node?): Node? {
        // 虚拟头节点，避免处理第一个节点的特殊情况
        val dummy = Node(0)
        var current = dummy

        var p1 = n1
        var p2 = n2

        // 两个都不为null时 进入 遍历操作
        while (p1 != null && p2 != null) {

            // 二者小值放在current next，升序
            if (p1.value <= p2.value) {
                current.next = p1
                p1 = p1.next
            } else {
                current.next = p2
                p2 = p2.next
            }

            // 给current 设置next
            current = current.next!!
        }

        // 把剩余部分直接接上
        current.next = p1 ?: p2

        return dummy.next
    }

    /**
     * 翻转链表
     *
     * 时间复杂度：O(n)
     * 空间复杂度：O(1)
     */
    fun reverseList(head: Node?): Node? {

        var prev: Node? = null
        var current = head

        while (current != null) {

            // 先保存下一个节点
            val next = current.next

            // 当前节点指向前一个节点
            current.next = prev

            // prev 和 current 同时向后移动
            prev = current
            current = next
        }

        return prev
    }

    fun reverseList2(head: Node?): Node? {
        var prev: Node? = null
        var current = head

        while (current != null) {
            // 先保存下一个节点
            val next  = current.next

            // 当前节点指向前一个节点
            current.next = prev

            // prev 和 current 都向后移
            prev = current
            current = next
            println("==== : ${current?.value}")
        }

        return current
    }

    /**
     * 根据数组创建链表
     */
    fun createList(vararg values: Int): Node? {

        val dummy = Node(0)
        var current = dummy

        for (value in values) {
            current.next = Node(value)
            current = current.next!!
        }

        return dummy.next
    }

    /**
     * 打印链表
     */
    fun printList(head: Node?) {

        var current = head

        while (current != null) {
            print(current.value)

            if (current.next != null) {
                print(" -> ")
            }

            current = current.next
        }

        println()
    }

    /**
     * 将链表转换成 List，方便测试
     */
    fun toList(head: Node?): List<Int> {

        val result = mutableListOf<Int>()

        var current = head

        while (current != null) {
            result.add(current.value)
            current = current.next
        }

        return result
    }

    /**
     * 测试辅助函数
     */
    fun assertList(
        expected: List<Int>,
        actual: Node?
    ) {

        val actualList = toList(actual)

        check(expected == actualList) {
            "测试失败：expected=$expected, actual=$actualList"
        }

        println("PASS: $actualList")
    }


    /**
     * 完整测试
     */
    @Test
    fun main1() {

        println("========== 合并有序链表 ==========")

        // 测试 1
        val list1 = createList(1, 3, 5, 7)
        val list2 = createList(2, 4, 6, 8)

        println("list1:")
        printList(list1)

        println("list2:")
        printList(list2)

        val merged = mergeTwoLists(list1, list2)

        println("merged:")
        printList(merged)

        assertList(
            listOf(1, 2, 3, 4, 5, 6, 7, 8),
            merged
        )


        // 测试 2：包含重复元素
        val list3 = createList(1, 2, 4, 4)
        val list4 = createList(1, 3, 4, 5)

        val merged2 = mergeTwoLists(list3, list4)

        assertList(
            listOf(1, 1, 2, 3, 4, 4, 4, 5),
            merged2
        )


        // 测试 3：一个为空
        val list5 = createList(1, 2, 3)
        val list6: Node? = null

        val merged3 = mergeTwoLists(list5, list6)

        assertList(
            listOf(1, 2, 3),
            merged3
        )


        // 测试 4：两个都为空
        val merged4 = mergeTwoLists(null, null)

        assertList(
            emptyList(),
            merged4
        )


        println()
        println("========== 翻转链表 ==========")

        // 测试 5
        val list7 = createList(1, 2, 3, 4, 5)

        println("before:")
        printList(list7)

        val reversed = reverseList(list7)

        println("after:")
        printList(reversed)

        assertList(
            listOf(5, 4, 3, 2, 1),
            reversed
        )


        // 测试 6：单节点
        val list8 = createList(1)

        val reversed2 = reverseList(list8)

        assertList(
            listOf(1),
            reversed2
        )


        // 测试 7：空链表
        val reversed3 = reverseList(null)

        assertList(
            emptyList(),
            reversed3
        )


        println()
        println("========== 组合测试 ==========")

        // 先合并，再翻转
        val list9 = createList(1, 3, 5)
        val list10 = createList(2, 4, 6)

        val merged5 = mergeTwoLists(list9, list10)

        println("merge:")
        printList(merged5)

        val reversed5 = reverseList(merged5)

        println("reverse:")
        printList(reversed5)

        assertList(
            listOf(6, 5, 4, 3, 2, 1),
            reversed5
        )


        println()
        println("========== 所有测试通过 ==========")
    }
}

class Node(
    val value: Int,
    var next: Node? = null
)