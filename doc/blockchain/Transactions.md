# what

外部拥有账户 (EOA) 发起的签名消息，由以太坊网络传输，并记录在以太坊区块链上

# send

eth_sendTransaction   
eth_sendRawTransaction  
personal_sendTransaction

# Structure of a Transaction

```json
 {
  "from": "0xEA674fdDe714fd979de3EdF0F56AA9716B898ec8",
  "to": "0xac03bb73b6a9e108530aff4df5077c2b3d481e5a",
  "gasLimit": "21000",
  "maxFeePerGas": "300",
  "maxPriorityFeePerGas": "10",
  "nonce": "0",
  "value": "10000000000"
}
```

## Nonce

由始发 EOA (externally owned account)发出的序列号，用于防止消息重播

## Gas price

发起者愿意为每单位 gas 支付的以太币数量（以 wei 为单位）
1ETH = 10^9 GWei = 10^18 Wei

## Gas limit

发起人愿意为此交易购买的最大 Gas 量

## to

## Value

发送到目的地的以太币数量（以 wei 为单位）

## Data

## v,r,s

原始 EOA 的 ECDSA 数字签名的三个组成部分

## The Transaction Nonce

nonce：一个标量值，等于从该地址发送的交易数量，或者在具有关联代码的账户的情况下，该账户创建的合约数量。
严格来说，nonce 是始发地址的一个属性；也就是说，它仅在发送地址的上下文中才有意义。但是，nonce 并未明确存储为区块链上
帐户状态的一部分。相反，它是通过计算来自某个地址的已确认交易的数量来动态计算的。
Keeping Track of Nonces
Gaps in Nonces, Duplicate Nonces, and Confirmation
Concurrency, Transaction Origination, and Nonces

## Gas

## Recipient

## Value and Data

Transmitting Value to EOAs and Contracts
Transmitting a Data Payload(有效荷载) to an EOA or Contract
A function selector
The function arguments

## Special Transaction: Contract Creation

## Digital Signatures

(ECDSA)Elliptic Curve椭圆曲线 Digital Signature Algorithm
How Digital Signatures Work
Verifying the Signature
ECDSA Math
Transaction Signing in Practice
Raw Transaction Creation and Signing
Raw Transaction Creation with EIP-155

## The Signature Prefix Value (v) and Public Key Recovery

## Separating Signing and Transmission (Offline Signing)离线签名

## Transaction Propagation

## Recording on the Blockchain

## Multiple-Signature (Multisig) Transactions