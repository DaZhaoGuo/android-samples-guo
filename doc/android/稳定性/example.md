* ⼏个典型稳定性问题：
    * Crash monkey测试 ⼩概率：ListView:mItemCount != adapter.getItemCount成员变量值更新不及时。解决⽅法：改⽤局部变量实时获取
    * Crash monkey测试 ⼩概率 空指针：多线程操作，变量未赋值就被另⼀线程调⽤，出现空指针。解决⽅法：调⽤前判空赋值
    * Crash monkey测试 ⼩概率 IllegalStateException：桌⾯获取DynamicShortcust，⽤户被锁定抛异常
    * Crash ⼩概率 测试类型未知，奔溃栈无当前应用调用栈，ViewRootImpl.performTraversals()
      遍历⼦view时mDisplay.getRealSize(size)为空。来⾃1102A芯⽚⼿机⽆线投屏断连。
      复现路径：⾸次⽆线投屏连接，同源模式，拉起推荐页，此时主动断开连接，应⽤crash。
      原因：推荐页在Explorer.apk中，和ExplorerService在同⼀进程，投屏连接时ExplorerService所在进程已经被拉起，断连时Explorer被触发去显⽰没有获取到Display，Display为null
      解决⽅法：其他问题修改规避了该问题，分进程android:process=，推荐页onDestory()时Process.killProces(
      mypid);
    * Crash monkey测试 ⼩概率，window句柄异常，native⽇志底层分析给出传参错误，结合app_log看出问题发⽣在⼀个Activity
      解决办法：梳理业务流程、组件使⽤规范有⽆不合理的地⽅：注销⼴播写在onDestroy中，onDestroy不能保证⼀定被执⾏到，占⽤资源没有及时被释放。应该onStop()
      中结合isStoping()来注销
    * Crash monkey测试 ⼩概率，BinderTransactionTooLarge，最⼤5M多，传到了6M多，原因：句柄fd异常，打开⽂件数O版本有提升
    * binder异常，底层出错