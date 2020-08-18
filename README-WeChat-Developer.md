# study-WeChat-Developer #
微信开发者

基于微信开放平台进行开发





## 微信小程序 ##

参考文档 https://developers.weixin.qq.com/miniprogram/dev/framework/quickstart/getstart.html

- 申请开发者账号
- 申请微信小程序
- 安装开发者工具
- 新建无云服务应用







### 新建页面 ###

右键 -》 新建 Page



页面跳转

https://developers.weixin.qq.com/miniprogram/dev/api/route/wx.navigateTo.html



按钮事件

bindtap="someXXX" 执行 someXXX 事件



页面传参

```
res.eventChannel.emit('nextPageEvent', { user: 'bage' })
```

页面获取参数

```
  onLoad: function (options) {
    const eventChannel = this.getOpenerEventChannel()
    eventChannel.on('nextPageEvent', function(data) {
      console.log(data.user)
    })
  },
```





支持直接使用IP ？？？明天进行配置下看看