# Android 性能优化：工具定位与实践学习路线

面向有 Android 实战经验的开发者｜从“会优化”升级到“会测量、会定位、会证明”

## 一、学习目标

这套路线不是从 API 八股开始，而是训练一套性能问题闭环：

现象 → 指标 → 稳定复现 → 采集 Trace/Profile → 定位瓶颈 → 建立原因假设 → 修改 → Benchmark/Trace 验证 → 防止回归。

最终应该能够独立处理：
1. 启动慢：冷启动、温启动、首帧、完全可用时间。
2. 掉帧/卡顿：主线程耗时、RenderThread、GPU、Binder、锁竞争、IO。
3. 内存问题：泄漏、Java/Kotlin Heap、Native Heap、GC 抖动、Bitmap。
4. CPU 问题：高 CPU、线程调度、锁竞争、频繁计算。
5. 网络/IO：主线程 IO、数据库、磁盘、Binder、网络等待。
6. APK/运行时：R8、资源、DEX、Baseline Profile、启动优化。
7. 建立可持续的性能基线和 CI 回归检测。

核心原则：先测量，再优化；先找到热点，再决定方案。不要从“我听说 XXX 优化有效”开始。

## 二、先建立 Android 性能模型

把 Android 性能问题先拆成几个系统：

1. UI/渲染链路
   UI Thread → RenderThread → GPU/SurfaceFlinger
   重点：一帧为什么超时？到底是谁占用了预算？

2. 应用启动
   Zygote/进程创建 → Application → ContentProvider → Activity → 首帧 → Fully Drawn
   重点：哪些初始化真正阻塞了启动？

3. CPU/线程
   Runnable / Running / Sleeping / Blocked / Binder 等状态
   重点：线程是在“算”、在“等”、还是在“抢锁”？

4. 内存
   Java/Kotlin Heap + Native Heap + Graphics + Stack + 映射文件
   重点：持续增长、瞬时峰值、GC 频繁、Native 泄漏分别是什么？

5. IO/Binder/网络
   重点不是“慢”，而是识别等待链：谁在等谁、等待发生在哪里。

6. ART/编译
   JIT/AOT、Baseline Profile、R8、dex 优化会影响启动和运行时性能。

建议先记住一句话：
“性能优化不是让代码看起来更快，而是减少关键路径上的实际耗时和资源竞争。”

## 三、工具地图：先学哪些工具

第一阶段只学 5 个工具，不要同时铺开：

A. Android Studio Profiler
- CPU Profiler：线程、CPU、方法调用、System Trace。
- Memory Profiler：Heap、GC、Allocation、泄漏线索。
- Network Profiler：网络请求时间、流量和请求行为。
用途：入门和快速定位。

B. Perfetto —— 重点
Android 9+ 上，Perfetto 是最完整的系统级性能分析工具。
要学会：
- CPU slices
- Process / Thread tracks
- sched_switch / 调度
- Binder
- Choreographer / FrameTimeline
- RenderThread
- I/O
- wakelock / power（后期）
- 自定义 trace section
核心能力：看到“时间线上到底发生了什么”。

C. adb + dumpsys
至少掌握：
adb shell dumpsys gfxinfo <package>
adb shell dumpsys meminfo <package>
adb shell dumpsys cpuinfo
adb shell dumpsys activity top
adb shell dumpsys package <package>
以及 perfetto / atrace 相关命令。
用途：不依赖 IDE，从系统角度验证。

D. Macrobenchmark
用真实用户路径测量：
- Startup
- FrameTiming
- 滚动
- 动画
- 自定义 TraceSection
它解决“我觉得变快了”无法证明的问题。

E. LeakCanary
主要用于 Java/Kotlin 对象泄漏定位。
注意：LeakCanary ≠ 内存性能的全部。Native、Graphics、Bitmap、FD、线程等问题要用其他工具。

## 四、第一阶段：先用一个小项目练习（1～2 天）

不要直接拿生产项目练。先建立一个 PerformanceLab。

建议创建 5 个故意有问题的页面：

1. StartupActivity
   - Application 中 sleep/大量初始化
   - Activity 中同步读取 SharedPreferences/文件
   - 模拟 SDK 初始化

