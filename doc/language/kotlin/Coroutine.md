# 一句话总结：

Coroutine 让异步代码写起来像同步代码，同时避免阻塞线程

# 异步写法：

## Coroutine之前：

结果通过回调回调回来

```
getUser{ user->
    showUser(user)
}
```

## Coroutine之后：

本行就能获取到结果，前提是getUser() 是 suspend函数

```
viewModelScope.launch {
    val token = login()
    val profile = getProfile(token)
    val messages = getMessages(profile.id)

    show(profile, messages)
}
```

## 关键区别：

* coroutine 里遇到 suspend 会挂起当前协程
* 不会阻塞线程

# 代码如何执行

launch启动协程后，在协程代码块内的suspend函数会等其返回结果再往下执行

# 设计Coroutine的目的：

让异步、并发、取消、错误处理都变得更像普通代码、更可管理

1. 避免异步写法回调地狱
2. 更自然的表达异步任务
3. 减少线程成本
4. 统一取消和生命周期管理

如Android中 ViewModel 清理时，里面的协程可以自动取消，避免任务泄漏

```
viewModelScope.launch {
    loadData()
}
```

5. 支持结构化并发

多个异步任务之间有父子关系，出错、取消、等待都更可控。

```
viewModelScope.launch {
    val user = async { api.getUser() }
    val orders = async { api.getOrders() }

    val result = user.await()
    val orderList = orders.await()

    show(result, orderList)
}
```

父子关系

```
viewModelScope.launch  // 父协程
 ├─ async getUser()    // 子协程
 └─ async getOrders()  // 子协程
```

结构化并发的意思是：

* 父协程会等待子协程完成
* 父协程取消，子协程也取消



