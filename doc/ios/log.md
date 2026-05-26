mac列出当前已连接的iOS设备及uuid
```
xcrun xctrace list devices
```

先执行指令，再操作app

```
idevicesyslog -u 00008110-000150C92186801E | tee iphone.log
```

