# MetaMask 桌面端如何实现跨平台

> 本文整理 MetaMask Desktop 项目的技术方案,以及自研 Web3 钱包桌面端时主流的跨平台实现选型。

---

## 一、背景:MetaMask Desktop 项目现状

MetaMask 目前的主要形态是 **浏览器扩展** 和 **移动端 App**。官方的 `MetaMask/metamask-desktop` 仓库是一个实验性项目,已于 2023 年被 **archived**(归档停止维护)。

虽然项目本身被放弃了,但它的架构依然具有参考价值,尤其是 "扩展 + 桌面代理" 这种模式在需要突破 Chrome MV3 限制的场景下仍是可选方案。

---

## 二、MetaMask Desktop 的原始技术方案

### 2.1 技术栈

| 层级 | 选型 |
|------|------|
| 应用壳 | Electron |
| 后台服务 | Node.js |
| UI 层 | React + Redux(复用扩展代码) |
| 通信 | WebSocket / UDP(扩展 ↔ 桌面) |
| 打包 | electron-builder |

### 2.2 核心架构 —— Extension Pairing 模式

MetaMask Desktop 没有把自己做成独立钱包,而是作为 **浏览器插件的后端代理**:

```
┌────────────────────┐     WebSocket      ┌─────────────────────┐
│ MetaMask Extension │  ◄───────────────► │  MetaMask Desktop    │
│   (Chrome/FF/...)  │   (加密配对通道)    │   (Electron + Node)  │
└────────────────────┘                    └─────────────────────┘
        ↑                                           ↑
       UI 层                              真正的钱包后台(keyring、
                                          RPC、交易签名)迁移到这里
```

- 插件里原来的 background service worker 被剥离,逻辑全部迁移到 Desktop 进程
- 动机:Chrome MV3 下 service worker 生命周期很短,扩容受限,桌面端没这个限制
- 配对流程:OTP(一次性验证码) + 加密 WebSocket 通道

### 2.3 为什么被放弃

- 用户体验复杂度高(需要安装两个东西,配对步骤繁琐)
- 维护成本大(同时维护扩展 + 桌面两份代码)
- 直接用扩展已能覆盖绝大多数场景

---