2. JankActivity
   - onDraw / bind 中做大量计算
   - RecyclerView/Compose 列表制造重活
   - 主线程 Bitmap 解码

3. MemoryActivity
   - Activity 被静态对象持有
   - Adapter/Listener 生命周期错误
   - 大 Bitmap
   - 大集合不断增长

4. CPUActivity
   - 高频计算
   - 多线程争抢锁
   - 无限/高频任务

5. IODatabaseActivity
   - 主线程 SQLite 查询
   - 大文件读写
   - Binder 高频调用

每个问题都故意制造，然后只允许自己用工具找原因。

验收标准：
- 你不能先看代码猜答案。
- 必须先得到“现象数据”。
- 必须截图/保存 Trace。
- 再提出原因假设。
- 修改后重新测量。

## 五、第二阶段：Perfetto 核心训练（3～5 天）

这是整个路线最重要的一阶段。

练习 1：主线程卡顿
故意执行：
Thread.sleep(100)
或者一段 100ms+ 的计算。

操作：
1. 打开 System Trace / Perfetto。
2. 找到目标进程。
3. 找主线程 Main Thread。
4. 找到 Runnable/Running 的时间段。
5. 放大时间轴。
6. 看该时间段下面的调用/调度信息。
7. 判断是 CPU 真正在执行，还是在等待。

练习 2：锁竞争
线程 A 持有 synchronized 锁 200ms。
线程 B 进入锁等待。

你需要回答：
- B 为什么没有运行？
- B 是 CPU 不够，还是在 Blocked？
- 哪个线程持有锁？
- 持锁线程又在干什么？

练习 3：Binder
制造主线程 Binder 调用。
观察：
Main Thread → Binder transaction → Server Thread
理解跨进程调用的等待链。

练习 4：IO
主线程读取大文件。
在 Trace 中找到：
Main Thread → read/open/fsync 等 IO 活动
最终回答：
“主线程卡住的 80ms 是 CPU、锁、Binder 还是 IO？”

练习 5：掉帧
制造一次 30～50ms 的主线程任务。
从 FrameTimeline/帧相关轨迹找到：
- 哪一帧超时
- 哪个线程拖慢
- 超时发生在应用侧还是渲染链路

Perfetto 学习的核心不是记 Track 名称，而是训练：
“时间 → 线程 → 状态 → 依赖 → 根因”的分析能力。

## 六、第三阶段：Android Studio CPU Profiler（2 天）

重点掌握三种思路：

1. Callstack/方法耗时
适合回答：
“哪个方法花了最多 CPU 时间？”

2. System Trace
适合回答：
“线程之间发生了什么？”

3. Sampled Profiling vs Instrumentation
- Sampled：开销较低，适合看 CPU 热点。
- Instrumentation：记录方法调用更完整，但开销明显更大。

练习：
写三个版本：
A. for 循环大量计算
B. 大量对象创建
C. 锁竞争

分别用 CPU Profiler 分析。

最终要能区分：
CPU Hotspot ≠ UI Jank 根因。
一个方法 CPU 时间高，不代表它就是用户感知卡顿的关键路径。

## 七、第四阶段：内存优化（3～4 天）

按这个顺序学习：

1. Memory Profiler
- Java/Kotlin Heap
- Allocation
- GC
- Heap Dump

2. LeakCanary
练习：
Activity → Singleton → Activity 泄漏。
要求自己从 GC Root 一路找到引用链。

3. Bitmap
理解：
- Bitmap 占用
- 分辨率
- 色彩格式
- decode/resize
- cache

4. Native Memory
了解：
Java Heap 正常 ≠ App 内存正常。
进一步学习 Native Heap、malloc、heapprofd。

5. GC
制造大量临时对象：
- 高频字符串
- 临时 List/Map
- JSON 对象
观察 GC 次数和暂停/CPU 影响。

6. FD / Thread 泄漏
理解：
资源泄漏不只发生在 Java Object。

验收：
给一个“内存持续上涨”的 App，你应该先判断：
是泄漏、缓存增长、Bitmap、Native、线程还是正常的堆扩张，而不是直接说“GC 不及时”。

## 八、第五阶段：启动性能（3 天）

建立启动时间模型：

