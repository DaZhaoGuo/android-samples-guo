3 sign types of EVM

## eth_sign

危险，直接签bytes, 用户看不懂对什么内容做了签名

## personal_sign

会加前缀：

```
"\x19Ethereum Signed Message:\n"
```

防止： 签名被当成交易重放

## EIP-712（现代主流） 结构化数据哈希和签名

结构化签名。

钱包可展示：

```
Swap 1 ETH
Approve USDT
```

而不是 hex。

现在 DApp 主流

签名的数据源变更：16进制字符串->结构化数据

对类型化的结构化数据（而不是字节串）进行散列和签名的过程

提高链下消息签名对链上的可用性,节省gas以及减少链上交易