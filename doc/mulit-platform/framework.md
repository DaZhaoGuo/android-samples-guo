## Mobile

### RN

### Flutter

### Kotlin Multi Platform

## Desktop

参考现代 Web3 钱包(Rabby、Rainbow、Phantom Desktop),主流有三条路线:

### Electron(MetaMask Desktop / Exodus 走的路)

[Detailed description](./wallet/desktop.md)

- 优点:生态最成熟,可直接复用现有 Extension 的 React 代码。
- 缺点:包体大(~100MB+),内存占用高。

### Tauri(Rabby Desktop 走的路)

**适合钱包场景的理由**:

- 私钥加解密、助记词处理用 Rust 写更安全
- 包体小、启动快
- 不内嵌 Chromium,依赖系统 WebView

### React Native Desktop / Flutter Desktop

适合已有 RN / Flutter 移动钱包、想与移动端共用一套 UI 的场景。MetaMask 移动端是 React Native,理论上可用
react-native-windows / react-native-macos 延伸到桌面,但生态稳定性不如 Electron / Tauri。

