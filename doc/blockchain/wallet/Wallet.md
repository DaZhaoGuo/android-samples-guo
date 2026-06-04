<!-- START doctoc generated TOC please keep comment here to allow auto update -->
<!-- DON'T EDIT THIS SECTION, INSTEAD RE-RUN doctoc TO UPDATE -->
**Table of Contents**  *generated with [DocToc](https://github.com/thlorenz/doctoc)*

- [Wallet Technology Overview](#wallet-technology-overview)
- [wallet分类](#wallet%E5%88%86%E7%B1%BB)
- [Hierarchical Deterministic Wallets (BIP-32/BIP-44)](#hierarchical-deterministic-wallets-bip-32bip-44)
- [Seeds and Mnemonic Codes (BIP-39)](#seeds-and-mnemonic-codes-bip-39)
- [bip-44](#bip-44)
- [Wallet Best Practices](#wallet-best-practices)
    - [Mnemonic Code Words (BIP-39)](#mnemonic-code-words-bip-39)
        - [使用助记码](#%E4%BD%BF%E7%94%A8%E5%8A%A9%E8%AE%B0%E7%A0%81)
            - [python-mnemonic](#python-mnemonic)
            - [ConsenSys/eth-lightwallet](#consensyseth-lightwallet)
            - [npm/bip39](#npmbip39)
            - [BIP-39 生成器（作为独立网页的 BIP-39 生成器）](#bip-39-%E7%94%9F%E6%88%90%E5%99%A8%E4%BD%9C%E4%B8%BA%E7%8B%AC%E7%AB%8B%E7%BD%91%E9%A1%B5%E7%9A%84-bip-39-%E7%94%9F%E6%88%90%E5%99%A8)
        - [web3j](#web3j)
    - [Creating an HD Wallet from the Seed](#creating-an-hd-wallet-from-the-seed)
    - [HD Wallets (BIP-32) and Paths (BIP-43/44)](#hd-wallets-bip-32-and-paths-bip-4344)
- [kind](#kind)
    - [托管钱包](#%E6%89%98%E7%AE%A1%E9%92%B1%E5%8C%85)
        - [Cobo Custody：](#cobo-custody)
            - [Cobo托管类型](#cobo%E6%89%98%E7%AE%A1%E7%B1%BB%E5%9E%8B)

<!-- END doctoc generated TOC please keep comment here to allow auto update -->

# Wallet Technology Overview

控制对用户资金的访问、管理密钥和地址、跟踪余额以及创建和签署交易. 此外，一些以太坊钱包还可以与合约进行交互，例如
ERC20 代币;   
存储和管理用户密钥的系统; 每个钱包都有一个密钥管理组件

# 分类

* Nondeterministic (Random) Wallets
* Deterministic (Seeded) Wallets

确定性或“种子”钱包是包含私钥的钱包，这些私钥均源自单个主密钥或种子。  
种子是一个随机生成的数字，它与其他数据
（例如索引号或“链码”（请参阅扩展的公钥和私钥））组合，以派生任意数量的私钥。  
在确定性钱包中，种子足以恢复所有派生密钥，因此在创建时的单个备份足以保护钱包中的所有资金和智能合约。  
种子也足以用于钱包导出或导入，允许在不同钱包实现之间轻松迁移所有密钥。最先进的确定性钱包形式是比特币BIP-32
标准定义的分层确定性（HD）钱包。
HD 钱包包含以树结构派生的密钥

# Hierarchical Deterministic Wallets (BIP-32/BIP-44)

# Seeds and Mnemonic Codes (BIP-39)
* Random -> mnemonic -> seed -> ECKeyPair

1.根据随机数从2048个常用词汇中选出12/24个单词作为mnemonic codes
2.根据助记词生成种子  
3.seed做sha256后生成公私钥对

```
byte[] initialEntropy = new byte[16];
secureRandom.nextBytes(initialEntropy);

String mnemonic = MnemonicUtils.generateMnemonic(initialEntropy);
byte[] seed = MnemonicUtils.generateSeed(mnemonic, password);
ECKeyPair privateKey = ECKeyPair.create(sha256(seed));
```

# 创建公私钥
seed做sha256后生成公私钥对

* Secp256k1创建公私钥
* priKey: 64位
* pubKey: 128位 priKey椭圆曲线算法得到
* address: 40位 公钥keccak256公钥得到的hash值，取后40位

# bip-44

# Wallet Best Practices

使用助记词（BIP39）   
使用层级确定性钱包（HD Wallets）（BIP32）   
使用多目的HD Wallets（BIP43）   
使用多币种，多账号的HD Wallets (BIP44)

## Mnemonic Code Words (BIP-39)

### 使用助记码

#### python-mnemonic

The reference implementation of the standard by the SatoshiLabs team that proposed BIP-39, in Python

#### ConsenSys/eth-lightwallet

Lightweight JS Ethereum wallet for nodes and browser (with BIP-39)

#### npm/bip39

JavaScript implementation of Bitcoin BIP-39: Mnemonic code for generating deterministic keys

#### BIP-39 生成器（作为独立网页的 BIP-39 生成器）

### web3j

## Creating an HD Wallet from the Seed

## HD Wallets (BIP-32) and Paths (BIP-43/44)

# Wallet分类

## Account Abstract

账户抽象(ERC-4337)钱包使用智能合约管理钱包, 而非EOA(外部拥有地址) 钱包由单个私钥管理

* 资产在合约中管理
* 智能合约钱包是账户抽象的基础：通过将账户的控制从网络中抽象出来并用智能合约进行定义，将各种以太坊账户类型统一为单一类型的过程

由于智能合约钱包无法发起交易，因此必须由 EOA 钱包调用。此过程由多个帐户抽象基础设施提供商（例如
Bundlers）和智能合约（例如 EntryPoint）支持

### work flow

    1.用户想要执行UserOperation
    2.用户操作被发送到“替代内存池”
    3.带有 EOA 钱包的Bundler会捆绑所有 UserOperations 并将其发送到 EntryPoint合约
    4.EntryPoint 合约验证并执行所有 UserOperations
    5.捆绑UserOperations的EOA钱包由用户钱包或Paymaster偿还他们代表用户花费的ETH

Some examples of smart contract wallets that use ERC-4337:

* Soul Wallet
* Candide
* UniPass
* Castle
* Openfort

Two main smart contract wallets that don't use ERC-4337 are Safe and Argent.

## 多签钱包

ex: Gnosis Safe  
建立在智能合约上的钱包，合约中定义了验证逻辑，比如如果需要验证一笔交易，需要一个以上的私钥，或者五个中至少三个私钥进行验证

## MPC(Multi-Party Computation)Wallet

对私钥进行多方计算在链下实现多签、跨链等更复杂的验证   
将一个私钥打碎成多片

## 托管钱包

案例：

### 1.Cobo Custody：

Cobo提供：

* 冷熱混合的多重簽名
* 可定制的多权限管理
* 银行级的硬件安全模块（HSM）等基础安全服务
* 为客户提供财务统计
* 数字资产追溯
* 数字资产交易等增值服务。

#### Cobo托管类型

* 协同托管服务：又称半托管服务或协管服务，即Cobo Custody 与客户通过多重签名的方式，共同管理多签地址下的数字资产。当客户发起一笔交易后，Cobo
  Custody 通过严格的风控测试与信息核对，待信息确认无误后会对该笔交易进行二次签名，完成最终的交易流程。
* 全托管服务：客户将数字资产打入Cobo Custody 为其专门配置的地址中，Cobo Custody
  会根据风控策略，对客户的数字资产进行冷热存储的分离。
  完善的多层次安全风控机制

热钱包服务中，私钥存储在硬件安全模块（HSM）的加密芯片中，且私钥永远不会离开加密芯片，冷钱包服务采用冷热多签混合机制，防止网络攻击。
Cobo Custody 通过限制访问IP、API 调用次数限速、交易限速、交易限额、黑白名单机制、客户历史行为分析、延迟提款、一键叫停等机制保障数字资产安全。

## 其他钱包服务

### Web3Auth

通过多方计算 (MPC) 和帐户抽象保持其安全、非托管和无种子阶段  
使用 Web3Auth 的 MPC 架构及其分布式部分密钥来签署交易并返回部分签名。我们的 TSS 将它们全部结合起来，生成区块链的最终签名