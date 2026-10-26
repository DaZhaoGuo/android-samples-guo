BIP-44 是 BIP-32 的路径约定：m / purpose' / coin_type' / account' / change / address_index。

MetaMask使用 m/44'/60'/0'/0/0

* 44' 表示按 BIP-44
* 60' 是以太坊（SLIP-44）
* 第一个 0' 是第 0 个账户
* 后面的 0 是外部收款地址，以太坊基本不用找零链
* 最后一个 0 是该账户的第 0 个地址
  用这条路径，是为了和 MetaMask 等钱包导入同一套助记词后得到同一个地址