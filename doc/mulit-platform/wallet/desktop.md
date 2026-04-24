

## 自研钱包桌面端的三条主流路线

### 方案 A:Electron

代表:MetaMask Desktop、Exodus

| 维度 | 说明 |
|------|------|
| 渲染层 | Chromium |
| 主进程 | Node.js,可直接用 `ethers.js` / `web3.js` / 原生模块 |
| 跨平台 | macOS / Windows / Linux 一套代码 |
| 打包 | `electron-builder` → `.dmg` / `.exe` / `.AppImage` |
| 更新 | `electron-updater` + 代码签名 |
| 包体 | 通常 100MB+ |
| 内存 | 较高 |

**优点**:生态最成熟,能直接复用现有扩展的 React 代码。  
**缺点**:包体大、资源占用高。

### 方案 B:Tauri

代表:Rabby Desktop

| 维度 | 说明 |
|------|------|
| 渲染层 | 系统自带 WebView(macOS WKWebView / Windows WebView2 / Linux WebKitGTK) |
| 主进程 | Rust |
| 包体 | 通常 < 10MB |
| 安全 | Rust 内存安全 + 细粒度 IPC 权限控制 |

**适合钱包场景的理由**:
- 私钥加解密、助记词处理用 Rust 写更安全
- 包体小、启动快
- 不内嵌 Chromium,依赖系统 WebView

### 方案 C:React Native Desktop / Flutter Desktop

适合已有 RN / Flutter 移动钱包、想与移动端共用一套 UI 的场景。MetaMask 移动端是 React Native,理论上可用 `react-native-windows` / `react-native-macos` 延伸到桌面,但生态稳定性不如 Electron / Tauri。

---

## 跨平台的关键模块

不管用哪种壳,钱包核心功能都要处理这些平台差异。

### 4.1 密钥安全存储

| 平台 | 推荐方案 |
|------|---------|
| macOS | Keychain(`security` / `keytar`) |
| Windows | DPAPI / Credential Manager |
| Linux | libsecret / GNOME Keyring |

- **Electron**:`safeStorage` API,或 `keytar` 第三方库
- **Tauri**:`tauri-plugin-stronghold`,或自己包 `keyring-rs`

### 4.2 硬件钱包

- **Ledger**:`@ledgerhq/hw-transport-node-hid`(Node)或 Rust 端的 `ledger-transport`
- **Trezor**:`@trezor/connect`
- 桌面端直接走 USB HID,比浏览器走 WebUSB 更稳定

### 4.3 签名与 RPC

- EVM:`ethers.js` / `viem` 完全跨平台
- Solana:`@solana/web3.js`
- 交易广播:自建 RPC 节点,或接入 Infura / Alchemy

### 4.4 自动更新与代码签名

| 平台 | 要点 |
|------|------|
| macOS | Apple Developer 证书 + notarization |
| Windows | 建议 EV Code Signing 证书(否则 SmartScreen 拦截钱包类应用) |
| 更新渠道 | `electron-updater` 或 Tauri updater |

---

## 选型建议

| 场景 | 推荐方案 |
|------|---------|
| 想快速复用现有 Web 钱包 UI | Electron |
| 新项目、性能/安全敏感 | Tauri |
| 已有 RN 移动钱包 | React Native Desktop |
| 想做"扩展 + 桌面代理"模式 | 参考 archived 版 MetaMask Desktop |

---

## 参考资料

- `MetaMask/metamask-desktop`(已归档):<https://github.com/MetaMask/metamask-desktop>
- Electron:<https://www.electronjs.org/>
- Tauri:<https://tauri.app/>
- Rabby Desktop:<https://rabby.io/>
- Ledger HW Transport:<https://github.com/LedgerHQ/ledger-live>
- keytar:<https://github.com/atom/node-keytar>
