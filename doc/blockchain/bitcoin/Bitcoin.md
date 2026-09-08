简易版Bitcoin Java版
-根据Bitcoin白皮书实现

## Block

### Genesis()

交易产生第一个Block

```
Block {
    hash,
    previousHash,
    merkleRoot, /hash根
    data,
    transactions,
    timeStamp,
    nonce
}


```

### Nonce

随机数，使得hash的前difficulty位是0

mineBlock(int difficulty)

```
Wallet {
    privateKey, 
    publicKey, 
    UTXOs
}

generateKeyPair // 使用ECC（椭圆曲线算法）生成公私钥    
getBalance    
sendFunds
```

## Transaction

### TransactionInput

每一个输入都对应着一笔交易的输出，沿用比特币白皮书中的描述，将其命名为UTXO

### UTXOs

Unspent Transaction Output, 属于自己且没有被使用的交易输出

## MerkleRoot

### What

任一父节点等于左右子节点数据相加后哈希的结果，所以默克尔树有另外一个名字哈希树(Hash tree)

### Use

mine时从transactions里获取merkleRoot:遍历树

### Function:

打包计算hash   
Sha256(merkleRoot + previousHash + timeStamp + nonce)

## Transaction

```
Transaction {
    transactionId, 
    sender, 
    recipient, //接收方
    value, 
    byte[] signature, 
    inputs, 
    outputs
}     

processTransaction              
verifySignature              
input.UTXO              
calculateHash              
UTXOs.put(output.id,output);              
UTXOs.remove(input.UTXO.id);  
```

## Signature

使用privateKey对交易双方和value等数据进行签名

使用公钥验证签名  
1.只有数字货币的拥有者才能使用；  
2.在交易被矿工挖出之前(共识前)交易不会被篡改