Cold Start
→ Process 创建
→ Application
→ ContentProvider
→ Activity
→ 首帧
→ Fully Drawn

重点工具：
1. Perfetto/System Trace
2. Macrobenchmark StartupTimingMetric
3. Baseline Profile

练习：
故意加入：
- Application 初始化
- ContentProvider 初始化
- 同步磁盘 IO
- 大量类加载
- SDK 初始化

然后逐个定位。

建立自己的启动优化清单：
- Application.onCreate()
- ContentProvider
- 第三方 SDK
- 主线程 IO
- 同步初始化
- 类加载
- 首屏布局/Compose
- 网络等待
- 数据库初始化
- Baseline Profile

特别注意：
不要把“首帧出来”当成“用户可以使用”。
必要时测量 Fully Drawn / 完全可用时间。

## 九、第六阶段：Macrobenchmark + 性能基线（3～5 天）

这是从“性能调试”进入“性能工程”的关键一步。

至少写 4 个 Benchmark：

1. coldStartup
2. warmStartup
3. scroll
4. 关键业务流程

建议记录：
- P50
- P90/P95
- P99
- FrameTiming
- frameOverrunMs
- 自定义 TraceSection

建立这样的实验记录：

| 项目 | 修改前 | 修改后 | 变化 |
|---|---:|---:|---:|
| Cold Startup P50 |  |  |  |
| Fully Drawn P50 |  |  |  |
| Frame P95 |  |  |  |
| Frame Overrun P99 |  |  |  |
| Memory Peak |  |  |  |

不要只记录平均值。启动等指标通常更关注 median；长尾问题则需要关注 P95/P99。

最终把 Benchmark 接入 CI，让性能回归可以被自动发现。

## 十、第七阶段：专项性能优化路线

完成工具基础后，再按专题深入。

A. UI/掉帧
学习顺序：
16.67ms/帧预算 → 主线程 → RenderThread → GPU → Binder/IO/锁 → FrameTimeline

B. 启动
Application → Provider → SDK → 类加载 → IO → 首帧 → Fully Drawn → Baseline Profile

C. 内存
Heap → Allocation → Leak → Bitmap → Native → GC → Graphics

D. CPU
CPU Profiler → Perfetto → scheduler → runnable/running → lock → Binder

E. 网络
DNS → Connect → TLS → Request → Server → Response → JSON → DB/UI

F. 数据库
SQLite/Room → query plan → index → transaction → Cursor → IO

G. APK/构建
R8 → resource shrink → DEX → startup → Baseline Profile

H. 功耗
WakeLock → Job/Alarm → 网络 → CPU → 后台任务 → Battery Historian/Perfetto


## 十一、建议的 4 周学习计划

第 1 周：工具入门
Day 1：建立 PerformanceLab
Day 2：Android Studio CPU/Memory Profiler
Day 3：Perfetto 基础
Day 4：Perfetto 主线程/锁/Binder
Day 5：Perfetto 掉帧
周末：独立分析 3 个故障

第 2 周：内存 + 启动
Day 1：Heap/Allocation
Day 2：LeakCanary
Day 3：Native/Bitmap/GC
Day 4：启动 Trace
Day 5：ContentProvider/SDK/IO
周末：做一次启动优化实验

第 3 周：Benchmark
Day 1：Macrobenchmark
Day 2：Startup Benchmark
Day 3：FrameTiming
Day 4：TraceSection
Day 5：Baseline Profile
周末：接入一个简单 CI 性能基线

第 4 周：项目实战
选择你实际项目中的一个复杂页面，例如：
- IM 消息列表
- 钱包首页
- Swap/交易页面
- DApp 页面
- 语音房/屏幕共享

完整走一遍：
现象 → 指标 → 复现 → Trace → 根因 → 修改 → Benchmark → 对比报告。

如果你能完成这一周，性能优化就从“知识点”变成了工程能力。

## 十二、你尤其应该练的 10 个面试题

