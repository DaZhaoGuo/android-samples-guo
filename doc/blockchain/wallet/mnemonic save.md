# 整体方案

```
Android StrongBox/Keystore AES-256-GCM key
↓
加密助记词 / seed / imported private key
↓
密文存 App 私有目录
```

## StrongBox/KeyStore

* 存放密钥
* 不可导出密钥
* 可以限制密钥必须在用户认证后才能使用
* StrongBox可用时优先用StrongBox，它比普通 TEE 隔离更强

## AES-256-GCM

加密算法选 AES/GCM/NoPadding, AES-GCM 能发现密文被篡改, 每次加密都要用随机 IV/nonce，

```
  不要用 DES，也不建议用 3DES，它们主要是兼容旧系统/旧协议，不适合新钱包方案。
  不要用 AES/ECB。
  不建议单独用 AES/CBC，除非你额外做 HMAC 校验；直接用 GCM 更省心，因为它自带认证，能发现密文被篡改。
  GCM 常用 12 字节 IV；IV 不需要保密，但必须和密文一起保存，且同一把 key 下不能重复
```

### 发现密文被篡改的好处

## 不要长期存每个地址的私钥

优先存加密后的 mnemonic/seed，私钥在签名时临时派生，用完清理