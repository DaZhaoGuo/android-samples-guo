# 4种主流实现方式

## AMM(Automated Market Maker)

Uniswap等

## On-chain Order Book

在链上维护一个完整的买单和卖单列表（CLOB），每一笔挂单和撤单都需要链上确认。

特点：交易透明，提供类似中心化交易所的挂单（Limit Order）功能。

挑战：由于每一笔操作都要支付 Gas 费，往往交易速度较慢、成本高（适合高价值低频交易）。

典型项目：0x, Loopring (路印)

## Off-chain Order Book

利用链下撮合技术，只有最终的成交结果才在链上结算。

特点：兼顾了流动性与去中心化的非托管特性（用户保留资产控制权）。

典型项目：0x (分级撮合机制)

## 去中心化衍生品交易所

dydx