1. Android 一帧为什么会掉帧？如何定位？
2. Perfetto 中 Main Thread 一段时间没有运行，怎么判断是在等锁、Binder、IO 还是 CPU？
3. 如何分析 App 冷启动？
4. Application.onCreate() 中有 300ms 初始化，如何证明它影响启动？
5. RecyclerView 滑动卡顿如何定位？
6. Compose LazyColumn 卡顿如何定位？
7. Java Heap 没有明显增长，但 RSS 持续上涨，可能是什么？
8. GC 频繁如何定位？GC 一定是性能问题吗？
9. 如何证明一次优化确实有效？
10. Macrobenchmark 为什么不能直接跑 Debuggable App？为什么推荐实体机？

回答这些问题时，不要只说优化手段。
按照：
“指标 → 工具 → 现象 → 原因 → 方案 → 验证”
回答，面试质量会明显高于背优化 checklist。

## 十三、你自己的 PerformanceLab 最终目录

建议最终做成：

PerformanceLab/
├── startup/
│   ├── SlowApplication
│   ├── SlowProvider
│   ├── SlowActivity
│   └── benchmark/
├── jank/
│   ├── MainThreadBlock
│   ├── LockContention
│   ├── BinderBlock
│   └── benchmark/
├── memory/
│   ├── ActivityLeak
│   ├── BitmapLeak
│   ├── AllocationStorm
│   └── NativeMemory
├── cpu/
│   ├── CpuHotspot
│   └── LockContention
├── io/
│   ├── FileIO
│   └── DatabaseIO
├── compose/
│   ├── Recomposition
│   └── LazyColumnJank
├── benchmark/
│   ├── StartupBenchmark
│   ├── FrameBenchmark
│   └── BaselineProfile
└── reports/
    ├── startup.md
    ├── jank.md
    ├── memory.md
    └── benchmark.md

每个实验都保存：
1. 问题描述
2. 复现步骤
3. 基线数据
4. Trace/Screenshot
5. 根因
6. 修改
7. 修改后数据
8. 结论


## 十四、最重要的学习方法

不要按“性能优化大全”从头看到尾。

推荐循环：

学一个工具
↓
故意制造一个问题
↓
用工具定位
↓
解释 Trace
↓
修改
↓
重新 Benchmark
↓
写 5～10 行实验报告
↓
再进入下一个问题

尤其是 Perfetto：
第一阶段不要追求把所有 Track 都看懂。
先做到下面四件事：

① 找到进程
② 找到线程
③ 找到异常时间段
④ 解释这段时间线程为什么没有完成工作

当这四件事熟练之后，再学习 FrameTimeline、Binder、ftrace、heapprofd、GPU 等高级内容。

## 十五、官方资料（建议按顺序阅读）

1. Android 性能检查总览
https://developer.android.com/topic/performance/inspecting-overview

2. System Tracing
https://developer.android.com/topic/performance/tracing/

3. Perfetto
https://developer.android.com/tools/perfetto

4. Benchmark 总览
https://developer.android.com/topic/performance/benchmarking/benchmarking-overview

5. Macrobenchmark
https://developer.android.com/topic/performance/benchmarking/macrobenchmark-overview

6. Macrobenchmark 实战 Codelab
https://developer.android.com/codelabs/android-macrobenchmark-inspect

7. Baseline Profiles
https://developer.android.com/topic/performance/baselineprofiles/overview

8. 创建 Baseline Profile
https://developer.android.com/topic/performance/baselineprofiles/create-baselineprofile


## 十六、最终能力检查表

□ 能独立使用 Perfetto 分析主线程卡顿
□ 能区分 CPU / Lock / Binder / IO 等等待
□ 能定位一次掉帧对应的具体时间段
□ 能分析 RenderThread 和应用主线程的关系
□ 能用 Memory Profiler 找 Heap/Allocation 问题
□ 能使用 LeakCanary 找 Java/Kotlin 泄漏
□ 知道 Java Heap、Native Heap、Graphics、RSS 的区别
□ 能分析冷启动
□ 能使用 Macrobenchmark 测量启动和帧时间
□ 能理解 P50/P95/P99
□ 能写 TraceSection
□ 能使用 Baseline Profile
□ 能建立性能基线
□ 能把性能测试放进 CI
□ 能写一份“性能问题 → 定位 → 优化 → 数据证明”的完整报告

达到这里，就不只是“会 Android 性能优化”，而是具备了 Android Performance Engineering 的基本工作流。

